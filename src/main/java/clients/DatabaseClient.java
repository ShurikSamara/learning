package clients;

import clients.base.db.interfaces.IDatabase;

import java.util.List;

public abstract class DatabaseClient {

  private final IDatabase database;

  public DatabaseClient(IDatabase database) {
    this.database = database;
  }

  public <T> List<T> getListData(String url, String user, String password, Class<T> entityClass, String query) {
    return database.getListData(url, user, password, entityClass, query);
  }

  public void setData(String url, String user, String password, String query) {
    database.setData(url, user, password, query);
  }
}