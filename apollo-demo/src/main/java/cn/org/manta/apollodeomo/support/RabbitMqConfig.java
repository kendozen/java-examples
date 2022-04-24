package cn.org.manta.apollodeomo.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hwang
 * @description 通过Configuration注入
 * @date 4/18/22 4:53 PM
 */
@Data
@Configuration
@ConfigurationProperties(value = "rabbitmq")
public class RabbitMqConfig {
  private String host;
  private String userName;
  private String password;

  @Override
  public String toString(){
    return "host: " + host + " userName: " + userName + " password: " + password;
  }

}
