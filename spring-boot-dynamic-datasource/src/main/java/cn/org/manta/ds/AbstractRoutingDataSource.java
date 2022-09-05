package cn.org.manta.ds;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.StringUtils;

/**
 * @author hwang
 * @description 抽象动态获取数据源
 * @date 9/2/22 10:50 AM
 */
public abstract class AbstractRoutingDataSource extends AbstractDataSource {

  /**
   * 由子类根据实现返回最终的数据源
   * @return
   */
  protected abstract DataSource determineDataSource();

  @Override
  public Connection getConnection() throws SQLException {
    return determineDataSource().getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return determineDataSource().getConnection(username, password);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (iface.isInstance(this)) {
      return (T) this;
    }
    return determineDataSource().unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return (iface.isInstance(this) || determineDataSource().isWrapperFor(iface));
  }
}
