package cn.org.manta.startup;

import cn.org.manta.service.Pay;
import cn.org.manta.service.Refund;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author hwang
 * @description TODO
 * @date 3/18/22 11:09 AM
 */
public class Main {

  public static void main(String[] args) {
    ServiceLoader<Pay> serviceLoader = ServiceLoader.load(Pay.class);
    Iterator<Pay> iterator = serviceLoader.iterator();
    while (iterator.hasNext()){
      iterator.next().pay("paid 100 RMB");
    }

    ServiceLoader<Refund> refundServiceLoader = ServiceLoader.load(Refund.class);
    Iterator<Refund> refunds = refundServiceLoader.iterator();
    while (refunds.hasNext()){
      refunds.next().refund("refund 100 RMB");
    }
  }
}
