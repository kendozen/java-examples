package cn.org.manta.configuration;

import cn.org.manta.aop.DynamicDataSourceAnnotationAdvisor;
import cn.org.manta.aop.DynamicDataSourceAnnotationInterceptor;
import cn.org.manta.ds.DynamicRoutingDataSource;
import cn.org.manta.provider.DynamicDataSourceProvider;
import cn.org.manta.provider.PropertyDynamicDataSourceProvider;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import java.util.Map;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

/**
 * @author hwang
 * @description 动态数据源自动装配
 * @date 9/2/22 5:25 PM
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(DataSourceProperty.class)
@Import({DruidDataSourceAutoConfigure.class})
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {

  private final DynamicDataSourceProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public DynamicDataSourceProvider dynamicDataSourceProvider() {
    Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
    return new PropertyDynamicDataSourceProvider(datasourceMap);
  }

  @Bean
  @ConditionalOnMissingBean
  public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
    DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
    dataSource.setProvider(dynamicDataSourceProvider);
    return dataSource;
  }

  @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
  @Bean
  @ConditionalOnMissingBean
  public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
    DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
    DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
    return advisor;
  }
}
