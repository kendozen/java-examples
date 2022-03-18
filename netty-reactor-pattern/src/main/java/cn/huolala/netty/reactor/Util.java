package cn.huolala.netty.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author hwang
 * @description 通用的
 * @date 11/18/21 2:21 PM
 */
public class Util {

  public static String socketToString(SocketChannel channel) {
    return channel.socket().getInetAddress().getHostAddress() + ":" + channel.socket().getPort();
  }


  public static String read(SocketChannel channel) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    StringBuilder data = new StringBuilder();
    while (true) {
      buffer.clear();
      int n = 0;
      try {
        n = channel.read(buffer);
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (n <= 0) {
        break;
      }
      buffer.flip();
      int limit = buffer.limit();
      char[] dst = new char[limit];
      for (int i = 0; i < limit; i++) {
        dst[i] = (char) buffer.get(i);
      }
      data.append(dst);
      buffer.clear();
    }
    return Util.socketToString(channel) + "\t" + data.toString();
  }
}
