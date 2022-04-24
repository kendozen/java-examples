package cn.org.manta.apollodeomo.web;

import cn.org.manta.apollodeomo.support.RabbitMqConfig;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwang
 * @description 测试控制器
 * @date 4/18/22 4:52 PM
 */
@RestController
@RequestMapping("/api/home")
public class HomeController {

  @Value("${spring.application.nickname}")
  private String name;

  @Resource
  private RabbitMqConfig rabbitMqConfig;

  @GetMapping("/name")
  public String getName(){
    return this.name;
  }

  @GetMapping("/config")
  public String getConfig(){
    return this.rabbitMqConfig.toString();
  }
}
