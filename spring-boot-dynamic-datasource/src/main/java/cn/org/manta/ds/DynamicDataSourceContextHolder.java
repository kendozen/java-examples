package cn.org.manta.ds;

import java.util.ArrayDeque;
import java.util.Deque;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

/**
 * @author hwang
 * @description 基于ThreadLocal的根据切面解析注入动态数据源配置，根据注入的DS的先进后出的栈顺序获取对应的数据源执行
 * 当方法进入前，会解析方法/类上面的@DS配置的数据源，压入栈，执行完当前方法需要出队
 * @date 9/2/22 11:05 AM
 */
public class DynamicDataSourceContextHolder {
  /**
   * 先进后出的栈满足方法调用与退出的顺序
   */
  private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<String>>("dynamic-datasource") {
    @Override
    protected Deque<String> initialValue() {
      return new ArrayDeque<>();
    }
  };

  private DynamicDataSourceContextHolder() {
  }

  /**
   * 获得当前线程数据源
   *
   * @return 数据源名称
   */
  public static String peek() {
    return LOOKUP_KEY_HOLDER.get().peek();
  }

  /**
   * 设置当前线程数据源
   * <p>
   * 如非必要不要手动调用，调用后确保最终清除
   * </p>
   *
   * @param ds 数据源名称
   */
  public static void push(String ds) {
    LOOKUP_KEY_HOLDER.get().push(StringUtils.isEmpty(ds) ? "" : ds);
  }

  /**
   * 清空当前线程数据源
   * <p>
   * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
   * </p>
   */
  public static void poll() {
    Deque<String> deque = LOOKUP_KEY_HOLDER.get();
    deque.poll();
    if (deque.isEmpty()) {
      LOOKUP_KEY_HOLDER.remove();
    }
  }

  /**
   * 强制清空本地线程
   * <p>
   * 防止内存泄漏，如手动调用了push可调用此方法确保清除
   * </p>
   */
  public static void clear() {
    LOOKUP_KEY_HOLDER.remove();
  }
}
