package cn.org.manta.rabbitmq.dead.letter;

import cn.org.manta.rabbitmq.Message;
import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description 死信队列是会将正常队列中过期的、消费未成功的消息重新通过死信交换机投递到队列中
 * @date 4/14/22 3:55 PM
 */
public class Producer {

  public static final String DEAD_EXCHANGE = "exchange.dlx";
  public static final String DEAD_QUEUE = "queue.dlx";
  public static final String DEAD_QUEUE_ROUTE_KEY = "dlx-routing-key";

  public static final String BIZ_EXCHANGE = "exchange.biz";
  public static final String BIZ_QUEUE = "queue.biz";

  public static void main(String[] args) throws IOException, TimeoutException {
    Channel channel = createDeadExchangeAndQueue();
    //routing key 必须与订阅的队列一致，否则不会投递而被丢弃
    final HashMap<String, Object> arguments = new HashMap<>();
    arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
    arguments.put("x-dead-letter-routing-key", DEAD_QUEUE_ROUTE_KEY);
    arguments.put("x-message-ttl", 10000);
    Builder headers = new Builder().headers(arguments);
    for(;;){
      Message message = Util.read();
      channel.basicPublish(BIZ_EXCHANGE, message.getRoutingKey(), headers.build(), message.getBody());
    }
  }

  private static Channel createDeadExchangeAndQueue() throws IOException, TimeoutException {
    final Channel channel = Util.getChannel(Util.createFactory());
    // 定义死信队列和死信交换机并将死信队列绑定到死信交换机
    // 这里与正常的定义交换机和队列没有差别；
    channel.exchangeDeclare(DEAD_EXCHANGE, "direct", true, false, null);
    channel.queueDeclare(DEAD_QUEUE, true, false, false, null);
    channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_QUEUE_ROUTE_KEY);

    // 定义正常的交换器
    channel.exchangeDeclare(BIZ_EXCHANGE, "fanout", true, false, null);
    // 这里是额外的属性配置，配置ttl及ttl之后消息将通过什么key发到哪个交换机上
    // 如果不定义x-dead-letter-routing-key，则会使用原本的route key
    // 如果消息和队列都定义了ttl，以俩者最小的那个为ttl
    /*
    final HashMap<String, Object> arguments = new HashMap<>();
    arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
    arguments.put("x-dead-letter-routing-key", DEAD_QUEUE_ROUTE_KEY);
    arguments.put("x-message-ttl", 10000);
     */

    channel.queueDeclare(BIZ_QUEUE, true, false, false, null);
    channel.queueBind(BIZ_QUEUE, BIZ_EXCHANGE, "");
    return channel;
  }
}
