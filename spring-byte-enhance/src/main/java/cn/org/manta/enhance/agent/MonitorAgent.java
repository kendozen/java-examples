package cn.org.manta.enhance.agent;

import cn.org.manta.enhance.plugins.IPlugin;
import cn.org.manta.enhance.plugins.MemoryCachePlugin;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.utility.JavaModule;

/**
 * @author hwang
 * @description 代理入口，通过ByteBuddy在类加载时进行字节码的注入
 * @date 4/18/22 10:20 AM
 */
@Slf4j
public class MonitorAgent {

  //这里使用硬编码方式来指定需要的插件，实际的场景中往往通过使用SPI的方式来扫描插件
  private static List<IPlugin> PLUGINS = new ArrayList<>();

  static {
    PLUGINS.add(new MemoryCachePlugin());
  }

  public static void startAgent(String arguments) {
    //将agent安装到当前VM中
    Instrumentation instrumentation = ByteBuddyAgent.install();
    //构建builder，策略是对已加载的类重新transform
    AgentBuilder agentBuilder = new AgentBuilder.Default()
        .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);
    //加载插件
    for (IPlugin plugin : PLUGINS) {
      if (plugin.filter()) {
        log.warn("Plugin {} is not working because filter is true.", plugin.name());
        continue;
      }
      InterceptPoint[] interceptPoints = plugin.getInterceptPoints();
      for (InterceptPoint point : interceptPoints) {
        if (null != plugin.adviceClass()) {
          //定义transformer的拦截类型、拦截方法以及对应的拦截动作
          AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            builder = builder
                .visit(Advice.to(plugin.adviceClass()).on(point.buildMethodsMatcher()));
            return builder;
          };
          agentBuilder = agentBuilder.type(point.buildTypesMatcher()).transform(transformer)
              .asTerminalTransformation();
        }

        if (null != point.methodDelegation()) {
          agentBuilder = delegateMethod(point, agentBuilder);
        }
      }
    }

    // 监听代理事件
    AgentBuilder.Listener listener = new AgentBuilder.Listener() {
      @Override
      public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {
        //log.info("onTransformation onDiscovery：" + s);
      }

      @Override
      public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
        log.info("onTransformation：" + typeDescription);
      }

      @Override
      public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
      }

      @Override
      public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
        log.info("AgentBuilder onError：" + s);
        log.info("AgentBuilder onError：" + throwable.getMessage());
      }

      @Override
      public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

      }
    };

    agentBuilder.with(listener).installOn(instrumentation);
  }

  private static AgentBuilder delegateMethod(InterceptPoint point, AgentBuilder agentBuilder) {
    AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
      builder = builder.method(point.buildMethodsMatcher()).intercept(MethodDelegation.withDefaultConfiguration()
          .withBinders(Morph.Binder.install(Callable.class)).to(point.methodDelegation()));
      return builder;
    };
    agentBuilder = agentBuilder.type(point.buildTypesMatcher()).transform(transformer).asTerminalTransformation();
    return agentBuilder;
  }
}
