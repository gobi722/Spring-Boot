package com.example.adminservice.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.EntityCrudService;
import com.example.adminservice.Service.GenericCrudService;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class EntityCrudServiceImpl implements EntityCrudService {

    @Autowired
    private GenericEntityServiceImpl genericEntityServiceImpl;

    public void saveEntity(String entityType, Map<String, Object> params) throws Exception {
        String packageName = "com.example.adminservice.Model."; // Adjust the package name accordingly
        Class<?> clazz = Class.forName(packageName + entityType);
        BaseEntity entity = (BaseEntity) clazz.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, fieldValue);
        }

        genericEntityServiceImpl.saveEntity(entity);
    }
}
