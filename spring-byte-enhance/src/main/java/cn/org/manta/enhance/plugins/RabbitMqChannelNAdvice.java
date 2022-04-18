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
 * @description 拦截消息队列增强，
 * @date 4/18/22 11:06 AM
 */
@Slf4j
public class RabbitMqChannelNAdvice {
  public RabbitMqChannelNAdvice() {
  }

  @OnMethodEnter
  public static void enter(@Origin Method method) {
    if ("basicPublish".equals(method.getName())) {
      try {
        System.out.println("rabbitmq is publishing message...");
      } catch (Throwable var2) {
        LoggerFactory.getLogger(RabbitMqChannelNAdvice.class).warn(var2.getMessage(), var2);
      }
    }
  }

  @OnMethodExit(
      onThrowable = Throwable.class
  )
  public static void exit(@AllArguments Object[] args, @This Object ths, @Origin Method method, @Return(typing = Typing.DYNAMIC) Object ret, @Thrown Throwable t) {
    try {
      System.out.println("rabbitmq published/consumed message. + " + args + ths + ret + t);
    } catch (Throwable var9) {
      LoggerFactory.getLogger(RabbitMqChannelNAdvice.class).warn("rabbitMq 打点异常:" + var9.getMessage());
    } finally {
    }
  }
}