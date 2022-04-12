import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import sun.misc.Unsafe;

/**
 * @author hwang
 * @description 一个简单的ComplatableFuture的示例
 * @date 4/12/22 10:31 AM
 */
public class CompletableTask<V> implements Future<V> {

  private V result;

  public void complete(V result) {
    UNSAFE.compareAndSwapObject(this, RESULT, null, result);
  }

  public static <V> CompletableTask<V> runSupply(Supplier<V> supplier){
    CompletableTask<V> task = new CompletableTask<V>();
    ForkJoinPool.commonPool().execute(new AsyncSupply<>(task, supplier));
    return task;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return false;
  }

  @Override
  public V get() {
    return result;
  }

  @Override
  public V get(long timeout, TimeUnit unit) throws InterruptedException{
        ForkJoinPool.managedBlock(new ManagedBlocker() {
          @Override
          public boolean block() throws InterruptedException {
            if(isReleasable()){
              return true;
            }
            //睡眠多久返回，这里会有一个问题，如果timeout比较久，但是工作线程结果很快就返回了，则会浪费时间
            //但是如果每隔多少毫秒去取一次，再睡眠，这样的话线程上下文切换也需要开销
            unit.sleep(timeout);
            return true;
          }

          @Override
          public boolean isReleasable() {
            return result != null;
          }
        });
    return result;
  }


  private static final sun.misc.Unsafe UNSAFE;
  private static final long RESULT;
  static {
    try {
      final sun.misc.Unsafe u;
      UNSAFE = u = createUnsafe();
      Class<?> k = CompletableFuture.class;
      RESULT = u.objectFieldOffset(k.getDeclaredField("result"));
    } catch (Exception x) {
      throw new Error(x);
    }
  }

  private static Unsafe createUnsafe() {
    try {
      Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      Field field = unsafeClass.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      Unsafe unsafe = (Unsafe) field.get(null);
      return unsafe;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
