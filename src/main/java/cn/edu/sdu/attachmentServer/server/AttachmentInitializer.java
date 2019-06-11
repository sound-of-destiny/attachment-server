package cn.edu.sdu.attachmentServer.server;

import cn.edu.sdu.attachmentServer.service.handler.SignallingHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class AttachmentInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("idleStateHandler", new IdleStateHandler(300,
                0, 0, TimeUnit.SECONDS));
        pipeline.addLast("protocol", new SignallingHandler());
    }
}
