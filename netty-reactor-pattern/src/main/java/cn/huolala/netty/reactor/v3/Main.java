package cn.huolala.netty.reactor.v3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * @author hwang
 * @description 单线程的NIO
 * @date 11/18/21 10:14 AM
 */
public class Main {

  public static void main(String[] args) throws IOException, InterruptedException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    ServerSocket socket = serverSocketChannel.socket();
    //需要配置为非阻塞
    serverSocketChannel.configureBlocking(false);
    socket.bind(new InetSocketAddress(8888));

    EventLoop eventLoop = new EventLoop();
    eventLoop.register(serverSocketChannel);
    //我们把循环和事件处理封装在EventLoop中了，这里每个EventLoop就启动一个线程去执行
    CompletableFuture.runAsync(() -> eventLoop.start());

    Thread.currentThread().join();
  }
}
