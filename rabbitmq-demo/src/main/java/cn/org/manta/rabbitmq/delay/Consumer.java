package cn.org.manta.rabbitmq.delay;

import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author hwang
 * @description 消费者
 * @date 4/14/22 3:55 PM
 */
public class Consumer {
  public static void main(String[] args) {
    consume(Producer.QUEUE_DELAY);
  }

  private static void consume(String queue){
    try{
      ConnectionFactory factory = Util.createFactory();
      Channel channel = Util.getChannel(factory);

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
