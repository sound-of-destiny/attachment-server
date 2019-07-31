package cn.edu.sdu.attachmentServer.service.handler;

import cn.edu.sdu.attachmentServer.exception.CheckSumErrorException;
import cn.edu.sdu.attachmentServer.protocol.Header;
import cn.edu.sdu.attachmentServer.server.BusinessManager;
import cn.edu.sdu.attachmentServer.util.BCD8421Operator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class SignallingHandler extends ChannelInboundHandlerAdapter {

    private BusinessManager businessManager = BusinessManager.getInstance();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.info("【终端断开】 " + ctx.channel());
                ctx.channel().close();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("【终端连接】 " + ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("【终端断开】 " + ctx.channel());
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        log.error("【发生异常】 " + ctx.channel(), e);
    }

    private boolean isSignalling(int magic1, int magic2) {
        return magic1 == 0x7e && magic2 == 0x7e;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            if (byteBuf.readableBytes() < 1) {
                return;
            }
            final int magic1 = byteBuf.getByte(byteBuf.readerIndex());
            final int magic2 = byteBuf.getByte(byteBuf.writerIndex() - 1);
            if (isSignalling(magic1, magic2)) {
                byteBuf.readByte();
                byteBuf = byteBuf.readRetainedSlice(byteBuf.readableBytes() - 1);
                byteBuf = unEscape(byteBuf);
                if (check(byteBuf)) throw new CheckSumErrorException("checksum error");

                int type = byteBuf.readUnsignedShort();
                int msgBodyProps = byteBuf.readUnsignedShort();
                byte[] BCDBytes = new byte[6];
                byteBuf.readBytes(BCDBytes);
                String terminalPhone = BCD8421Operator.bcd2String(BCDBytes).trim();
                int replyFlowId = byteBuf.readUnsignedShort();
                // 默认无分包

                switch (type) {
                    case 0x1210:
                        alarmFileInfoHandler(byteBuf, ctx, terminalPhone, replyFlowId);
                        break;
                    case 0x1211:
                        fileInfoUploadHandler(byteBuf, ctx, terminalPhone, replyFlowId);
                        break;
                    case 0x1212:
                        fileInfoUploadFinishHandler(byteBuf, ctx, terminalPhone, replyFlowId);
                        break;
                }
            } else {
                final int magic3 = byteBuf.getInt(byteBuf.readerIndex());
                if (magic3 == 0x30316364) {
                    byteBuf.readBytes(4);
                    byte[] fileNameBytes = new byte[50];
                    byteBuf.readBytes(fileNameBytes);
                    String fileName = new String(fileNameBytes);
                    businessManager.putFileName(ctx.channel().id().asLongText(), fileName);
                    long dataOffset = byteBuf.readUnsignedInt();
                    long dataLength = byteBuf.readUnsignedInt();
                    byte[] data = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(data);
                    writeToFile(fileName, data);
                } else {
                    byte[] data = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(data);
                    writeToFile(businessManager.findFileName(ctx.channel().id().asLongText()), data);
                }

            }

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void writeToFile(String fileName, byte[] data) {
        log.info(fileName);
        try (FileOutputStream fos= new FileOutputStream("/home/attach/data/photos/" + fileName.trim(), true)) {
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alarmFileInfoHandler(ByteBuf byteBuf, ChannelHandlerContext ctx, String terminalPhone, int replyFlowId) {
        byte[] terminalIdBytes = new byte[7];
        byteBuf.readBytes(terminalIdBytes);
        String terminalId = new String(terminalIdBytes);

        byte[] alarmFlagBytes = new byte[16];
        byteBuf.readBytes(alarmFlagBytes);
        // TODO
        byte[] alarmCodeBytes = new byte[32];
        byteBuf.readBytes(alarmCodeBytes);
        // TODO
        int infoType = byteBuf.readByte();
        int fileNum = byteBuf.readByte();

        for (int i = 0; i < fileNum; i++) {
            int fileNameLength = byteBuf.readByte();
            String fileName = byteBuf.readCharSequence(fileNameLength, StandardCharsets.UTF_8).toString().trim();
            long fileSize = byteBuf.readUnsignedInt();
        }

        sendCommonResponse(ctx, terminalPhone, 0x1210, replyFlowId);
        log.info("alarmFileInfo");
    }

    private void fileInfoUploadHandler(ByteBuf byteBuf, ChannelHandlerContext ctx, String terminalPhone, int replyFlowId) {
        int fileNameLength = byteBuf.readByte();
        String fileName = byteBuf.readCharSequence(fileNameLength, StandardCharsets.UTF_8).toString().trim();
        int fileType = byteBuf.readByte();
        long fileSize = byteBuf.readUnsignedInt();
        sendCommonResponse(ctx, terminalPhone, 0x1211, replyFlowId);
        log.info("fileInfoUpload");
    }

    private void fileInfoUploadFinishHandler(ByteBuf byteBuf, ChannelHandlerContext ctx, String terminalPhone, int replyFlowId) {
        log.info("fileInfoUploadFinish");
        int fileNameLength = byteBuf.readByte();
        String fileName = byteBuf.readCharSequence(fileNameLength, StandardCharsets.UTF_8).toString().trim();
        int fileType = byteBuf.readByte();
        long fileSize = byteBuf.readUnsignedInt();
        // 回复
        ByteBuf bodyBuf = Unpooled.directBuffer(256);
        bodyBuf.writeByte(fileNameLength);
        bodyBuf.writeBytes(fileName.getBytes());
        bodyBuf.writeByte(fileType);
        bodyBuf.writeByte(0);
        bodyBuf.writeByte(0);

        Header header = new Header(0x9212, BusinessManager.getInstance().currentFlowId(), terminalPhone);
        writeToTerminal(header, ctx, bodyBuf);
        log.info("fileInfoUploadFinishResponse");
    }

    private void sendCommonResponse(ChannelHandlerContext ctx, String terminalPhone, int type, int replyFlowId) {
        ByteBuf bodyBuf = Unpooled.directBuffer(256);
        bodyBuf.writeShort(replyFlowId); // 应答流水号
        bodyBuf.writeShort(type);        // 应答ID
        bodyBuf.writeByte(0);           // 结果
        Header header = new Header(0x8001, BusinessManager.getInstance().currentFlowId(), terminalPhone);
        writeToTerminal(header, ctx, bodyBuf);
    }

    private void writeToTerminal(Header header, ChannelHandlerContext ctx, ByteBuf bodyBuf) {
        header.setMsgBodyLength(bodyBuf.readableBytes());
        header.setMsgBodyPropsField(header.getMsgBodyPropsField());
        ByteBuf headBuf = Unpooled.directBuffer(16);
        headBuf.writeShort(header.getType());
        headBuf.writeShort(header.getMsgBodyPropsField());
        headBuf.writeBytes(BCD8421Operator.string2Bcd(header.getTerminalPhone()));
        headBuf.writeShort(header.getFlowId());
        ByteBuf buf = Unpooled.wrappedBuffer(headBuf, bodyBuf);
        sign(buf);
        buf = escape(buf);
        ctx.writeAndFlush(Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(new byte[]{ 0x7e }),
                buf, Unpooled.wrappedBuffer(new byte[]{ 0x7e })).retain());
    }

    private byte xor(ByteBuf byteBuf) {
        byte cs = 0;
        while (byteBuf.isReadable())
            cs ^= byteBuf.readByte();
        byteBuf.resetReaderIndex();
        return cs;
    }

    private void sign(ByteBuf buf) {
        byte checkCode = xor(buf);
        buf.writeByte(checkCode);
    }

    private ByteBuf[] slice(ByteBuf byteBuf, int index, int length) {
        byte first = byteBuf.getByte(index + length - 1);

        ByteBuf[] bufs = new ByteBuf[2];
        bufs[0] = byteBuf.slice(index, length);

        if (first == 0x7d)
            // 0x01 不做处理 p47
            bufs[1] = Unpooled.wrappedBuffer(new byte[]{0x01});
        else {
            byteBuf.setByte(index + length - 1, 0x7d);
            bufs[1] = Unpooled.wrappedBuffer(new byte[]{0x02});
        }

        return bufs;
    }

    private ByteBuf escape(ByteBuf buf) {
        int low = buf.readerIndex();
        int high = buf.writerIndex();

        int mark = buf.forEachByte(low, high, value -> !(value == 0x7d || value == 0x7e));

        if (mark == -1)
            return buf;

        List<ByteBuf> bufList = new ArrayList<>(5);

        int len;
        do {
            len = mark + 1 - low;
            ByteBuf[] slice = slice(buf, low, len);
            bufList.add(slice[0]);
            bufList.add(slice[1]);
            low += len;

            mark = buf.forEachByte(low, high - low, value -> !(value == 0x7d || value == 0x7e));
        } while (mark > 0);

        bufList.add(buf.slice(low, high - low));

        ByteBuf[] bufs = bufList.toArray(new ByteBuf[0]);

        return Unpooled.wrappedBuffer(bufs);
    }

    private ByteBuf sliceUn(ByteBuf byteBuf, int index, int length) {
        byte second = byteBuf.getByte(index + length - 1);
        if (second == 0x02)
            byteBuf.setByte(index + length - 2, 0x7e);

        // 0x01 不做处理 p47
        // if (second == 0x01) {
        // }
        return byteBuf.slice(index, length - 1);
    }

    private ByteBuf unEscape(ByteBuf buf) {
        int low = buf.readerIndex();
        int high = buf.writerIndex();

        int mark = buf.indexOf(low, high, (byte) 0x7d);
        if (mark == -1)
            return buf;

        List<ByteBuf> bufList = new ArrayList<>();

        int len;
        do {

            len = mark + 2 - low;
            bufList.add(sliceUn(buf, low, len));
            low += len;

            mark = buf.indexOf(low, high, (byte) 0x7d);
        } while (mark > 0);

        bufList.add(buf.slice(low, high - low));

        return new CompositeByteBuf(UnpooledByteBufAllocator.DEFAULT, false, bufList.size(), bufList);
    }

    private boolean check(ByteBuf buf) {
        byte checkCode = buf.getByte(buf.readableBytes() - 1);
        buf = buf.slice(0, buf.readableBytes() - 1);
        byte calculatedCheckCode = xor(buf);

        return checkCode != calculatedCheckCode;
    }
}
