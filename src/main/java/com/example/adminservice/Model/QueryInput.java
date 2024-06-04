package com.example.adminservice.Model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryInput {
	

	    public List<String> columns;
	    public String table;
	    public List<Condition> conditions;
	    public List<String> order_by;
	

public List<Condition> getFilters() {
    return this.conditions; // Assuming conditions represent filters
}
}