package yang.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {


    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date =new Date();
    //handlerAdded 表示建立连接，第一个被执行
    //将当前channel加入到channelGroup

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel =ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        /*
        * 该方法会将channelGroup中所有的channel遍历，并发送消息。我们不需要自己遍历
        * */
        channelGroup.writeAndFlush("[客户端]" +channel.remoteAddress()+"加入聊天"+sdf.format(new java.util.Date())+"\n");
        channelGroup.add(channel);
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    //表示channel处于下线状态，提示xx下线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }

    //断开连接，将客户离开信息推送给当前在线的用户


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel =ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了");
        System.out.println("channelGroup size"+ channelGroup.size());
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取当前channel
        final Channel channel =ctx.channel();
        //这时我们遍历channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(ch ->{
            if (channel != ch){//不是当前的channel，转发消息
                ch.writeAndFlush("[客户]"+channel.remoteAddress()+"发送了："+msg+"\n");
            }else{//回显自己的消息
                ch.writeAndFlush("[自己]发送了消息"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
