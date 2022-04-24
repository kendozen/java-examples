package cn.org.manta.apollodeomo.support;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author hwang
 * @description 这里通过@ApolloConfigChangeListener订阅需要的感兴趣的key，并将改变的key通过
 * 环境改变事件发送出去
 * @date 4/19/22 10:33 AM
 */
@Component
public class ApolloConfigRefreshConfig implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @ApolloConfigChangeListener(value = "user.properties", interestedKeyPrefixes = {"rabbitmq"})
  private void refresh(ConfigChangeEvent configChangeEvent){
    applicationContext.publishEvent(new EnvironmentChangeEvent(configChangeEvent.changedKeys()));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
