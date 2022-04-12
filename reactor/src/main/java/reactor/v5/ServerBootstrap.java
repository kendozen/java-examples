package reactor.v5;

import cn.manta.reactor.Util;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author hwang
 * @description 初始化启动
 * @date 11/18/21 3:02 PM
 */
public class ServerBootstrap {

  private EventLoopGroup boss;
  private EventLoopGroup child;

  public ServerBootstrap group(EventLoopGroup boss, EventLoopGroup child) {
    this.boss = boss;
    this.child = child;
    return this;
  }

  public ServerBootstrap channel(ServerSocketChannel channel) throws ClosedChannelException {
    //boss注册连接事件，并把新的socket连接放入child中去循环
    boss.registerAcceptConsumer(c -> {
      try {
        SocketChannel clientSocketChannel = c.accept();
        clientSocketChannel.configureBlocking(false);
        child.register(clientSocketChannel);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    //child注册读取事件进行监听，并做相应的业务处理
    child.registerReadConsumer(c -> System.out.println(Util.read(c)));
    boss.register(channel);
    return this;
  }
}
