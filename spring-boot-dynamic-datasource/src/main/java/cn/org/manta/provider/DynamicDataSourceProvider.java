package cn.org.manta.provider;

import java.util.Map;
import javax.sql.DataSource;

/**
 * @author hwang
 * @description 多数据源加载接口，可以自己实现从其他地方加载所有数据源
 * @date 9/2/22 1:56 PM
 */
public interface DynamicDataSourceProvider {
  /**
   * 加载所有数据源
   *
   * @return 所有数据源，key为数据源名称
   */
  Map<String, DataSource> loadDataSources();
}
