package com.hamrorestaurant.billing.model;

import lombok.Data;

@Data
public class OrderedMenu {

    private String itemName;
    private int count;
}
