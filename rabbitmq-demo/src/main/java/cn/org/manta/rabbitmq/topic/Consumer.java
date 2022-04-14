package cn.org.manta.rabbitmq.topic;

import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description topic可以使用多个单词，用.分开，常见的示例如 business.type.user， #可以替代多个单词，*可以替代一个单词
 * @date 4/14/22 11:23 AM
 */
public class Consumer {

  private static final String QUEUE_ONE = "queue.topic.one";
  private static final String QUEUE_TWO = "queue.topic.two";

  public static void main(String[] args) throws IOException, TimeoutException {
    // #可以替代多个单词，*可以替代一个单词
    // 这个会匹配所有使用user开头的routing key，例如user.abc user.a.b.c等，都可以匹配
    CompletableFuture.runAsync(() -> consume(QUEUE_ONE, "user.#"));
    //这里使用*号可以替代匹配一个单词，可以匹配例如 user.a.order user.b.order
    CompletableFuture.runAsync(() -> consume(QUEUE_TWO, "user.*.order"));
  }

  private static void consume(String queue, String routingKey){
    try{
      ConnectionFactory factory = Util.createFactory();
      Channel channel = Util.getChannel(factory);
      Util.declareQueue(channel, queue, false, false, false, null);
      channel.queueBind(queue, Producer.EXCHANGE_NAME, routingKey, null);

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
