package cn.org.manta.enhance.plugins;

import cn.org.manta.enhance.agent.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author hwang
 * @description 内存缓存拦截定义
 * @date 4/18/22 1:46 PM
 */
public class MemoryCachePlugin implements IPlugin {

  @Override
  public String name() {
    return "memory cache";
  }


  @Override
  public InterceptPoint[] getInterceptPoints() {
    return new InterceptPoint[]{new InterceptPoint() {
      @Override
      public ElementMatcher<TypeDescription> buildTypesMatcher() {
        return ElementMatchers.named("cn.org.manta.enchance.biz.MemoryCache");
      }

      @Override
      public ElementMatcher<MethodDescription> buildMethodsMatcher() {
        return ElementMatchers.named("put").or(ElementMatchers.named("get"));
      }
    }};
  }

  @Override
  public Class<?> adviceClass() {
    return MemoryCacheAdvice.class;
  }
}
