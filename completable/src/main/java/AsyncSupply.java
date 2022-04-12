import java.util.function.Supplier;

/**
 * @author hwang
 * @description 封装的任务
 * @date 4/12/22 10:36 AM
 */
public class AsyncSupply<V> implements Runnable{

  private CompletableTask<V> task;
  private Supplier<V> fn;

  public AsyncSupply(CompletableTask<V> task, Supplier<V> fn){
    this.task = task;
    this.fn = fn;
  }

  @Override
  public void run() {
    V result = fn.get();
    task.complete(result);
  }
}
