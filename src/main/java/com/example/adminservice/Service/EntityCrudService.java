package com.example.adminservice.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface EntityCrudService {
	 void saveEntity(String entityType, Object params) throws Exception;//
	 List<?> getAllEntities(String entityType) throws Exception;
	 BaseEntity getEntityById(String entityType, Long id) throws Exception;
	 void upsertEntity(String entityType, Long id, Map<String, Object> params) throws Exception;
	 List<? extends BaseEntity> getEntitiesByCriteria(String entityType, Map<String, String> criteria) throws Exception;
}

