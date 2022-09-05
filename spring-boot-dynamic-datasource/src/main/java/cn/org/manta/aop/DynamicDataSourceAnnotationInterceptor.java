package cn.org.manta.aop;

import cn.org.manta.ds.DynamicDataSourceContextHolder;
import cn.org.manta.support.DataSourceClassResolver;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author hwang
 * @description 动态数据源对方法的拦截器，拦截方法并解析DS注解的值，压入到DynamicDataSourceContextHolder中
 * @date 9/2/22 4:19 PM
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

  private final DataSourceClassResolver dataSourceClassResolver;

  public DynamicDataSourceAnnotationInterceptor(){
    dataSourceClassResolver = new DataSourceClassResolver();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      String dsKey = determineDatasourceKey(invocation);
      DynamicDataSourceContextHolder.push(dsKey);
      return invocation.proceed();
    } finally {
      DynamicDataSourceContextHolder.poll();
    }
  }

  private String determineDatasourceKey(MethodInvocation invocation) {
    return dataSourceClassResolver.findDSKey(invocation.getMethod(), invocation.getThis());
  }
}
