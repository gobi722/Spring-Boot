package com.kriyatec.dynamic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;

import com.kriyatec.dynamic.model.MyDocument;
import com.mongodb.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
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
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
@Validated
@RestController
public class BaseController {
	
    @Autowired
    private MongoTemplate mongoTemplate;
    private DynamicModelGenerator dynamicgenerator;

   

    @PostMapping("/save")
    public ResponseEntity<Object> saveEntities(@RequestBody Map<String, Object> requestData,@RequestParam("modelName")  String  modelName) {
      
        Class<?> entityClass =null;
        try {
        	
        	 // Retrieve data from the specified collection in MongoDB
        	String collectionName = "data_model"; // Specify the collection name here
        	 Aggregation aggregation = Aggregation.newAggregation(
        			 Aggregation.match(Criteria.where("collection_name").is(modelName)), 		
                     Aggregation.group("$model_name")
                     .first("collection_name").as("collection")
                     .addToSet("import").as("import")
                         .push(new BasicDBObject("access_method", "$access_method")
                             .append("column_name", "$column_name")
                             .append("data_type", "$data_type")
                             .append("annonations", "$annotations"))
                             .as("fields")
                                
             );
             // Execute the aggregation pipeline
             List<Map> resultList =  mongoTemplate.aggregate(aggregation, collectionName, Map.class).getMappedResults();  
	 
             List<Map<String, Object>> typedResultList = new ArrayList<>();      	
             for (Map map : resultList) {
                 typedResultList.add(map);
             }
           
           
             
             StringBuilder classDefinition = new StringBuilder();
             StringBuilder classDefinition1 = new StringBuilder();
             StringBuilder classDefinition2 = new StringBuilder();

      	   classDefinition.append("import org.springframework.data.mongodb.core.mapping.Document;\n"); // Import necessary annotations
  	       classDefinition.append("import org.springframework.validation.annotation.Validated;\n"); // Import Validated annotation
  	       classDefinition.append("import com.fasterxml.jackson.annotation.JsonProperty;\n");
  	       classDefinition.append("import javax.validation.constraints.NotBlank;\n");
  	       classDefinition.append("import org.springframework.lang.NonNull;\n");
  	       classDefinition.append("import org.springframework.data.annotation.TypeAlias;\n");
  	       classDefinition.append("import javax.validation.constraints.NotNull;\n");
  	       classDefinition.append("import java.util.List;\n");
  	        
             for (Map<String, Object> data : typedResultList) {
     	        List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) data.get("fields");
     	       List<Map<String, Object>> importList = (List<Map<String, Object>>) data.get("import");
//     	     for (Map<String, Object> importMap : importList) {
//    	      System.out.println(importMap);
////     	     classDefinition.append("import"+" "+importdata+";\n");
//            }
     	    
     	        String id =(String) data.get("_id");    
     	      
     	       String  Access= GetACessName( id, modelName,classDefinition1);
     	     // Call generateDynamicModelClass function with dataList 
     	     // Dynamically generate a model class based on the structure of the documents
             // This step involves analyzing the structure of the documents and generating a Java class accordingly
     	      classDefinition1 = dynamicgenerator.generateDynamicModelClass(fieldsList, id,classDefinition1,Access);
     	       	
             }
             classDefinition2 =   classDefinition.append(classDefinition1);
//             System.out.println(classDefinition);

             try {
             	 entityClass =dynamicgenerator.createClass(classDefinition2.toString(), modelName);
             
             	} catch (Exception e) {
                    e.printStackTrace();
                }
     
            
   	        
            Object dataObject = requestData;
        
            Object entityInstance = null;
            Map<String, Object> errorMessage = new HashMap<>();
            if (dataObject instanceof Map) {
              // If requestData is a single map, convert it to a list containing that map
            	  Map<String, Object> dataMap = (Map<String, Object>) dataObject;
        	  try {
				if (!save(entityClass, dataMap, entityInstance)) {
						  errorMessage.put("message", "Extra/Missing Field");
				           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
					  }
			} catch (JsonProcessingException e) {
				errorMessage.put("message", "Extra/Missing Field");
		           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			} catch (NoSuchFieldException e) {
				errorMessage.put("message", "Extra/Missing Field");
		           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				
			} catch (SecurityException e) {
				errorMessage.put("message", "Extra/Missing Field");
		           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
					}  catch (BindException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          } else if (dataObject instanceof List) {
        	  List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataObject;
        	  for (Map<String, Object> data : dataList) {
        		  try {
					if (!save(entityClass, data, entityInstance)) {
						  errorMessage.put("message", "Extra/Missing Field");
					      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
					  }
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (BindException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }          
          } else {
              // Handle unsupported requestData format          
              errorMessage.put("message", "Request body format not supported");
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
          }   
            return ResponseEntity.ok().build();
        } catch ( IllegalArgumentException ex) {
            ex.printStackTrace();
            Map<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("message", "Failed to save entities: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    public boolean save(Class<?> entityClass, Map<String, Object> data, Object entityInstance) throws NoSuchFieldException, SecurityException, JsonProcessingException, BindException {
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
            System.out.println("Class of field value: " + fieldValue.getClass());       
                // Handle nested object
                try {
                	 if (fieldValue instanceof LinkedHashMap) {
                	
                    Field field = entityClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    // Convert LinkedHashMap to JSON string
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(fieldValue);
                    // Deserialize JSON string to object of field type
                    Object nestedObject = objectMapper.readValue(json, field.getType());
                    field.set(entityInstance, nestedObject);
//                	 } else if(fieldValue instanceof ArrayList){
//                		 System.out.println( entityInstance);
//                		 
                	 }else {
                		  Field field = entityClass.getDeclaredField(fieldName);
                          field.setAccessible(true);
                          field.set(entityInstance, fieldValue);
                	         }
                   } catch (IllegalAccessException e) {
                     // Handle instantiation exception
                     e.printStackTrace();
                     return false;
                     }           
        }
       
        try {
            mongoTemplate.save(entityInstance);
            return true; // Return true if the save operation was successful
        } catch (MongoException e) {
            // Handle MongoDB save exception
            e.printStackTrace();
            return false;
        }
    }
    public String GetACessName(String id,String modelName, StringBuilder classDefinition) {
   	 if (id.equals(modelName)) {
   		 // Append annotations to the class definition
   	        classDefinition.append("@Document(collection = \""+modelName+"\")\n");
   	     classDefinition.append("@TypeAlias(\"\")\n");
             return "public";
         } else {
            return  "";
         }
   }
   public static <T> List<T> jsonArrayToList(String json, Class<T> elementClass) throws IOException {
       ObjectMapper objectMapper = new ObjectMapper();
       CollectionType listType = 
         objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
       return objectMapper.readValue(json, listType);
   }

//    public boolean save(Class<?> entityClass, Map<String, Object> data, Object entityInstance) {
//        try {
//            entityInstance = entityClass.newInstance(); // Using default constructor
//        } catch (InstantiationException | IllegalAccessException e) {
//            // Handle instantiation exception
//            e.printStackTrace();
//            return false;
//        }
//
//        // Populate fields of the entity instance
//        for (Map.Entry<String, Object> entry : data.entrySet()) {
//            String fieldName = entry.getKey();
//            Object fieldValue = entry.getValue();
//            System.out.println("Class of address field: " + fieldValue.getClass());
//            if (fieldValue.getClass().equals(java.util.LinkedHashMap.class)) {
//
//            	 System.out.println(entry);
//                 List<?> bookList;
//				try {
//					bookList = jsonArrayToList(fieldValue.toString(), List.class);
//					System.out.println(bookList);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}                		 
//            }         
//            // Use reflection to set field values
//            try {
//                Field field = entityClass.getDeclaredField(fieldName);
//                field.setAccessible(true);
//              
//                field.set(entityInstance, fieldValue);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                // Handle reflection exceptions
//                e.printStackTrace();
//                return false;
//            }
//        }
//        try {
//            mongoTemplate.save(entityInstance);
//            return true; // Return true if the save operation was successful
//        } catch (Exception e) {
//            // Handle MongoDB save exception
//            e.printStackTrace();
//            return false;
//        }
//    }

}
//        Object entityInstance;
//
//        // Get the inner class mymodel from the provided entityClass
//        Class<?> innerClass = Arrays.stream(entityClass.getDeclaredClasses())
//                                     .filter(c -> c.getSimpleName()
//                                    		 .equals("mymodel"))
//                                     .findFirst()
//                                     .orElse(null);
////        Stream<Class<?>> a = Arrays.stream(entityClass.getDeclaredClasses()).filter(c -> c.getSimpleName());
//
//        if (innerClass == null) {
//            System.err.println("Inner class mymodel not found in the provided entityClass.");
//            return false;
//        }
////        System.out.println(a);
//        try {
//            entityInstance = innerClass.newInstance(); // Using default constructor
//        } catch (InstantiationException | IllegalAccessException e) {
//            // Handle instantiation exception
//            e.printStackTrace();
//            return false;
//        }
