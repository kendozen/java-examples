package cn.org.manta.apollodeomo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig(value = {"user.properties"})
public class ApolloDeomoApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApolloDeomoApplication.class, args);
  }

}
