package com.example.adminservice.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.EntityCrudService;
import com.example.adminservice.Service.GenericCrudService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class EntityCrudServiceImpl implements EntityCrudService {

    @Autowired
    private GenericEntityServiceImpl genericEntityServiceImpl;

    public void saveEntity(String entityType, Object params) throws Exception {
        String packageName = "com.example.adminservice.Model."; 
        Class<?> clazz = Class.forName(packageName + entityType);
        
        if (params instanceof Map) {
            saveSingleEntity(clazz, (Map<String, Object>) params);
        } else if (params instanceof List) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) params;
            for (Map<String, Object> data : dataList) {
                saveSingleEntity(clazz, data);
            }
        } else {
            throw new IllegalArgumentException("Unsupported params type: " + params.getClass().getName());
        }
    }

    private void saveSingleEntity(Class<?> clazz, Map<String, Object> dataMap) throws Exception {
        BaseEntity entity = (BaseEntity) clazz.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(entity, fieldValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to set field: " + fieldName, e);
            }
        }

        genericEntityServiceImpl.saveEntity(entity);
    }

    @Override
    public List<?> getAllEntities(String entityType) throws Exception {
        String packageName = "com.example.adminservice.Model."; 
        Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>) Class.forName(packageName + entityType);
        return genericEntityServiceImpl.getAllEntities(clazz);
    }
    @Override
    public BaseEntity getEntityById(String entityType, Long id) throws Exception {
        String packageName = "com.example.adminservice.Model."; // Adjust the package name accordingly
        Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>) Class.forName(packageName + entityType);
        return genericEntityServiceImpl.getEntityById(clazz, id);
    }
    @Override
    public void upsertEntity(String entityType, Long id, Map<String, Object> params) throws Exception {
        String packageName = "com.example.adminservice.Model."; 
        Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>) Class.forName(packageName + entityType);
        BaseEntity entity = (BaseEntity) clazz.getDeclaredConstructor().newInstance();

        BaseEntity existingEntity = genericEntityServiceImpl.getEntityById(clazz, id);
        if (existingEntity != null) {
            entity = existingEntity;
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, fieldValue);
        }

        genericEntityServiceImpl.saveOrUpdateEntity(entity);
    }
    @Override
    public List<? extends BaseEntity> getEntitiesByCriteria(String entityType, Map<String, String> criteria) throws Exception {
        String packageName = "com.example.adminservice.Model."; // Adjust the package name accordingly
        Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>) Class.forName(packageName + entityType);
        return genericEntityServiceImpl.getEntitiesByCriteria(clazz, criteria);
    }
}
