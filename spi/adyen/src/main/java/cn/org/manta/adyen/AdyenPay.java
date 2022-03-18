package cn.org.manta.adyen;


import cn.org.manta.service.Pay;

/**
 * @author hwang
 * @description TODO
 * @date 3/18/22 11:11 AM
 */
public class AdyenPay implements Pay {

  @Override
  public void prepay(String msg) {
    System.out.println("Adyen prepay: " + msg);
  }

  @Override
  public void pay(String msg) {
    System.out.println("Adyen pay:" + msg);
  }
}
