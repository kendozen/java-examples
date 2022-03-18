package cn.org.manta.adyen;

import cn.org.manta.service.Refund;

/**
 * @author hwang
 * @description TODO
 * @date 3/18/22 11:27 AM
 */
public class AdyenRefund implements Refund {

  @Override
  public void refund(String msg) {
    System.out.println("Adyen refund:" + msg);
  }
}
