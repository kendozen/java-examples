package cn.org.manta.paymaya;


import cn.org.manta.service.Pay;

/**
 * @author hwang
 * @description TODO
 * @date 3/18/22 11:10 AM
 */
public class PaymayaPay implements Pay {

  @Override
  public void prepay(String msg) {
    System.out.println("paymaya prepay: " + msg);
  }

  @Override
  public void pay(String msg) {
    System.out.println("paymaya pay: " + msg);
  }
}
