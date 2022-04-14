package cn.org.manta.rabbitmq.fanout;

import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description 这里俩个queue使用的都是abc作为routing key，但是生产者发送消息时，不管routing key是什么，这俩个队列都能收到消息
 * @date 4/14/22 11:23 AM
 */
public class Consumer {

  private static final String ROUTE_KEY = "abc";
  private static final String QUEUE_ONE = "queue.fanout.one";
  private static final String QUEUE_TWO = "queue.fanout.two";

  public static void main(String[] args) {
    CompletableFuture.runAsync(() -> consume(QUEUE_ONE));
    consume(QUEUE_TWO);
  }

  private static void consume(String queue){
    try {
      ConnectionFactory factory = Util.createFactory();
      Channel channel = Util.getChannel(factory);
      Util.declareQueue(channel, queue, false, true, true, null);
      channel.queueBind(queue, Producer.EXCHANGE_NAME, ROUTE_KEY, null);

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(queue + " Received '" + message + "'");
      };
      channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
