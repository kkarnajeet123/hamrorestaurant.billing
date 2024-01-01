package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.model.OrderedFood;
import com.hamrorestaurant.billing.model.OrderedMenu;
import com.hamrorestaurant.billing.repository.MenuRepository;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.DataBindingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Autowired
    private MenuRepository repo;

    @Override
    public CommonResponse getCustomerMenuItem(List<OrderedFood> orderedFoods) {
        CommonResponse response = new CommonResponse();
        Map<String, String> itemPrice = new HashMap<>();
        // List<OrderedMenu> order = orderedFoods.get(0).getMenu();
        List<OrderedMenu> order = orderedFoods.stream().filter(Objects::nonNull).map(a -> a.getMenu()).findAny().orElse(null);
        List<Integer> eachItemCount = new ArrayList<>();
        orderedFoods.stream().filter(Objects::nonNull).forEach(f -> {
            f.getMenu().stream().filter(Objects::nonNull).forEach(l -> {
                eachItemCount.add(l.getCount());
            });
        });
        List<MenuItem> menuItem = repo.findAll();
        menuItem.forEach(menu -> {
            itemPrice.put(menu.getItemName(), menu.getPrice());
        });

        Map<String, Integer> orderItemCount = new HashMap<>();
        orderedFoods.forEach(n -> {
            n.getMenu().stream().filter(Objects::nonNull).forEach(m -> {
                orderItemCount.put(m.getItemName(), m.getCount());
            });
        });
        //getting item order price along with the item name
        Map<String, Double> mapItemAndPrice = gettingEachItemPrice(itemPrice, order);
        // calculating total
        //send the count and price to calculate count*price
        List<Double> calculatedTotalforEachItem = calculateTotalPriceOfEachItem(mapItemAndPrice, orderItemCount);
        //Calculating Total Bill Amount
//        calculatingTotalCost(calculatedTotalforEachItem);
        response.setData(calculatingTotalCost(calculatedTotalforEachItem));
        return response;
    }


    private Map<String, Double> gettingEachItemPrice(Map<String, String> itemWithTheirPrice, List<OrderedMenu> orderedFoods) {
        Map<String, Double> matchedItemValue = new HashMap<>();
        for (OrderedMenu key : orderedFoods) {
            orderedFoods.forEach(p -> {
                if (itemWithTheirPrice.containsKey(p.getItemName())) {
                    matchedItemValue.put(p.getItemName(), Double.parseDouble(itemWithTheirPrice.get(p.getItemName())));
                }
            });

        }
        return matchedItemValue;
    }

    private List<Double> calculateTotalPriceOfEachItem(Map<String, Double> orderedItemPrice, Map<String, Integer> orderItemCount) {
        List<Double> calculatedTotal = new ArrayList<>();
        orderItemCount.entrySet().stream().filter(Objects::nonNull).forEach(a -> {
            if (orderedItemPrice.containsKey(a.getKey())) {
                double price = orderedItemPrice.get(a.getKey());
                double total = price * a.getValue();
                calculatedTotal.add(total);
            }
        });
        return calculatedTotal;

    }

    private Double calculatingTotalCost(List<Double> costOfEachOrders) {
        Double total = 0.0;
        for (Double cost : costOfEachOrders) {
            total += cost;
        }
        return total;
    }
}
