package com.yang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        //创建ServerSocketChannel->ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector关心时间为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待
        while(true){

            //这里等待一秒
            if(selector.select(1000)==0){
                System.out.println("服务器等待一秒，无连接");
                continue;
            }

            //如果返回的>0 就获取到相关的selectionKey集合
            //1.如果返回的>0,表示已经获取到关注的事件
            //2.selector.selectedKeys() 返回关注事件的集合
            //  通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历Set<SelectionKey> 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();

                //根据key对应的通道发生的事件做相应处理
                if(key.isAcceptable()){//如果是OP_ACCEPT，表示有新的客户端连接
                    //给客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功"+ socketChannel.hashCode());

                    //将SocketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    //将SocketChannel注册到selector，关注事件为OP_READ，同时给socketChannel
                    //关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){//发生OP_READ
                    //通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();

                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();

                    channel.read(buffer);
                    System.out.println("from客户端"+new String(buffer.array()));
                }
                //手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }

        }

    }
}
