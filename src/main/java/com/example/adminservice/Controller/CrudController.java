package com.example.adminservice.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.adminservice.Model.User;
import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.EntityCrudService;
import com.example.adminservice.ServiceImpl.ResponseService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/entities")
public class CrudController {
	@Autowired
	EntityCrudService entityCrudService;
	ResponseService responseService;
    @GetMapping("/resource")
    public ResponseEntity<?> getResource() {
        return ResponseEntity.ok("Accessed protected resource");
    }
    

    @PostMapping("/{entityType}")
    public ResponseEntity<?> saveEntity(@PathVariable String entityType, @RequestBody Map<String, Object> params) {
        try {
            entityCrudService.saveEntity(entityType, params);
            return responseService.successResponse("Entity saved successfully");
        } catch (Exception e) {
        	System.out.print(e.getMessage());
        	 return responseService.badRequest("Failed to save entity:" + e.getMessage());
        }
    }
//        @GetMapping("/{entityType}/{id}")
//        public ResponseEntity<?> getEntityById(@PathVariable String entityType, @PathVariable Long id) {
//            try {
//                ResponseEntity<?> entityResponse = entityCrudService.getById(entityType, id);
//                
//                if (entityResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
//                	 return responseService.badRequest("Entity not found");
//                }
//                
//                if (!entityResponse.getStatusCode().is2xxSuccessful()) {
//                	 return responseService.badRequest("Error occurred: " + entityResponse.getBody());
//                }
//                
//                return entityResponse; // Return the response received from getById method directly
//            } catch (ClassNotFoundException e) {
//            	 return responseService.badRequest("Class not found: " + e.getMessage());
//            } catch (Exception e) {
//            	 return responseService.badRequest("Error occurred: " + e.getMessage());
//            }
//        }


}