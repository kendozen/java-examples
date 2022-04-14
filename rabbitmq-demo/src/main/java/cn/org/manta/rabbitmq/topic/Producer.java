package cn.org.manta.rabbitmq.topic;

import cn.org.manta.rabbitmq.Message;
import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description topic类型的exchange可以通过routingKey进行模糊匹配，并将消息投递到匹配到的所有的队列中
 * @date 4/14/22 10:52 AM
 */
public class Producer {

  public static final String EXCHANGE_NAME = "exchange.topic";

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = Util.createFactory();
    Channel channel = Util.getChannel(factory);
    Util.declareExchange(channel, EXCHANGE_NAME, "topic", false, false, "amq.direct");
    //routing key 必须与订阅的队列一致，否则不会投递而被丢弃
    for(;;){
      Message message = Util.read();
      channel.basicPublish(EXCHANGE_NAME, message.getRoutingKey(), null, message.getBody());
    }
  }
}
