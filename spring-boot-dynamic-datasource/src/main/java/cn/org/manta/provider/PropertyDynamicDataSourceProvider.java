package cn.org.manta.provider;

import cn.org.manta.configuration.DataSourceProperty;
import java.util.Map;
import javax.sql.DataSource;

/**
 * @author hwang
 * @description 配置文件加载数据源
 * @date 9/2/22 3:01 PM
 */
public class PropertyDynamicDataSourceProvider extends AbstractDataSourceProvider{

  private final Map<String, DataSourceProperty> dataSourcePropertiesMap;

  public PropertyDynamicDataSourceProvider(Map<String, DataSourceProperty> dataSourcePropertiesMap){
    this.dataSourcePropertiesMap = dataSourcePropertiesMap;
  }

  @Override
  public Map<String, DataSource> loadDataSources() {
    return null;
  }
}
