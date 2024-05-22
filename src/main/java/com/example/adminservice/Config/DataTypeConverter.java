package com.example.adminservice.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTypeConverter {

    // Method to parse a string to LocalDateTime
    public static LocalDateTime parseToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    // Method to format LocalDateTime to a string
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    // Method to parse a string to boolean
    public static boolean parseToBoolean(String booleanString) {
        return Boolean.parseBoolean(booleanString);
    }

    // Method to convert a boolean to string representation
    public static String convertToString(boolean boolValue) {
        return Boolean.toString(boolValue);
    }

    // Example usage in Main class
    public static void main(String[] args) {
        // Parsing a string to LocalDateTime
        String dateString = "2024-05-23 12:30:00";
        LocalDateTime dateTime = DataTypeConverter.parseToLocalDateTime(dateString);
        System.out.println("Parsed LocalDateTime: " + dateTime);

        // Formatting LocalDateTime to a string
        String formattedDate = DataTypeConverter.formatLocalDateTime(dateTime);
        System.out.println("Formatted LocalDateTime: " + formattedDate);

        // Parsing a string to boolean
        String booleanString = "true";
        boolean boolValue = DataTypeConverter.parseToBoolean(booleanString);
        System.out.println("Parsed boolean: " + boolValue);

        // Converting a boolean to string representation
        String boolAsString = DataTypeConverter.convertToString(boolValue);
        System.out.println("Boolean as string: " + boolAsString);
    }
}
