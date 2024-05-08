package com.kriyatec.dynamic.service;

public interface GenericService<T> {
    T create(String collectionName, T entity);
    T update(String collectionName, String id, T entity);
    T getById(String collectionName, String id);
    void deleteById(String collectionName, String id);
}
