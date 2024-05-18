package com.example.adminservice.Service;

public interface GenericCrudService {
    <T extends BaseEntity> void saveEntity(T entity);

	
}
