package cn.org.manta.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description 常量
 * @date 4/14/22 10:51 AM
 */
public final class Util {

  public static ConnectionFactory createFactory(){
    ConnectionFactory factory = new ConnectionFactory();
    //factory.setHost(HOST);
    //factory.setUsername(USER_NAME);
    //factory.setPassword(PASSWORD);
    return factory;
  }

  public static Channel getChannel(ConnectionFactory factory) throws IOException, TimeoutException {
    Connection connection = factory.newConnection();
    return connection.createChannel();
  }

  public static void declareExchange(Channel channel, String exchange, String type, boolean durable, boolean autoDelete, String alternateExchange)
      throws IOException {
    Map<String, Object> arguments = new HashMap<>();
    if(false == (alternateExchange == null || alternateExchange.equals(""))){
      arguments.put("alternate-exchange", alternateExchange);
    }
    channel.exchangeDeclare(exchange, type, durable, autoDelete, arguments);
  }

  public static void declareQueue(Channel channel, String queue, boolean durable, boolean exclusive, boolean autoDelete,
      Map<String, Object> arguments) throws IOException {
    channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
  }

  public static void exchangeBind(Channel channel, String destination, String source, String routingKey)
      throws IOException {
    channel.exchangeBind(destination, source, routingKey);
  }

  public static void queueBind(Channel channel, String queue, String exchange, String routingKey, Map<String, Object> arguments)
      throws IOException {
    channel.queueBind(queue, exchange, routingKey, arguments);
  }

  public static Message read(){
    Scanner scanner = new Scanner(System.in);
    System.out.println("routing key:");
    String routingKey = scanner.nextLine();
    System.out.println("body:");
    String body = scanner.nextLine();
    return Message.builder().routingKey(routingKey).body(body.getBytes()).build();
  }
}
