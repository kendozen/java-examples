package cn.org.manta.service;

/**
 * @author hwang
 * @description TODO
 * @date 3/18/22 11:09 AM
 */
public interface Pay {

  void prepay(String msg);

  void pay(String msg);
}
