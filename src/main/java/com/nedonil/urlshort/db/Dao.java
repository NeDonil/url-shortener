package com.nedonil.urlshort.db;

import java.util.List;

public interface Dao<T> {

    List<T> findAll();

    void save(T t);

    void deleteByKey(String id);

    T findByKey(String key);
}