package cn.org.manta.rabbitmultivhost.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author hwang
 * @description TODO
 * @date 4/24/22 11:10 AM
 */
public class CooperateConsumer {
  @RabbitListener(
      bindings =
      @QueueBinding(
          value = @Queue(name = "llm_invoice_svc_cooperate_top_up_queue", admins = {"accountRabbitAdmin"}),
          exchange = @Exchange(name = "bme_acc_trans_operate_wallet_exchange", admins = {"accountRabbitAdmin"}),
          key = "op.enterprise.ep_wallet",
          admins = {"accountRabbitAdmin"}),
      containerFactory = "rabbitListenerContainerFactory")
  private void process(String payload, Message message, Channel channel) throws IOException {
    System.out.println(payload);
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    channel.basicAck(deliveryTag, false);
  }
}
