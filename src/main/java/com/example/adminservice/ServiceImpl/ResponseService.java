package com.example.adminservice.ServiceImpl;

import com.example.adminservice.Model.ErrorResponse;
import com.example.adminservice.Model.Success;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseService {

    public static ResponseEntity<?> entityNotFound(String message) {
        ErrorResponse error = new ErrorResponse(404, "entity-not-found", message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(error);
    }

    public static ResponseEntity<?> badRequest(String message) {
        ErrorResponse error = new ErrorResponse(400, "bad-request", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(error);
    }

    public static ResponseEntity<?> unexpected(String message) {
        ErrorResponse error = new ErrorResponse(500, "internal-server", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(error);
    }

    public static ResponseEntity<?> successResponse(Object data) {
        Success success = new Success(200, data, "");
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(success);
    }
}
