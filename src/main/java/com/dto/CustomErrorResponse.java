package com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomErrorResponse(
        String error,
        Map<String, List<String>> fields
) {
}
