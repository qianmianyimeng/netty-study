package yang.netty.inBoundHandlerAndOutBoundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class myServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new myByteToLongDecoder());
        pipeline.addLast(new myLongToByteEncoder());
        pipeline.addLast(new myServerHandler());
    }
}
