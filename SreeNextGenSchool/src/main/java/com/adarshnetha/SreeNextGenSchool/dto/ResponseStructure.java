package com.adarshnetha.SreeNextGenSchool.dto;

public record ResponseStructure<T>(
        String message,
        T data,
        int statusCode
) {
}
