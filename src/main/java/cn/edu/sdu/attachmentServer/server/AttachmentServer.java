package cn.edu.sdu.attachmentServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttachmentServer {
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public AttachmentServer() {
        bossGroup = new EpollEventLoopGroup(1);
        workerGroup = new EpollEventLoopGroup();
    }

    public void bindAttachmentServer() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(EpollServerSocketChannel.class)
                    .childHandler(new AttachmentInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = b.bind(10005).sync();
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    log.info("---> 附件服务器启动成功, 端口号: " + 10005);
                } else {
                    log.info("---> 附件服务器启动失败");
                }
            });
        } catch (InterruptedException e) {
            log.error("附件服务器启动出错", e);
        }
    }
}
