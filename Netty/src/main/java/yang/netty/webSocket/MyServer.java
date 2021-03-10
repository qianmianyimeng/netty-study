package yang.netty.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyServer {
    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup =new NioEventLoopGroup(1);
        EventLoopGroup workerGroup =new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap =new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline channelPipeline=ch.pipeline();
                            //基于http协议，使用http解码器
                            channelPipeline.addLast(new HttpServerCodec());

                            //是以块方式写，添加ChunkedWriteHandler
                            channelPipeline.addLast(new ChunkedWriteHandler());

                            channelPipeline.addLast(new HttpObjectAggregator(8192));

                            channelPipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            channelPipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
