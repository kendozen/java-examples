package cn.huolala.netty.reactor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * @author hwang
 * @description 客户端
 * @date 11/18/21 10:14 AM
 */
public class Client {

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("127.0.0.1", 8888);
      CompletableFuture.runAsync(() -> recieve(socket));

      Scanner scanner = new Scanner(System.in);
      while (scanner.hasNextLine()) {
        send(socket, scanner.nextLine());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void send(Socket socket, String msg) throws IOException {
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    bw.write(msg + "\n");
    bw.flush();
  }

  private static void recieve(Socket socket) {
    for (; ; ) {
      try {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = br.readLine();
        if (message == null) {
          continue;
        }
        System.out.println("server reply：" + message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
