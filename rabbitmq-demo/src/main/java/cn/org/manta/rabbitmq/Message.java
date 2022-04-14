package cn.org.manta.rabbitmq;

import lombok.Builder;
import lombok.Data;

/**
 * @author hwang
 * @description 消息
 * @date 4/14/22 11:54 AM
 */
@Data
@Builder
public class Message {
  private String routingKey;
  private byte[] body;
}
