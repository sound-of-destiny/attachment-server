package cn.edu.sdu.attachmentServer.service.codec;

import cn.edu.sdu.attachmentServer.service.handler.BitStreamHandler;
import cn.edu.sdu.attachmentServer.service.handler.SignallingHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PortUnificationServerHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Will use the first one bytes to detect a protocol.
        if (in.readableBytes() < 1) {
            return;
        }
        final int magic1 = in.getByte(in.readerIndex());
        final int magic2 = in.getByte(in.writerIndex() - 1);
        log.info("nowProtocol {}", ctx.channel().remoteAddress());
        if (isSignalling(magic1, magic2)) {
            if (ctx.pipeline().get("signalling") == null) {
                switchToSignalling(ctx);
                log.info("switchToSignalling");
            }
        } else {
            if (ctx.pipeline().get("bitStream") == null) {
                switchToBitStream(ctx);
                log.info("switchToBitStream");
            }
        }
        out.add(in);
    }

    private boolean isSignalling(int magic1, int magic2) {
        return magic1 == 0x7e && magic2 == 0x7e;
    }

    private void switchToSignalling(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast("signallingDelimiter", new DelimiterBasedFrameDecoder(65535,
                Unpooled.wrappedBuffer(new byte[]{ 0x7e }),
                Unpooled.wrappedBuffer(new byte[]{ 0x7e }, new byte[]{ 0x7e })));
        pipeline.addLast("signalling", new SignallingHandler());

        if (pipeline.get("bitStreamDelimiter") != null) {
            pipeline.remove("bitStreamDelimiter");
        }
        if (pipeline.get("bitStream") != null) {
            pipeline.remove("bitStream");
        }
    }

    private void switchToBitStream(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast("bitStreamDelimiter", new DelimiterBasedFrameDecoder(65535,
                Unpooled.wrappedBuffer(new byte[]{ 0x30, 0x31, 0x63, 0x64 }),
                Unpooled.wrappedBuffer(new byte[]{ 0x30, 0x31, 0x63, 0x64 }, new byte[]{ 0x30, 0x31, 0x63, 0x64 })));
        pipeline.addLast("bitStream", new BitStreamHandler());

        if (pipeline.get("signallingDelimiter") != null) {
            pipeline.remove("signallingDelimiter");
        }
        if (pipeline.get("signalling") != null) {
            pipeline.remove("signalling");
        }
    }
}
