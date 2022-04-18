package cn.org.manta.enhance.agent;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author hwang
 * @description 监听应用启动事件，在应用启动时加载类之前注入字节码拦截
 * 注意，这个需要在META-INF目录下的spring.factories里面配置，否则不会生效
 * @date 4/18/22 10:19 AM
 */
public class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent>{

  @Override
  public void onApplicationEvent(ApplicationStartingEvent event) {
    MonitorAgent.startAgent(null);
  }
}
