package com.example.adminservice.Config;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLQueryBuilder {

    public String buildAggregationQuery(List<FilterCondition> inputData, String baseTableName, List<String> selectColumns) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ");

        if (selectColumns == null || selectColumns.isEmpty()) {
            queryBuilder.append("*");
        } else {
            queryBuilder.append(String.join(", ", selectColumns));
        }

        queryBuilder.append(" FROM ").append(baseTableName).append(" WHERE ");

        List<String> filterQueries = new ArrayList<>();

        for (FilterCondition filter : inputData) {
            String logicalOperator = filter.getClause().equalsIgnoreCase("OR") ? " OR " : " AND ";
            List<String> conditionQueries = filter.getConditions().stream()
                                                 .map(condition -> generateCondition(condition, baseTableName))
                                                 .collect(Collectors.toList());
            String combinedConditions = String.join(logicalOperator, conditionQueries);
            filterQueries.add("(" + combinedConditions + ")");
        }

        String finalQuery = queryBuilder.append(String.join(" AND ", filterQueries)).toString();
        return finalQuery;
    }

    private String generateCondition(ConditionGroup condition, String baseTableName) {
        String column = condition.getColumn();
        String operator = condition.getOperator();
        Object value = condition.getValue();

        // Map operator to SQL equivalents
        Map<String, String> operatorMap = new HashMap<>();
        operatorMap.put("EQUALS", "=");
        operatorMap.put("NOTEQUALS", "!=");
        operatorMap.put("CONTAINS", "LIKE");
        operatorMap.put("LESSTHAN", "<");
        operatorMap.put("GREATERTHAN", ">");
        operatorMap.put("LESSTHANOREQUAL", "<=");
        operatorMap.put("GREATERTHANOREQUAL", ">=");
        operatorMap.put("IN", "IN");
        // Add mappings for other operators as needed

        String sqlOperator = operatorMap.getOrDefault(operator, "=");

        if (baseTableName != null && !baseTableName.isEmpty() && condition.getParentCollectionName() != null && !condition.getParentCollectionName().isEmpty()) {
            column = condition.getParentCollectionName() + "." + column;
        }

        // Construct SQL condition based on operator and value
        String conditionString = column + " " + sqlOperator + " ";

        if (value instanceof String) {
            conditionString += "'" + value + "'";
        } else if (value instanceof ConditionGroup) {
            // Handle nested subqueries
            conditionString += "(" + buildSubquery((ConditionGroup) value) + ")";
        } else {
            conditionString += value;
        }

        return conditionString;
    }

    private String buildSubquery(ConditionGroup subqueryCondition) {
        // Assume subqueryCondition is structured properly to represent a subquery
        String subqueryBaseTable = subqueryCondition.getColumn();
        List<FilterCondition> subqueryFilters = new ArrayList<>();
        FilterCondition filterCondition = new FilterCondition();
        filterCondition.setClause(subqueryCondition.getClause());
        filterCondition.setConditions(subqueryCondition.getConditions());
        subqueryFilters.add(filterCondition);
        
        return buildAggregationQuery(subqueryFilters, subqueryBaseTable, null);
    }

    public static void main(String[] args) {
        // Sample data
        List<FilterCondition> inputData = new ArrayList<>();

        // First filter condition
        FilterCondition filter1 = new FilterCondition();
        filter1.setClause("OR");
        ConditionGroup condition1 = new ConditionGroup();
        condition1.setOperator("CONTAINS");
        condition1.setColumn("employee_id");
        condition1.setType("text");
        condition1.setValue("4");
        filter1.getConditions().add(condition1);

        ConditionGroup condition2 = new ConditionGroup();
        condition2.setOperator("EQUALS");
        condition2.setColumn("employee_id");
        condition2.setType("text");
        condition2.setValue("45");
        filter1.getConditions().add(condition2);

        inputData.add(filter1);

        // Second filter condition
        FilterCondition filter2 = new FilterCondition();
        filter2.setClause("AND");
        ConditionGroup condition3 = new ConditionGroup();
        condition3.setOperator("GREATERTHAN");
        condition3.setColumn("employee_name");
        condition3.setType("text");
        condition3.setValue("gobi");
        filter2.getConditions().add(condition3);

        ConditionGroup condition4 = new ConditionGroup();
        condition4.setOperator("NOTEQUALS");
        condition4.setColumn("employee_no");
        condition4.setType("number");
        condition4.setValue(45);
        filter2.getConditions().add(condition4);

        inputData.add(filter2);

        // Subquery condition
        FilterCondition filter3 = new FilterCondition();
        filter3.setClause("AND");
        ConditionGroup condition5 = new ConditionGroup();
        condition5.setOperator("IN");
        condition5.setColumn("ROLL_NO");
        condition5.setType("subquery");

        // Subquery details
        ConditionGroup subqueryCondition = new ConditionGroup();
        subqueryCondition.setColumn("STUDENT");
        subqueryCondition.setClause("AND");
        
        ConditionGroup subCondition1 = new ConditionGroup();
        subCondition1.setOperator("EQUALS");
        subCondition1.setColumn("SECTION");
        subCondition1.setType("text");
        subCondition1.setValue("A");
        subqueryCondition.getConditions().add(subCondition1);

        condition5.setValue(subqueryCondition);
        filter3.getConditions().add(condition5);

        inputData.add(filter3);

        // Base table name
        String baseTableName = "employees";
        // Columns to select
        List<String> selectColumns = List.of("NAME", "LOCATION", "PHONE_NUMBER");

        // Build query
        SQLQueryBuilder queryBuilder = new SQLQueryBuilder();
        String query = queryBuilder.buildAggregationQuery(inputData, baseTableName, selectColumns);

        // Output query
        System.out.println("Generated SQL query: " + query);
    }
}

class FilterCondition {
    private String clause;
    private List<ConditionGroup> conditions = new ArrayList<>();

    // Getters and setters
    public String getClause() { return clause; }
    public void setClause(String clause) { this.clause = clause; }
    public List<ConditionGroup> getConditions() { return conditions; }
    public void setConditions(List<ConditionGroup> conditions) { this.conditions = conditions; }
}

class ConditionGroup {
    private String operator;
    private String column;
    private String parentCollectionName;
    private String type;
    private Object value;
    private String clause;
    private List<ConditionGroup> conditions = new ArrayList<>();

    // Getters and setters
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    public String getColumn() { return column; }
    public void setColumn(String column) { this.column = column; }
    public String getParentCollectionName() { return parentCollectionName; }
    public void setParentCollectionName(String parentCollectionName) { this.parentCollectionName = parentCollectionName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    public String getClause() { return clause; }
    public void setClause(String clause) { this.clause = clause; }
    public List<ConditionGroup> getConditions() { return conditions; }
    public void setConditions(List<ConditionGroup> conditions) { this.conditions = conditions; }
}
