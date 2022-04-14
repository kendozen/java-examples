package cn.org.manta.rabbitmq.fanout;

import cn.org.manta.rabbitmq.Message;
import cn.org.manta.rabbitmq.Util;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hwang
 * @description fanout类型的exchange不会使用routing key，所有绑定到该类型exchange的queue都会收到消息
 * @date 4/14/22 10:52 AM
 */
public class Producer {

  public final static String EXCHANGE_NAME = "exchange.fanout";

  public static void main(String[] args) throws IOException, TimeoutException {
    ConnectionFactory factory = Util.createFactory();
    Channel channel = Util.getChannel(factory);
    //type设置为fanout
    Util.declareExchange(channel, EXCHANGE_NAME, "fanout", false, false, null);
    //fanout会将所有的消息都投递到绑定到exchange的queue上，不会使用routing key
    for(;;){
      Message message = Util.read();
      channel.basicPublish(EXCHANGE_NAME, message.getRoutingKey(), null, message.getBody());
    }
  }
}
