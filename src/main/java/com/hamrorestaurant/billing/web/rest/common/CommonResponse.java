package com.hamrorestaurant.billing.web.rest.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonResponse {

    private int tableNumber;
    private Object data;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Object> errors;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<Object> warnings;
}
