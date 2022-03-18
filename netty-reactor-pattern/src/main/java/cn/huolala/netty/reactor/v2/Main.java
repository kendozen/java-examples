package cn.huolala.netty.reactor.v2;

import cn.huolala.netty.reactor.Util;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hwang
 * @description 单线程的NIO
 * @date 11/18/21 10:14 AM
 */
public class Main {

  public static void main(String[] args) throws IOException {
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    ServerSocket socket = serverSocketChannel.socket();
    //需要配置为非阻塞
    serverSocketChannel.configureBlocking(false);
    socket.bind(new InetSocketAddress(8888));
    //打开一个selector，一个selector可以注册多个channel
    Selector selector = Selector.open();
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT | SelectionKey.OP_READ);

    //单线程一直判断selector有没有订阅的事件就绪，这里是一个主线程一直执行，所以是单线程的，所有的事件都在一个线程中处理
    for (; ; ) {
      if (selector.select() == 0) {
        continue;
      }
      //拿到所有就绪队列的key，去执行读写操作（主要是读）
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();

      while (iterator.hasNext()) {
        SelectionKey next = iterator.next();
        //新建连接及读取数据的事件，都是在这个主线程中执行的
        if (next.isAcceptable()) {
          ServerSocketChannel socketChannel = (ServerSocketChannel) next.channel();
          // 服务器会为每个新连接创建一个 SocketChannel
          SocketChannel clientSocketChannel = socketChannel.accept();
          clientSocketChannel.configureBlocking(false);
          // 新的连接的channel，注册回这个selector
          clientSocketChannel.register(selector, SelectionKey.OP_READ);
        } else if (next.isReadable()) {
          //数据到达，读取数据做业务处理
          SocketChannel socketChannel = (SocketChannel) next.channel();
          System.out.println(Util.read(socketChannel));
        }
        iterator.remove();
      }
    }
  }
}
