package com.hamrorestaurant.billing.web.rest.common;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private static final long serialVersionUID=1L;

    private String code;
    private String message;
    private List<String> fields;

}
