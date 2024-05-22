package com.example.adminservice.Service;

import java.util.List;
import java.util.Map;

public interface GenericCrudService {
    <T extends BaseEntity> void saveEntity(T entity);
    <T extends BaseEntity> List<T> getAllEntities(Class<T> clazz);
    <T extends BaseEntity> T getEntityById(Class<T> clazz, Long id);
    <T extends BaseEntity> void saveOrUpdateEntity(T entity);
    <T extends BaseEntity> List<T> getEntitiesByCriteria(Class<T> clazz, Map<String, String> criteria);
    <T extends BaseEntity> void deleteAllEntities(Class<T> clazz);
}
