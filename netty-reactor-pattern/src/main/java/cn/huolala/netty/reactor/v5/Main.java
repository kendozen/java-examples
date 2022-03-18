package cn.huolala.netty.reactor.v5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

/**
 * @author hwang
 * @description reactor
 * @date 11/18/21 10:14 AM
 */
public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    ServerSocket socket = serverSocketChannel.socket();
    serverSocketChannel.configureBlocking(false);
    socket.bind(new InetSocketAddress(8888));

    EventLoopGroup boss = new EventLoopGroup(1);
    EventLoopGroup work = new EventLoopGroup(3);

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(boss, work).channel(serverSocketChannel);

    Thread.currentThread().join();
  }
}
