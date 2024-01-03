package com.hamrorestaurant.billing.services;

import com.hamrorestaurant.billing.entity.MenuItem;
import com.hamrorestaurant.billing.model.BillingCommonResponse;
import com.hamrorestaurant.billing.model.OrderedFood;
import com.hamrorestaurant.billing.model.OrderedMenu;
import com.hamrorestaurant.billing.repository.MenuRepository;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Autowired
    private MenuRepository repo;

    @Override
    public CommonResponse getCustomerMenuItem(List<OrderedFood> orderedFoods) {
        CommonResponse response = new CommonResponse();
        Map<String, String> itemPrice = new HashMap<>();
        // Getting order item list
        List<OrderedMenu> order = orderedFoods.stream().filter(Objects::nonNull).map(a -> a.getMenu()).findAny().orElse(null);

        //getting item order price along with the item name
        Map<String, Double> mapItemAndPrice = gettingEachItemPrice(getItemPriceFromDb(), order);
        // calculating total
        //send the count and price to calculate count*price
        List<Double> calculatedTotalforEachItem = calculateTotalPriceOfEachItem(mapItemAndPrice, getOrderItemCount(orderedFoods));
        //Calculating Total Bill Amount
//        calculatingTotalCost(calculatedTotalforEachItem);
        BillingCommonResponse commonResponse = new BillingCommonResponse();
        response.setData(calculatingTotalCost(calculatedTotalforEachItem));
        response.setTableNumber(orderedFoods.stream().map(m->m.getTableNumber()).findAny().orElse(null));
        //response.getBillingCommonResponse().setData(commonResponse);
        return response;
    }

    private List<Integer> getEachItemCount(List<OrderedFood> orderedFoods) {
        List<Integer> eachItemCount = new ArrayList<>();
        orderedFoods.stream().filter(Objects::nonNull).forEach(f -> {
            f.getMenu().stream().filter(Objects::nonNull).forEach(l -> {
                eachItemCount.add(l.getCount());
            });
        });
        return eachItemCount;
    }

    private Map<String, String> getItemPriceFromDb() {
        Map<String, String> itemPrice = new HashMap<>();
        List<MenuItem> menuItem = repo.findAll();
        menuItem.forEach(menu -> {
            itemPrice.put(menu.getItemName(), menu.getPrice());
        });
        return itemPrice;
    }

    private Map<String, Integer> getOrderItemCount(List<OrderedFood> orderedFoods) {
        Map<String, Integer> orderItemCount = new HashMap<>();
        orderedFoods.forEach(n -> {
            n.getMenu().stream().filter(Objects::nonNull).forEach(m -> {
                orderItemCount.put(m.getItemName(), m.getCount());
            });
        });
        return orderItemCount;
    }

    @Override
    public CommonResponse getCostOfEachItem(List<OrderedFood> orderedFoods) {
        CommonResponse response = new CommonResponse();
        List<OrderedMenu> order = orderedFoods.stream().filter(Objects::nonNull).map(a -> a.getMenu()).findAny().orElse(null);
        Map<String, Double> mapItemAndPrice = gettingEachItemPrice(getItemPriceFromDb(), order);
        response.setData(getPricePerMenuItem(mapItemAndPrice, getOrderItemCount(orderedFoods)));
        response.setTableNumber(orderedFoods.stream().filter(Objects::nonNull).map(a->a.getTableNumber()).findAny().orElse(null));
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

    private Map<String, Double> getPricePerMenuItem(Map<String, Double> orderedItemPrice, Map<String, Integer> orderItemCount) {
        Map<String, Double> totalPriceOfEachItem = new HashMap<>();
        orderItemCount.entrySet().stream().filter(Objects::nonNull).forEach(a -> {
            if (orderedItemPrice.containsKey(a.getKey())) {
                double price = orderedItemPrice.get(a.getKey());
                double total = price * a.getValue();
                totalPriceOfEachItem.put(a.getKey(), total);
            }
        });
        return totalPriceOfEachItem;
    }

    private Double calculatingTotalCost(List<Double> costOfEachOrders) {
        Double total = 0.0;
        for (Double cost : costOfEachOrders) {
            total += cost;
        }
        return total;
    }
}
