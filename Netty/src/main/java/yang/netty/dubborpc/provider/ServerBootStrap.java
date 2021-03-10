package yang.netty.dubborpc.provider;

import yang.netty.dubborpc.netty.NettyServer;

public class ServerBootStrap {
    public static void main(String[] args) {
        //
        NettyServer.startServer("127.0.0.1",7000);
    }
}
