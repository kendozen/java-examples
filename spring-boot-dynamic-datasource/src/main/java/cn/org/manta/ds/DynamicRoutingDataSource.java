package cn.org.manta.ds;

import cn.org.manta.provider.DynamicDataSourceProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * @author hwang
 * @description 动态数据源
 * @date 9/2/22 11:03 AM
 */
@Data
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean,
    DisposableBean {

  private final Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

  private String primary = "master";

  private DynamicDataSourceProvider provider;

  @Override
  protected DataSource determineDataSource() {
    return getDataSource(DynamicDataSourceContextHolder.peek());
  }

  private DataSource getDataSource(String ds) {
    if (StringUtils.hasText(ds)) {
      return determinePrimaryDataSource();
    } else if (dataSourceMap.containsKey(ds)) {
      return dataSourceMap.get(ds);
    }
    return determinePrimaryDataSource();
  }

  private DataSource determinePrimaryDataSource(){
    return dataSourceMap.get(primary);
  }

  @Override
  public void destroy() throws Exception {
    for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
      closeDataSource(item.getValue());
    }
  }

  @Override
  public void afterPropertiesSet() {
    Map<String, DataSource> dataSources = provider.loadDataSources();
    for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
      addDataSource(dsItem.getKey(), dsItem.getValue());
    }
    if(dataSourceMap.containsKey(primary) == false){
      throw new RuntimeException("dynamic-datasource Please check the setting of primary");
    }
  }

  private void closeDataSource(DataSource dataSource)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<? extends DataSource> clazz = dataSource.getClass();
    Method closeMethod = clazz.getDeclaredMethod("close");
    closeMethod.invoke(dataSource);
  }

  private synchronized void addDataSource(String ds, DataSource dataSource){
    if (!dataSourceMap.containsKey(ds)) {
      dataSourceMap.put(ds, dataSource);
    }
  }
}
