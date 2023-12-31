package com.hamrorestaurant.billing.web.rest.common;

import lombok.Data;

import java.util.List;

@Data
public class CommonResponse {

    private Object data;
    private List<Object> errors;
    private List<Object> warnings;
}
