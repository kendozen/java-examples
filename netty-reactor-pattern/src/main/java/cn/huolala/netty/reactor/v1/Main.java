package cn.huolala.netty.reactor.v1;

import cn.huolala.netty.reactor.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

/**
 * @author hwang
 * @description BIO多线程，主线程进行accept，收到一个新连接后交给一个新线程循环监听是否有可读取数据
 * @date 11/18/21 9:42 AM
 */
public class Main {

  public static void main(String[] args) throws IOException {
    start();
  }

  private static void start() throws IOException {
    ServerSocket serverSocket = new ServerSocket(8888, 1024);
    System.out.println("server setup...");
    for (; ; ) {
      final Socket socket = serverSocket.accept();
      socket.getInetAddress();
      System.out.println("client connected:" + socket.getInetAddress().getHostAddress());
      //这里需要分配一个线程一直循环并且等待数据到达
      CompletableFuture.runAsync(() -> {
        listen(socket);
      });
    }
  }

  private static void listen(Socket socket) {
    for (; ; ) {
      try {
        //socket.getInputStream()如果没有数据就会一直阻塞等待
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg = reader.readLine();
        //业务处理
        System.out
            .println("client：" + Util.socketToString(socket.getChannel()) + "\tmessage: " + msg);
        write(socket, msg);
      } catch (Exception e) {
        System.out.println("error when listen socket." + e);
        return;
      }
    }
  }

  private static void write(Socket socket, String msg) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    writer.write("server reply:" + msg + "\n");
    writer.flush();
  }
}
