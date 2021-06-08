package sample.dal.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    T getById(int id);
    boolean insert (T item);
    boolean update (T item);
    boolean delete (int id);
}
