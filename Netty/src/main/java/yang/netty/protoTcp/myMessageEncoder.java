package yang.netty.protoTcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class myMessageEncoder extends MessageToByteEncoder<messageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, messageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MME encode 方法被调用");

        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
