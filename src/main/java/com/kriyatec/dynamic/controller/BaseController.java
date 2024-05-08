package com.kriyatec.dynamic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kriyatec.dynamic.model.MyDocument;
import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
public class BaseController {

    @Autowired
    private MongoTemplate mongoTemplate;
    private DynamicModelGenerator dynamicgenerator;

    @PostMapping("/save")
    public ResponseEntity<Object> saveEntities(@RequestBody Map<String, Object> requestData) {
        String modelName = (String) requestData.get("modelName");
   
        try {
        	
        	 // Retrieve data from the specified collection in MongoDB
        	String collectionName = "data_model"; // Specify the collection name here
        	 Aggregation aggregation = Aggregation.newAggregation(
        			 Aggregation.match(Criteria.where("collection_name").is(modelName)),
                     Aggregation.group("$collection_name")
                         .push(new BasicDBObject("access_method", "$access_method")
                             .append("column_name", "$column_name")
                             .append("data_type", "$data_type")
                             .append("annotation", "$annotation"))
                             .as("fields")
             );

//             Aggregation aggregation = Aggregation.newAggregation(groupOperation);

             // Execute the aggregation pipeline
             List<Map> resultList = mongoTemplate.aggregate(aggregation, collectionName, Map.class).getMappedResults();  
             List<Map<String, Object>> typedResultList = new ArrayList<>();      	
             for (Map map : resultList) {
                 typedResultList.add(map);
             }
           
             String jsonData = "[\n" +
                     "  {\n" +
                     "    \"fields\": [\n" +
                     "      {\n" +
                     "        \"access_method\": \"private\",\n" +
                     "        \"column_name\": \"id\",\n" +
                     "        \"data_type\": \"String\"\n" +
                     "      },\n" +
                     "      {\n" +
                     "        \"access_method\": \"private\",\n" +
                     "        \"column_name\": \"name\",\n" +
                     "        \"data_type\": \"String\"\n" +
                     "      },\n" +
                     "      {\n" +
                     "        \"access_method\": \"private\",\n" +
                     "        \"column_name\": \"age\",\n" +
                     "        \"data_type\": \"int\"\n" +
                     "      },\n" +
                     "      {\n" +
                     "        \"access_method\": \"private\",\n" +
                     "        \"column_name\": \"Address\",\n" +
                     "        \"data_type\": \"object\",\n" +
                     "        \"nested_fields\": [\n" +
                     "          {\n" +
                     "            \"access_method\": \"private\",\n" +
                     "            \"column_name\": \"street\",\n" +
                     "            \"data_type\": \"String\"\n" +
                     "          },\n" +
                     "          {\n" +
                     "            \"access_method\": \"private\",\n" +
                     "            \"column_name\": \"city\",\n" +
                     "            \"data_type\": \"String\"\n" +
                     "          },\n" +
                     "          {\n" +
                     "            \"access_method\": \"private\",\n" +
                     "            \"column_name\": \"zip\",\n" +
                     "            \"data_type\": \"String\"\n" +
                     "          }\n" +
                     "        ]\n" +
                     "      },\n" +
                     "      {\n" +
                     "        \"access_method\": \"private\",\n" +
                     "        \"column_name\": \"Hobbies\",\n" +
                     "        \"data_type\": \"array\",\n" +
                     "        \"nested_fields\": [\n" +
                     "          {\n" +
                     "            \"access_method\": \"private\",\n" +
                     "            \"column_name\": \"name\",\n" +
                     "            \"data_type\": \"String\"\n" +
                     "          },\n" +
                     "          {\n" +
                     "            \"access_method\": \"private\",\n" +
                     "            \"column_name\": \"description\",\n" +
                     "            \"data_type\": \"String\"\n" +
                     "          }\n" +
                     "        ]\n" +
                     "      }\n" +
                     "    ]\n" +
                     "  }\n" +
                     "]";

             // Parse JSON string into List<Map<String, Object>>
             ObjectMapper mapper = new ObjectMapper();
             try {
                 List<Map<String, Object>> dataList = mapper.readValue(jsonData, List.class);
                 System.out.println(dataList);
                 // Call generateDynamicModelClass function with dataList
                 Class<?> entityClass = dynamicgenerator.generateDynamicModelClass(dataList, modelName);
//                 System.out.println(entityClass);
//                 // Print the name of the generated class
//                 System.out.println("Generated class name: " + entityClass.getName());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             
            // Dynamically generate a model class based on the structure of the documents
            // This step involves analyzing the structure of the documents and generating a Java class accordingly
//            Class<?> entityClass = dynamicgenerator.generateDynamicModelClass(typedResultList,modelName);
//        	
//            System.out.println(entityClass);
//            Object dataObject = requestData.get("data");
//            Object entityInstance = null;
//            Map<String, Object> errorMessage = new HashMap<>();
//            if (dataObject instanceof Map) {
//              // If requestData is a single map, convert it to a list containing that map
//            	  Map<String, Object> dataMap = (Map<String, Object>) dataObject;
//            	  if (!save(entityClass, dataMap, entityInstance)) {
//            		   errorMessage.put("message", "Request body format not supported");
//                       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//            	  }
//          } else if (dataObject instanceof List) {
//        	  List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataObject;
//        	  for (Map<String, Object> data : dataList) {
//        		  if (!save(entityClass, data, entityInstance)) {
//        			  errorMessage.put("message", "Extra/Missing Field");
//                      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        		  }
//              }          
//          } else {
//              // Handle unsupported requestData format
//            
//              errorMessage.put("message", "Request body format not supported");
//              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//          }   
//        	

            return ResponseEntity.ok().build();
        } catch ( IllegalArgumentException ex) {
            ex.printStackTrace();
            Map<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("message", "Failed to save entities: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    public boolean save(Class<?> entityClass, Map<String, Object> data, Object entityInstance) {
        try {
            entityInstance = entityClass.newInstance(); // Using default constructor
        } catch (InstantiationException | IllegalAccessException e) {
            // Handle instantiation exception
            e.printStackTrace();
            return false;
        }

        // Populate fields of the entity instance
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();

            // Use reflection to set field values
            try {
                Field field = entityClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(entityInstance, fieldValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Handle reflection exceptions
                e.printStackTrace();
                return false;
            }
        }

        try {
            mongoTemplate.save(entityInstance);
            return true; // Return true if the save operation was successful
        } catch (Exception e) {
            // Handle MongoDB save exception
            e.printStackTrace();
            return false;
        }
    }



}


//
// @PostMapping("/save")
//    public ResponseEntity<Object> saveEntities(@RequestParam("modelName") String modelName,@RequestBody Object requestData) {
//        if (requestData instanceof Map) {
//            // If requestData is a single map, convert it to a list containing that map
//            List<Map<String, Object>> dataList = new ArrayList<>();
//            dataList.add((Map<String, Object>) requestData);
//            return saveData(dataList,modelName);
//        } else if (requestData instanceof List) {
//            // If requestData is already a list, directly call saveData method
//            return saveData((List<Map<String, Object>>) requestData,modelName);
//        } else {
//            // Handle unsupported requestData format
//            Map<String, Object> errorMessage = new HashMap<>();
//            errorMessage.put("message", "Request body format not supported");
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//        }
//    }
//  








//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.kriyatec.dynamic.service.GenericService;
//
//@RestController
//@RequestMapping("/api")
//public class BaseController<T> {
//
//    @Autowired
//    private GenericService<T> genericService;
//
//    @PostMapping("/{collectionName}")
//    public ResponseEntity<T> create(@PathVariable String collectionName, @RequestBody T entity) {
//    	System.out.print(collectionName);
//        T createdEntity = genericService.create(collectionName, entity);
//        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{collectionName}/{id}")
//    public ResponseEntity<T> update(@PathVariable String collectionName, @PathVariable String id, @RequestBody T entity) {
//        T updatedEntity = genericService.update(collectionName, id, entity);
//        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
//    }
//
//    @GetMapping("/{collectionName}/{id}")
//    public ResponseEntity<T> getById(@PathVariable String collectionName, @PathVariable String id) {
//        T entity = genericService.getById(collectionName, id);
//        return new ResponseEntity<>(entity, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{collectionName}/{id}")
//    public ResponseEntity<Void> deleteById(@PathVariable String collectionName, @PathVariable String id) {
//        genericService.deleteById(collectionName, id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
