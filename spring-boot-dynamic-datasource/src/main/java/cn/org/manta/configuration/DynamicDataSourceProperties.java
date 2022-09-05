package cn.org.manta.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hwang
 * @date 9/2/22 5:32 PM
 */
@Data
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {

  public static final String PREFIX = "spring.datasource.dynamic";

  /**
   * 每一个数据源
   */
  private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
}
