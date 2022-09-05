package cn.org.manta.provider;

import cn.org.manta.configuration.DataSourceProperty;
import cn.org.manta.creator.DataSourceCreator;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hwang
 * @date 9/2/22 2:53 PM
 */
public abstract class AbstractDataSourceProvider implements DynamicDataSourceProvider {

  @Autowired
  private DataSourceCreator dataSourceCreator;

  protected Map<String, DataSource> createDataSourceMap(
      Map<String, DataSourceProperty> dataSourcePropertiesMap){
    Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);
    for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()){
      DataSourceProperty dataSourceProperty = item.getValue();
      String poolName = dataSourceProperty.getPoolName();
      if (poolName == null || "".equals(poolName)) {
        poolName = item.getKey();
      }
      dataSourceProperty.setPoolName(poolName);
      dataSourceMap.put(poolName, dataSourceCreator.createDataSource(dataSourceProperty));
    }
    return dataSourceMap;
  }
}
