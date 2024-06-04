package com.example.adminservice.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.adminservice.Model.QueryInput;
import com.example.adminservice.Model.User;
import com.example.adminservice.Service.BaseEntity;
import com.example.adminservice.Service.EntityCrudService;
import com.example.adminservice.ServiceImpl.DynamicQueryBuilder;
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
    public ResponseEntity<?> saveEntity(@PathVariable String entityType, @RequestBody Object params) {
        try {
            entityCrudService.saveEntity(entityType, params);
            return responseService.successResponse("Entity saved successfully");
        } catch (Exception e) {
        	System.out.print(e.getMessage());
        	 return responseService.badRequest("Failed to save entity:" + e.getMessage());
        }
    }
    @GetMapping("/{entityType}/{id}")
    public ResponseEntity<?> getEntityById(@PathVariable String entityType, @PathVariable Long id) {
        try {
            BaseEntity entity = entityCrudService.getEntityById(entityType, id);
            return responseService.successResponse(entity);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return responseService.badRequest("Failed to retrieve entity: " + e.getMessage());
        }
    }
    @GetMapping("/{entityType}")
    public ResponseEntity<?> getAllEntities(@PathVariable String entityType) {
        try {
            List<?> entities = entityCrudService.getAllEntities(entityType);
            return responseService.successResponse(entities);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return responseService.badRequest("Failed to retrieve entities: " + e.getMessage());
        }
    }
    @PutMapping("/{entityType}/{id}")
    public ResponseEntity<?> upsertEntity(@PathVariable String entityType, @PathVariable Long id, @RequestBody Map<String, Object> params) {
        try {
            entityCrudService.upsertEntity(entityType, id, params);
            return responseService.successResponse("Entity upserted successfully");
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return responseService.badRequest("Failed to upsert entity: " + e.getMessage());
        }
    } 
    @GetMapping("/filter/{entityType}")
    public ResponseEntity<?> getEntitiesByCriteria(@PathVariable String entityType, @RequestParam Map<String, String> criteria) {
        try {
        	 List<? extends BaseEntity>entities = entityCrudService.getEntitiesByCriteria(entityType, criteria);
            return responseService.successResponse(entities);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return responseService.badRequest("Failed to retrieve entities: " + e.getMessage());
        }
    }
    @DeleteMapping("/{entityType}/all")
    public ResponseEntity<?> deleteAllEntities(@PathVariable String entityType) {
        try {
            entityCrudService.deleteAllEntities(entityType);
            return responseService.successResponse("All entities of type " + entityType + " deleted successfully");
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return responseService.badRequest("Failed to delete entities: " + e.getMessage());
        }
    }  
    @PostMapping("/build/oo/oo")
    public ResponseEntity<?> buildAndExecuteQuery(@RequestBody QueryInput input) {
        try {
            String queryString = DynamicQueryBuilder.buildQuery(input);
//            System.out.println(queryString  +"hi");
            List<?> result = entityCrudService.executeDynamicQuery(queryString, input.getFilters());
            return responseService.successResponse(result);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}