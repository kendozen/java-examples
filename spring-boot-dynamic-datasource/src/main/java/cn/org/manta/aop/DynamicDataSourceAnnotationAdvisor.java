package cn.org.manta.aop;

import cn.org.manta.annotation.DS;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author hwang
 * @description 需要拦截注入的织入点
 * @date 9/2/22 5:02 PM
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor {

  private final Advice advice;
  private final Pointcut pointcut;

  public DynamicDataSourceAnnotationAdvisor(DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor){
    this.advice = dynamicDataSourceAnnotationInterceptor;
    this.pointcut = buildPointcut();
  }

  @Override
  public Pointcut getPointcut() {
    return this.pointcut;
  }

  @Override
  public Advice getAdvice() {
    return this.advice;
  }

  private Pointcut buildPointcut() {
    return new AnnotationMatchingPointcut(DS.class, true);
    //Pointcut cpc = new AnnotationMatchingPointcut(DS.class, true);
    //Pointcut mpc = new AnnotationMethodPoint(DS.class);
    //return new ComposablePointcut(cpc).union(mpc);
  }
}
