package cn.org.manta.configuration;

import javax.sql.DataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hwang
 * @date 9/2/22 2:57 PM
 */
@Data
public class DataSourceProperty {
  /**
   * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
   */
  private String poolName;
  /**
   * 连接池类型，如果不设置自动查找 Druid > HikariCp
   */
  private Class<? extends DataSource> type;
  /**
   * JDBC driver
   */
  private String driverClassName;
  /**
   * JDBC url 地址
   */
  private String url;
  /**
   * JDBC 用户名
   */
  private String username;
  /**
   * JDBC 密码
   */
  private String password;
}
