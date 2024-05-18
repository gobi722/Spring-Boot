package com.example.adminservice.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Success {
    private int status;
    private Object data;
    private String errorMsg;

    public Success(int status, Object data, String errorMsg) {
        this.status = status;
        this.data = data;
        this.errorMsg = errorMsg;
    }
    @JsonProperty("status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    @JsonProperty("message")
    public String getMessage() {
        return errorMsg;
    }
	
}
