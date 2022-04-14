package cn.org.manta.rabbitmq.direct;

import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description direct是通过routingKey直接将消息投递到队列中，routingKey需要完全匹配,如果多个相同routing key的队列绑定到exchange，
 * 则匹配routing key的所有queue都会收到这条消息
 * @date 4/14/22 11:23 AM
 */
public class Consumer {

  private static final String ROUTE_ONE = "abc";
  private static final String ROUTE_TWO = "efg";

  public static void main(String[] args) {
    //这里有俩个consume使用了一样的routing key，可以看到这俩个消费者能消费到一样的消息
    CompletableFuture.runAsync(() -> consume("queue.fanout.one", ROUTE_ONE));
    CompletableFuture.runAsync(() -> consume("queue.fanout.two", ROUTE_ONE));
    consume("queue.fanout.three", ROUTE_TWO);
  }

  private static void consume(String queue, String routingKey){
    try{
      ConnectionFactory factory = Util.createFactory();
      Channel channel = Util.getChannel(factory);
      Util.declareQueue(channel, queue, false, true, true, null);
      channel.queueBind(queue, Producer.EXCHANGE_DIRECT, routingKey, null);

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(queue + " Received '" + message + "'");
      };
      //这里每个queue有俩个消费者，可以看到消息并不会重复消费
      channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
      channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
