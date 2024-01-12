package com.hamrorestaurant.billing.model;

import lombok.Data;

import java.util.List;
@Data
public class OrderedFood {

    //private int tableNumber;
    private List<OrderedMenu> menu;
}
