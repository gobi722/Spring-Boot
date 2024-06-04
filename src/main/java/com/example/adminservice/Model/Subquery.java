package com.example.adminservice.Model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subquery {
    public List<String> columns;
    public String table;
    public List<Condition> conditions;
}