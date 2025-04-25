package clients.base.db.impl;

import clients.base.db.interfaces.IDatabase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PostgresImpl implements IDatabase {

  @Override
  public void setData(String url, String user, String password, String query) {

  }

  @Override
  public <T> List<T> getListData(String url, String user, String password, Class<T> entityClass, String query) {
    return List.of();
  }
}