import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

/**
 * @author hwang
 * @description 一个简单的ComplatableFuture的示例
 * @date 4/12/22 10:45 AM
 */
public class Main {

  public static void main(String[] args)
      throws ExecutionException, InterruptedException, TimeoutException {
    CompletableTask<Long> task = CompletableTask.runSupply(() -> {
      IntStream.range(1, 3).forEach( i -> {
        try {
          System.out.println( "times: " + i);
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });

      return 100L;
    });
    /*
    Long result;
    while((result = task.get()) == null){
      //主线程不睡眠一下无法释放cpu给到工作线程执行
      Thread.sleep(1);
    }
     */
    Long result = task.get(4, TimeUnit.SECONDS);
    System.out.println("result is :" + result);

  }
}
