package cn.huolala.netty.reactor.v5;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author hwang
 * @description 事件循环
 * @date 11/18/21 11:27 AM
 */
public class EventLoop {

  //把selector封装在一个EventLoop里，那么就可以在一个EventLoop里监控多个Socket的读写事件了
  private final Selector selector;
  private Consumer<ServerSocketChannel> acceptConsumer;
  private Consumer<SocketChannel> readConsumer;

  public EventLoop() throws IOException {
    selector = Selector.open();
  }

  public void start() {
    CompletableFuture.runAsync(() -> {
      try {
        start0();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void start0() throws IOException {
    for (; ; ) {
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
          //通过注册将事件传递出去
          if (Objects.nonNull(acceptConsumer)) {
            acceptConsumer.accept(socketChannel);
          }
        } else if (next.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) next.channel();
          if (Objects.nonNull(readConsumer)) {
            readConsumer.accept(socketChannel);
          }
        }
        iterator.remove();
      }
    }
  }

  public void registerAcceptConsumer(Consumer<ServerSocketChannel> consumer) {
    this.acceptConsumer = consumer;
  }

  public void registerReadConsumer(Consumer<SocketChannel> consumer) {
    this.readConsumer = consumer;
  }

  public void register(SelectableChannel socketChannel) throws ClosedChannelException {
    if (socketChannel instanceof ServerSocketChannel) {
      socketChannel.register(selector, SelectionKey.OP_ACCEPT);
    } else if (socketChannel instanceof SocketChannel) {
      socketChannel.register(selector, SelectionKey.OP_READ);
    }
  }
}
