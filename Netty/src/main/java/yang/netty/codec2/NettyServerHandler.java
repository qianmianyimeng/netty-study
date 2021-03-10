package yang.netty.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import yang.netty.codec.studentPOJO;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

        //读取数据事件（这里我们可以读取客户端发送的消息）
        /*
        * 1.channelHandlerContext ctx:上下文对象。含有 管道pipeline,通道channel，地址
        * 2.Object msg:就是客户端发送的数据 默认object
        * */

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {

        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生编号" + student.getId() + "学生名字" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人的编号" + worker.getId() + "工人的名字" + worker.getName());
        } else {
            System.out.println("输入的类型有误");
        }
    }
    //数据读取完毕

    //用户自定义任务
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {

        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端，普通自定义", CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        });

        //用户自定义定时任务
        ctx.channel().eventLoop().schedule(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端，定时自定义", CharsetUtil.UTF_8));
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        },5, TimeUnit.SECONDS);



        //writeAndFlush是write + flush
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端 ~ wang?",CharsetUtil.UTF_8));
    }
    //处理异常,一般是需要关闭通道


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
