package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.repository.MenuRepository;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository repo;
    @Override
    public CommonResponse getCustomerMenuItem(int tableNumber, List<MenuItem> menu) {
        return null;
    }
}
