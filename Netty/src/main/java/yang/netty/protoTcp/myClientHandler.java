package yang.netty.protoTcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class myClientHandler extends SimpleChannelInboundHandler<messageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, messageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("==========================");

        System.out.println("客户端收到消息如下");
        System.out.println("长度"+len);
        System.out.println("内容"+new String(content, Charset.forName("utf-8")));
        System.out.println("客户端接收消息数量" +(++this.count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i <7 ; i++) {
            String mes ="我是测试数据";

            int length = mes.getBytes(Charset.forName("utf-8")).length;
            byte[] content =mes.getBytes(Charset.forName("utf-8"));

            messageProtocol messageProtocol =new messageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息"+cause.getMessage());
        ctx.close();
    }
}
