package cn.org.manta.support;

import cn.org.manta.annotation.DS;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

/**
 * @author hwang
 * @description 根据调用，解析DS注解，并获取DS注解中对应的value
 * @date 9/2/22 3:34 PM
 */
public class DataSourceClassResolver {

  private static boolean mpEnabled = false;
  private static Field mapperInterfaceField;

  private final Map<Object, String> dsCache = new ConcurrentHashMap<>();

  //如果注解用于mapper，则需要找到实际的代理接口.mybatis-plus, mybatis-spring情况下
  static {
    Class<?> proxyClass = null;
    try {
      proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.MybatisMapperProxy");
    } catch (ClassNotFoundException e1) {
      try {
        proxyClass = Class.forName("com.baomidou.mybatisplus.core.override.PageMapperProxy");
      } catch (ClassNotFoundException e2) {
        try {
          proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
        } catch (ClassNotFoundException ignored) {
        }
      }
    }
    if (proxyClass != null) {
      try {
        mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
        mapperInterfaceField.setAccessible(true);
        mpEnabled = true;
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public String findDSKey(Method method, Object target){
    if(method.getDeclaringClass() == Object.class){
      return "";
    }
    Object cacheKey = new MethodClassKey(method, target.getClass());
    String ds = dsCache.get(cacheKey);
    if(ds == null){

    }
    return ds;
  }

  private String computeDatasource(Method method, Object target){
    Class<?> targetClass = target.getClass();
    Class<?> userClass = ClassUtils.getUserClass(targetClass);
    // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
    Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
    // 通过桥接器再获取一遍
    specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
    // 从当前方法查找
    String dsAttr = findDataSourceAttribute(specificMethod);
    // 如果在当前方法中找到了该注解，则直接返回
    if (dsAttr != null) {
      return dsAttr;
    }
    // 没有在方法上找到注解，再从当前方法声明的类查找
    dsAttr = findDataSourceAttribute(specificMethod.getDeclaringClass());
    if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
      return dsAttr;
    }
    // 如果还没有找到，则看看是否存在桥接方法
    if (specificMethod != method) {
      // 从桥接方法查找
      dsAttr = findDataSourceAttribute(method);
      if (dsAttr != null) {
        return dsAttr;
      }
      // 从桥接方法声明的类查找
      dsAttr = findDataSourceAttribute(method.getDeclaringClass());
      if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
        return dsAttr;
      }
    }
    return getDefaultDataSourceAttr(target);
  }

  /**
   * 默认的获取数据源名称方式
   *
   * @param targetObject 目标对象
   * @return ds
   */
  private String getDefaultDataSourceAttr(Object targetObject) {
    Class<?> targetClass = targetObject.getClass();
    // 如果不是代理类, 从当前类开始, 不断的找父类的声明
    if (!Proxy.isProxyClass(targetClass)) {
      Class<?> currentClass = targetClass;
      while (currentClass != Object.class) {
        String datasourceAttr = findDataSourceAttribute(currentClass);
        if (datasourceAttr != null) {
          return datasourceAttr;
        }
        currentClass = currentClass.getSuperclass();
      }
    }
    // mybatis-plus, mybatis-spring 的获取方式
    if (mpEnabled) {
      final Class<?> clazz = getMapperInterfaceClass(targetObject);
      if (clazz != null) {
        return findDataSourceAttribute(clazz);
      }
    }
    return null;
  }

  /**
   * 用于处理嵌套代理
   *
   * @param target JDK 代理类对象
   * @return InvocationHandler 的 Class
   */
  private Class<?> getMapperInterfaceClass(Object target) {
    Object current = target;
    while (Proxy.isProxyClass(current.getClass())) {
      Object currentRefObject = AopProxyUtils.getSingletonTarget(current);
      if (currentRefObject == null) {
        break;
      }
      current = currentRefObject;
    }
    try {
      if (Proxy.isProxyClass(current.getClass())) {
        return (Class<?>) mapperInterfaceField.get(Proxy.getInvocationHandler(current));
      }
    } catch (IllegalAccessException ignore) {
    }
    return null;
  }

  /**
   * 通过 AnnotatedElement 查找标记的注解, 映射为  DatasourceHolder
   *
   * @param ae AnnotatedElement
   * @return 数据源映射持有者
   */
  private String findDataSourceAttribute(AnnotatedElement ae) {
    AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, DS.class);
    if (attributes != null) {
      return attributes.getString("value");
    }
    return null;
  }
}
