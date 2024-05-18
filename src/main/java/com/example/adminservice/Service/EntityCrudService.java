package com.example.adminservice.Service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface EntityCrudService {
	 void saveEntity(String entityType, Map<String, Object> params) throws Exception;
//	  ResponseEntity<?> getById(String entityType, Long id);
	  //<T extends BaseEntity>
}

