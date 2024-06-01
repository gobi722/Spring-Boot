package com.example.adminservice.Config;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class DynamicQueryBuilder {
	 private static final ObjectMapper mapper = new ObjectMapper();
	  private static final Map<String, String> operatorMap = new HashMap<>();
	  static {
	        operatorMap.put("EQUALS", "=");
	        operatorMap.put("NOTEQUALS", "!=");
	        operatorMap.put("CONTAINS", "LIKE");
	        operatorMap.put("LESSTHAN", "<");
	        operatorMap.put("GREATERTHAN", ">");
	        operatorMap.put("LESSTHANOREQUAL", "<=");
	        operatorMap.put("GREATERTHANOREQUAL", ">=");
	        operatorMap.put("IN", "IN");
	        // Add more mappings as needed
	    }
	  public static String buildQuery(QueryInput input) {
	        StringBuilder query = new StringBuilder("SELECT ");
    
	        if (input.columns == null || input.columns.isEmpty()) {
	        	query.append("*");
	        } else {
	        	 query.append(String.join(", ", input.columns));
	        }
	
	       
	        query.append(" FROM ").append(input.table);

	        // Add conditions
	        if (input.conditions != null && !input.conditions.isEmpty()) {
	            query.append(" WHERE ");
	            for (int i = 0; i < input.conditions.size(); i++) {
	                Condition condition = input.conditions.get(i);

	                // Replace operator with SQL equivalent
	                String sqlOperator = operatorMap.getOrDefault(condition.operator, condition.operator);

	                query.append(condition.column).append(" ").append(sqlOperator).append(" ").append(formatValue(condition.value));
	                if (i < input.conditions.size() - 1) {
	                    query.append(" AND ");
	                }
	            }
	        }

	        // Add order by
	        if (input.order_by != null && !input.order_by.isEmpty()) {
	            query.append(" ORDER BY ").append(String.join(", ", input.order_by));
	        }

	        return query.toString();
	    }


	  private static String formatValue(Object value) {
//		  System.out.println(value);
		    if (value instanceof String) {
//		    	 System.out.println(value + "o");
		    	 return "'" + value + "'";
		    } else if (value instanceof LinkedHashMap) {
		        @SuppressWarnings("unchecked")
		        LinkedHashMap<String, Object> subqueryMap = (LinkedHashMap<String, Object>) value;
		        
		        Subquery subquery = new Subquery();
		        subquery.columns = (List<String>) subqueryMap.get("columns");
		        subquery.table = (String) subqueryMap.get("table");
		        subquery.conditions = new ArrayList<>();
		        
		        List<LinkedHashMap<String, Object>> conditionsList = (List<LinkedHashMap<String, Object>>) subqueryMap.get("conditions");
		        if (conditionsList != null) {
		            for (LinkedHashMap<String, Object> conditionMap : conditionsList) {
		                Condition condition = new Condition();
		                condition.column = (String) conditionMap.get("column");
		                condition.operator = (String) conditionMap.get("operator");
		                condition.value = conditionMap.get("value");
		                subquery.conditions.add(condition);
		            }
		        }

		        StringBuilder subqueryStr = new StringBuilder("(");
		        subqueryStr.append("SELECT ");
		        subqueryStr.append(String.join(", ", subquery.columns));
		        subqueryStr.append(" FROM ").append(subquery.table);

		        if (subquery.conditions != null && !subquery.conditions.isEmpty()) {
		            subqueryStr.append(" WHERE ");
		            for (int i = 0; i < subquery.conditions.size(); i++) {
		                Condition condition = subquery.conditions.get(i);
		                String sqlOperator = operatorMap.getOrDefault(condition.operator, condition.operator);
		                subqueryStr.append(condition.column).append(" ").append(sqlOperator).append(" ").append(formatValue(condition.value));
		                if (i < subquery.conditions.size() - 1) {
		                    subqueryStr.append(" AND ");
		                }
		            }
		        }
		        subqueryStr.append(")");
		        return subqueryStr.toString();
		    } else if (value instanceof Integer || value instanceof Double) {
		        return value.toString();
		    }
		    throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
		}


}