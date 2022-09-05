package cn.org.manta.creator;

import cn.org.manta.configuration.DataSourceProperty;
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;

/**
 * @author hwang
 * @description 数据源创建器
 * @date 9/2/22 2:54 PM
 */
public class DataSourceCreator {

  public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
    //TODO 通过调用对应的DataSource的实现类，并进行配置，返回DataSource,这里不进行细致的配置，只进行了简单的配置
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUsername(dataSourceProperty.getUsername());
    dataSource.setPassword(dataSourceProperty.getPassword());
    dataSource.setUrl(dataSourceProperty.getUrl());
    return dataSource;
  }
}
