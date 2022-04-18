package cn.org.manta.enhance.rabbitmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.stereotype.Component;

/**
 * @author hwang
 * @description 消费者客户端
 * @date 4/18/22 11:37 AM
 */
@Component
public class RabbitMQConsumer {
  static {
    ConnectionFactory factory = new ConnectionFactory();
    Connection connection = null;
    try {
      connection = factory.newConnection();
      Channel channel = connection.createChannel();
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" Received '" + message + "'");
      };
      //这里每个queue有俩个消费者，可以看到消息并不会重复消费
      channel.basicConsume("fanout.queue", true, deliverCallback, consumerTag -> { });
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
  }
}
