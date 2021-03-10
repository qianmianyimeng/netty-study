package yang.netty.protoTcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class myServerHandler extends SimpleChannelInboundHandler<messageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, messageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("=================================");

        System.out.println("服务器接收到数据如下");
        System.out.println("长度"+len);
        System.out.println("内容"+ new String(content, Charset.forName("utf-8")));

        System.out.println("服务器接收到消息包数量" + (++this.count));

        String responseContent = UUID.randomUUID().toString();
        int responseLen = responseContent.getBytes("utf-8").length;
        byte[] responseContent2 = responseContent.getBytes("utf-8");

        messageProtocol messageProtocol = new messageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseContent2);

        ctx.writeAndFlush(messageProtocol);
    }
}
