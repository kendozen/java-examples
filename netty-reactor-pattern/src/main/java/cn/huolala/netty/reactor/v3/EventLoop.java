package cn.huolala.netty.reactor.v3;

import cn.huolala.netty.reactor.Util;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author hwang
 * @description 事件循环
 * @date 11/18/21 11:27 AM
 */
public class EventLoop {

  //把selector封装在一个EventLoop里，那么就可以在一个EventLoop里监控多个Socket的读写事件了
  private final Selector selector;

  public EventLoop() throws IOException {
    selector = Selector.open();
  }

  public void start() {
    CompletableFuture.runAsync(() -> start0());
  }

  private void start0() {
    for (; ; ) {
      try {
        //一直判断是否有就绪的socket，这里是NIO与BIO的区别，这里不会阻塞
        if (selector.select(1000) == 0) {
          continue;
        }
        //拿到所有就绪队列的key，去执行读写操作（主要是读）
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();

        while (iterator.hasNext()) {
          SelectionKey next = iterator.next();
          if (next.isAcceptable()) {
            ServerSocketChannel socketChannel = (ServerSocketChannel) next.channel();
            // 服务器会为每个新连接创建一个 SocketChannel
            SocketChannel clientSocketChannel = socketChannel.accept();
            clientSocketChannel.configureBlocking(false);
            // 新的连接的channel，注册回这个selector
            register(clientSocketChannel);
            System.out.println(clientSocketChannel.socket().getPort());
          } else if (next.isReadable()) {
            //读事件，直接读取
            SocketChannel socketChannel = (SocketChannel) next.channel();
            //注意这里的读事件，这里可以封装成handler，后续通过pipline的方式去触发
            System.out.println(Util.read(socketChannel));
          }
          iterator.remove();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void register(SelectableChannel socketChannel) throws ClosedChannelException {
    if (socketChannel instanceof ServerSocketChannel) {
      socketChannel.register(selector, SelectionKey.OP_ACCEPT);
    } else if (socketChannel instanceof SocketChannel) {
      socketChannel.register(selector, SelectionKey.OP_READ);
    }
  }
}
