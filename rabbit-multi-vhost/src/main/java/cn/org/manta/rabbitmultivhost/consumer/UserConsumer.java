package cn.org.manta.rabbitmultivhost.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hwang
 * @description 消费者
 * @date 4/24/22 11:10 AM
 */
@Component
public class UserConsumer {
  @RabbitListener(
      bindings =
      @QueueBinding(
          value = @Queue(name = "llm_invoice_svc_user_top_up_queue", admins = {"accountRabbitAdmin"}), //要设置admins，不然所有的vhost都会创建
          exchange = @Exchange(name = "bme_acc_trans_operate_wallet_exchange", admins = {"accountRabbitAdmin"}),//要设置admins，不然所有的vhost都会创建
          key = "op.person.user_wallet",
          admins = {"accountRabbitAdmin"}),//要设置admins，不然所有的vhost都会创建
      containerFactory = "rabbitListenerContainerFactory")
  private void process(String payload, Message message, Channel channel) throws IOException {
    System.out.println(payload);
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    channel.basicAck(deliveryTag, false);
  }
}
