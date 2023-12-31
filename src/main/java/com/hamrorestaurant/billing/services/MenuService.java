package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;

import java.util.List;

public interface MenuService {


    CommonResponse getCustomerMenuItem(int tableNumber, List<MenuItem> menu);
}
