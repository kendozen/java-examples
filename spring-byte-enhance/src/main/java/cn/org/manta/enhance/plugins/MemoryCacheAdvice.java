package cn.org.manta.enhance.plugins;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.asm.Advice.Thrown;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import org.slf4j.LoggerFactory;

/**
 * @author hwang
 * @description 内存缓存拦截
 * @date 4/18/22 1:48 PM
 */
@Slf4j
public class MemoryCacheAdvice {
  @OnMethodEnter
  public static void enter(@AllArguments Object[] args) {
    System.out.println("cache excecuting {}");
    for(Object o :args){
      System.out.println(o.toString());
    }
  }

  @OnMethodExit(
      onThrowable = Throwable.class
  )
  public static void exit(@AllArguments Object[] args, @This Object target, @Origin Method method, @Return(typing = Typing.DYNAMIC) Object ret, @Thrown Throwable t) {
    try {
      System.out.println("cache exit");
      for(Object o :args){
        System.out.println(o.toString());
      }
      System.out.println(ret == null ? "" : ret.toString());
    } catch (Throwable var9) {
      LoggerFactory.getLogger(RabbitMqChannelNAdvice.class).warn("cache 打点异常:" + var9.getMessage());
    } finally {
    }
  }
}
