package cn.org.manta.enhance.plugins;

import cn.org.manta.enhance.agent.InterceptPoint;

/**
 * @author hwang
 * @description 插件模式
 * @date 4/18/22 10:17 AM
 */
public interface IPlugin {

  String name();

  InterceptPoint[] getInterceptPoints();

  /**
   * 拦截器类
   *
   * @return 拦截器类
   */
  default Class<?> adviceClass() {return null;}

  /**
   * 是否过滤 插件.
   * @return boolean
   */
  default boolean filter() {
    return false;
  }
}
