package cn.org.manta.rabbitmq.delay;

import cn.org.manta.rabbitmq.Message;
import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description 延时交换机，消息会被延时投递至queue,需要再rabbitmq服务器启动插件(先下载再安装)
 * 下载 wget https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.9.0/rabbitmq_delayed_message_exchange-3.9.0.ez && mv ./rabbitmq_delayed_message_exchange-3.9.0.ez ./plugins/
 * 安装 rabbitmq-plugins enable rabbitmq_delayed_message_exchange
 * @date 4/14/22 4:20 PM
 */
public class Producer {

  public static final String EXCHANGE_DELAY = "exchange.delay";
  public static final String QUEUE_DELAY = "queue.delay";
  public static final String ROUTE_KEY = "exchange.delay";

  public static void main(String[] args) throws IOException, TimeoutException {
    Channel channel = createDeadExchangeAndQueue();
    AMQP.BasicProperties.Builder propertyBuilder = new AMQP.BasicProperties.Builder();
    Map<String, Object> headers = new HashMap<>();
    // 延时5秒后投递
    headers.put("x-delay", 5000);
    propertyBuilder.headers(headers);
    BasicProperties properties = propertyBuilder.build();
    for(;;){
      Message message = Util.read();
      channel.basicPublish(EXCHANGE_DELAY, message.getRoutingKey(), properties, message.getBody());
    }
  }

  private static Channel createDeadExchangeAndQueue() throws IOException, TimeoutException {
    final Channel channel = Util.getChannel(Util.createFactory());
    Map<String, Object> args = new HashMap<>();
    // 这里x-delayed-type定义交换机的基础类型，即topic fanout direct三种
    args.put("x-delayed-type", "direct");
    // 这里交换机的类型设置为x-delayed-message，表示延时队列消息
    channel.exchangeDeclare(EXCHANGE_DELAY, "x-delayed-message", true, false, args);
    channel.queueDeclare(QUEUE_DELAY, true, false, false, null);
    channel.queueBind(QUEUE_DELAY, EXCHANGE_DELAY, ROUTE_KEY);
    return channel;
  }
}
