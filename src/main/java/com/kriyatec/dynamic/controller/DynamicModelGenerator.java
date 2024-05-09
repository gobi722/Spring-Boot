package com.kriyatec.dynamic.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DocumentationTool.Location;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class DynamicModelGenerator {

    public static StringBuilder generateDynamicModelClass(List<Map<String, Object>> dataList , String Model,StringBuilder classDefinition,String accessidentifier )   {
    	// Start by generating a class skeleton
//    	StringBuilder classDefinition = new StringBuilder();
	        // Append annotations to the class definition
	        classDefinition.append("@Document(collection = \""+Model+"\")\n");
	        classDefinition.append("@Validated\n");
	        classDefinition.append(accessidentifier+" class ").append(Model).append(" {\n");
	      
//	        System.out.println(classDefinition);
    	
    	// Maintain a set of seen fields to avoid duplication
//    	Set<String> seenFields = new HashSet<>();
//    	List<Map<String, Object>> dataList1 = new ArrayList<>();
//
//    	for (Map<String, Object> data : dataList) {
//    	    // Extract the "fields" value from each map in dataList
//    	    List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) data.get("fields");
//    	    
//    	    // Add the extracted "fields" list to dataList1
//    	    dataList1.addAll(fieldsList);
//    	}
//    			
   	generateFields(dataList, classDefinition,Model);


    	// Close the class definition
    	classDefinition.append("}");
   return classDefinition;
//    	try {
//            // Use reflection to define the generated class
////            return Class.forName("com.kriyatec.dynamic.model." + Model);
//        } catch (ClassNotFoundException e) {
//            // Define the class if it doesn't already exist
//            try {
//            	return null;
////                return createClass(classDefinition.toString(), Model);//"com.kriyatec.dynamic.model." + 
//            } catch (Exception ex) {
//                // Handle the exception appropriately, e.g., log it or rethrow a RuntimeException
//                ex.printStackTrace();
//                throw new RuntimeException("Failed to create dynamic class", ex);
//            }
//        }
    }

    private static String determineFieldType(Object value) {
        // Determine the field type based on the value type
        if (value instanceof String) {
            return "String";
        } else if (value instanceof Integer) {
            return "int";
        } else if (value instanceof Long) {
            return "long";
        } else if (value instanceof Double) {
            return "double";
        } else if (value instanceof Boolean) {
            return "boolean";
        } else {
            return "Object";
        }
    }


//    private static Class<?> defineClass(String className, String classDefinition) {
//        // Define the class using Java's built-in class loader
//        try {
//            return Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            byte[] classBytes = classDefinition.getBytes();
//            return new DynamicClassLoader().defineDynamicClass(className, classBytes, 0, classBytes.length, null);
//        }
//    }

 //////////////////////////////////////////////////////////////////   

    
//    private static class DynamicClassLoader extends ClassLoader {
//        public Class<?> defineDynamicClass(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) {
//            return super.defineClass(name, b, off, len, protectionDomain);
//        }
//    }
    
//    private static Class<?> defineClass(String className, String classDefinition) {
//        // Define the class using Java's built-in class loader
//        try {
//            return Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            byte[] classBytes = classDefinition.getBytes();
//            return new DynamicClassLoader().defineDynamicClass(className, classBytes, 0, classBytes.length, null);
//        }
   
    
    //////////////////////////////////////////////////////////////////////////
    
//    private static class DynamicClassLoader extends ClassLoader {
//        public Class<?> defineDynamicClass(String name, byte[] b, int off, int len, ProtectionDomain protectionDomain) {
//            return super.defineClass(name, b, off, len, protectionDomain);
//        }
//    }
	public static Class<?> createClass(String classDefinition, String className) throws Exception {
	    // Load the Java Compiler
	    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	    if (compiler == null) {
	        throw new RuntimeException("Java compiler is not available. Make sure you're running on a JDK.");
	    }

	    // Create a new instance of JavaFileObject from the provided class definition
	    JavaFileObject javaFileObject = createDynamicJavaFileObject(className, classDefinition);

	    // Compile the dynamic class
	    CompilationTask task = compiler.getTask(null, null, null, null, null, Arrays.asList(javaFileObject));
	    boolean success = task.call();
	    if (!success) {
	        throw new RuntimeException("Compilation failed");
	    }

	    // Load the compiled class dynamically
	    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("").toURI().toURL()});
	    return classLoader.loadClass(className);
	}

	public static JavaFileObject createDynamicJavaFileObject(String name, String sourceCode) {
	    return new SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE) {
	        private final String source = sourceCode;

	        @Override
	        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
	            return source;
	        }
	    };
	}
//	// A method to generate fields recursively for nested objects and arrays
	private static void generateFields(List<Map<String, Object>> dataList, StringBuilder classDefinition,String Model) {
		
	    for (Map<String, Object> field : dataList) {
//	        List<Map<String, Object>> fieldsList = (List<Map<String, Object>>) data.get("fields");
//	        if (fieldsList == null) {
//	            // Handle the case where fieldsList is null (optional)
//	            continue; // Skip processing this data entry
//	        }
//	        for (Map<String, Object> field : fieldsList) {
	    	 
	            String accessMethod = (String) field.get("access_method");
	            String fieldName = (String) field.get("column_name");
	            String fieldType = (String) field.get("data_type");
	            String fieldAnnonations = (String) field.get("annonations");
	            if (fieldAnnonations != null) {
          		  System.out.println(fieldAnnonations);
		                   classDefinition.append(fieldAnnonations).append("\n");
  		        }else {
  		        	  classDefinition.append("\n");
  		        }
	            // If the fieldType is an object or an array, handle it recursively
	           
	            if (fieldType.equals("object")) {
	                // Generate the class definition for the nested object
	                classDefinition.append(accessMethod).append(" ");
	                String convertedString = convertToLowerCaseFirstLetter(fieldName);
	                classDefinition.append(convertedString);
	               
	                classDefinition.append(" ").append(fieldName).append(";\n");
	            } else if (fieldType.equals("array")) {
	                // Generate the class definition for the array of objects
	            	
	                classDefinition.append(accessMethod).append(" ");
	                classDefinition.append("List<");
//	                System.out.println(generateDynamicModelClass((List<Map<String, Object>>) field.get("nested_fields"), fieldName).getName());
	                String convertedString = convertToLowerCaseFirstLetter(fieldName);
	                classDefinition.append(convertedString);
	                
	                classDefinition.append("> ").append(fieldName).append(";\n");
	            } else {
	                // Regular field
	            	 
	                classDefinition.append(accessMethod).append(" ").append(fieldType).append(" ").append(fieldName).append(";\n");
	            }
	        }
	    } 
//	}

	 public static String convertToLowerCaseFirstLetter(String original) {
	        if (original == null || original.isEmpty()) {
	            return original;
	        }
	        return Character.toUpperCase(original.charAt(0)) + original.substring(1);
	    }
}
   

    

