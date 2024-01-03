package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.model.OrderedFood;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;

import java.util.List;

public interface MenuService {


    CommonResponse getCustomerMenuItem(List<OrderedFood> orderedFoods);
    CommonResponse getCostOfEachItem(List<OrderedFood> orderedFoods);
}
