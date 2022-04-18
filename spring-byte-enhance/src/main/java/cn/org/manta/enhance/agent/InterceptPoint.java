package cn.org.manta.enhance.agent;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author hwang
 * @description 定义需要拦截的类型和方法
 * @date 4/18/22 10:23 AM
 */
public interface InterceptPoint {
  /**
   * 类匹配规则
   * @return 类匹配规则
   */
  ElementMatcher<TypeDescription> buildTypesMatcher();

  /**
   * 方法匹配规则
   * @return 方法匹配规则
   */
  ElementMatcher<MethodDescription> buildMethodsMatcher();

  /**
   * 代替需要拦截的方法
   * @return
   */
  default Class<?> methodDelegation() { return null;}
}
