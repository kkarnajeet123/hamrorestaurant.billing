package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.model.OrderedFood;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;

import java.util.List;

public interface MenuService {


    CommonResponse getCustomerMenuItem(int tableNumber, List<OrderedFood> orderedFoods);

    CommonResponse getCostOfEachItem(int tableNumber,OrderedFood orderedFoods);

    CommonResponse getTotalCostOfMenu(int tableNumber, OrderedFood orderedFoods);
}
