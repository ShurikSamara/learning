package clients.base.db.interfaces;

import java.util.List;

public interface IDatabase {
  void setData(String url, String user, String password, String query);
  <T> List<T> getListData(String url, String user, String password, Class<T> entityClass, String query);
}