package cn.huolala.netty.reactor.v4;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author hwang
 * @description 多线程的模型，一个EventLoopGroup对应多一个EventLoop，每个EventLoop对应一个线程和一个selector
 * @date 11/18/21 11:37 AM
 */
public class EventLoopGroup {

  private final List<EventLoop> eventLoopList;
  private int lastELIndex = 0;

  public EventLoopGroup(int count) {
    eventLoopList = new ArrayList<>(count);
    initEventLoop(count);
  }

  private void initEventLoop(int count) {
    IntStream.range(0, count).forEach(i -> {
      try {
        EventLoop eventLoop = new EventLoop();
        eventLoop.start();
        eventLoopList.add(eventLoop);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  public void register(SelectableChannel channel) throws ClosedChannelException {
    getNextEventLoop().register(channel);
  }

  private EventLoop getNextEventLoop() {
    if (lastELIndex >= eventLoopList.size()) {
      lastELIndex = 0;
    }
    return eventLoopList.get(lastELIndex++);
  }
}
