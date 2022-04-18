package cn.org.manta.enhance.plugins;

import cn.org.manta.enhance.agent.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author hwang
 * @description 拦截rabbitmq消息消费和推送方法
 * @date 4/18/22 11:04 AM
 */
public class RabbitMqChannelNPlugin implements IPlugin {

  @Override
  public String name() {
    return "amqpChannelN";
  }

  @Override
  public InterceptPoint[] getInterceptPoints() {
    return new InterceptPoint[]{new InterceptPoint() {
      @Override
      public ElementMatcher<TypeDescription> buildTypesMatcher() {
        return ElementMatchers.named("com.rabbitmq.client.impl.ChannelN");
      }

      @Override
      public ElementMatcher<MethodDescription> buildMethodsMatcher() {
        return ElementMatchers.named("basicConsume");
      }
    }};
  }

  @Override
  public Class<?> adviceClass() {
    return RabbitMqChannelNAdvice.class;
  }

  @Override
  public boolean filter() {
    return false;
  }

}

