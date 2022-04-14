package cn.org.manta.rabbitmq.direct;

import cn.org.manta.rabbitmq.Message;
import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description direct类型的exchange，只会投递与routing key一致的消息到queue中，所有订阅的queue都会被投递。
 * 假如有n个queue使用了一样的routing key，那么推送时会将消息推送到这n个queue上
 * @date 4/14/22 10:52 AM
 */
public class Producer {

  public static final String EXCHANGE_DIRECT = "exchange.direct";

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = Util.createFactory();
    Channel channel = Util.getChannel(factory);
    //type生命为direct
    Util.declareExchange(channel, EXCHANGE_DIRECT, "direct", false, false, null);
    //routing key 必须与订阅的队列一致，否则不会投递而被丢弃
    for(;;){
      Message message = Util.read();
      channel.basicPublish(EXCHANGE_DIRECT, message.getRoutingKey(), null, message.getBody());
    }
  }
}
