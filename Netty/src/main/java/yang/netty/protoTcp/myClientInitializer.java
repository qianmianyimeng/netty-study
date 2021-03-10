package yang.netty.protoTcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class myClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline =ch.pipeline();
        pipeline.addLast(new myMessageEncoder());
        pipeline.addLast(new myMessageDecoder());
        pipeline.addLast(new myClientHandler());
    }
}
