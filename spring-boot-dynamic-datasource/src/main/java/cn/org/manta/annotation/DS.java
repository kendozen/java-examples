package cn.org.manta.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hwang
 * @description 设置数据源的注解，会根据该注解的value进行数据源的切换
 * @date 9/2/22 3:28 PM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {

  /**
   * 待使用的数据源的名称
   * @return
   */
  String value();
}
