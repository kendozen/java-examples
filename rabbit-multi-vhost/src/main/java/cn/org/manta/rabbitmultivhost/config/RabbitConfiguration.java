package cn.org.manta.rabbitmultivhost.config;

import java.time.Duration;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Ssl;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hwang
 * @description 演示如何进行连接各参数都相同，仅仅不同vhost的方式
 * 需要创建不同的connectionFactory和containerFactory
 * @date 4/24/22 11:07 AM
 */
@Configuration
public class RabbitConfiguration {

  @Bean(value = "accountRabbitCachingConnectionFactory")
  public CachingConnectionFactory createTransRabbitConnection(@Autowired RabbitProperties properties,
      @Value("${spring.rabbitmq.virtual-host.acc_trans}") String vhost) throws Exception {
    return new CachingConnectionFactory(getRabbitConnectionFactoryBean(properties, vhost).getObject());
  }

  @Bean(value = "hpayRabbitCachingConnectionFactory")
  public CachingConnectionFactory createHpayRabbitConnection(@Autowired RabbitProperties properties,
      @Value("${spring.rabbitmq.virtual-host.hpay}") String vhost) throws Exception {
    return new CachingConnectionFactory(getRabbitConnectionFactoryBean(properties, vhost).getObject());
  }

  @Bean(value = "rabbitListenerContainerFactory")
  public SimpleRabbitListenerContainerFactory createAccTransRabbitListenerContainerFactory(
      @Autowired SimpleRabbitListenerContainerFactoryConfigurer configurer,
      @Qualifier("accountRabbitCachingConnectionFactory") CachingConnectionFactory factory){
    SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(containerFactory, factory);
    return containerFactory;
  }

  @Bean(value = "hpayRabbitListenerContainerFactory")
  public SimpleRabbitListenerContainerFactory createHpayRabbitListenerContainerFactory(
      @Autowired SimpleRabbitListenerContainerFactoryConfigurer configurer,
      @Qualifier("hpayRabbitCachingConnectionFactory") CachingConnectionFactory factory){
    SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(containerFactory, factory);
    return containerFactory;
  }

  @Bean(value = "hpayRabbitAdmin")
  public RabbitAdmin createHpayRabbitAdmin(@Qualifier("hpayRabbitCachingConnectionFactory") CachingConnectionFactory factory){
    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
    rabbitAdmin.setAutoStartup(true);
    return rabbitAdmin;
  }

  @Bean(value = "accountRabbitAdmin")
  public RabbitAdmin createTransRabbitAdmin(@Qualifier("accountRabbitCachingConnectionFactory") CachingConnectionFactory factory){
    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
    rabbitAdmin.setAutoStartup(true);
    return rabbitAdmin;
  }

  //除了vhost配置，其他配置都相同，使用系统自带的配置代码
  private RabbitConnectionFactoryBean getRabbitConnectionFactoryBean(RabbitProperties properties, String vhost) {
    PropertyMapper map = PropertyMapper.get();
    RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
    map.from(properties::determineHost).whenNonNull().to(factory::setHost);
    map.from(properties::determinePort).to(factory::setPort);
    map.from(properties::determineUsername).whenNonNull().to(factory::setUsername);
    map.from(properties::determinePassword).whenNonNull().to(factory::setPassword);
    map.from(properties::getRequestedHeartbeat).whenNonNull().asInt(Duration::getSeconds)
        .to(factory::setRequestedHeartbeat);
    factory.setVirtualHost(vhost);
    Ssl ssl = properties.getSsl();
    if (ssl.determineEnabled()) {
      factory.setUseSSL(true);
      map.from(ssl::getAlgorithm).whenNonNull().to(factory::setSslAlgorithm);
      map.from(ssl::getKeyStoreType).to(factory::setKeyStoreType);
      map.from(ssl::getKeyStore).to(factory::setKeyStore);
      map.from(ssl::getKeyStorePassword).to(factory::setKeyStorePassphrase);
      map.from(ssl::getTrustStoreType).to(factory::setTrustStoreType);
      map.from(ssl::getTrustStore).to(factory::setTrustStore);
      map.from(ssl::getTrustStorePassword).to(factory::setTrustStorePassphrase);
      map.from(ssl::isValidateServerCertificate)
          .to((validate) -> factory.setSkipServerCertificateValidation(!validate));
      map.from(ssl::getVerifyHostname).to(factory::setEnableHostnameVerification);
    }
    map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
        .to(factory::setConnectionTimeout);
    factory.afterPropertiesSet();
    return factory;
  }
}