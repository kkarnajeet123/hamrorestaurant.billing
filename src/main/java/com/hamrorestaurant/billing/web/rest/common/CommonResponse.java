package com.hamrorestaurant.billing.web.rest.common;

import com.hamrorestaurant.billing.model.BillingCommonResponse;
import lombok.Data;

import java.util.List;

@Data
public class CommonResponse {

    private int tableNumber;
    private Object data;
    private List<Object> errors;
    private List<Object> warnings;
}
