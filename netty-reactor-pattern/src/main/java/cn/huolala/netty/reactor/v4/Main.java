package cn.huolala.netty.reactor.v4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

/**
 * @author hwang
 * @description 多线程的NIO
 * @date 11/18/21 10:14 AM
 */
public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    ServerSocket socket = serverSocketChannel.socket();
    //需要配置为非阻塞
    serverSocketChannel.configureBlocking(false);
    socket.bind(new InetSocketAddress(8888));
    //在这里我们把EventLoop放到了EventLoopGroup中，即一个EventLoopGroup对应多个EventLoop
    EventLoopGroup boss = new EventLoopGroup(3);
    boss.register(serverSocketChannel);

    Thread.currentThread().join();
  }
}
