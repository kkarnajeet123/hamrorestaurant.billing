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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    @Autowired
    private MenuRepository repo;

    @Override
    public CommonResponse getCustomerMenuItem(int tableNumber, List<OrderedFood> orderedFoods) {
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
        response.setData(calculatingTotalCost(calculatedTotalforEachItem));
        response.setTableNumber(tableNumber);
        // response.setTableNumber(orderedFoods.stream().map(m -> m.getTableNumber()).findAny().orElse(null));
        //response.getBillingCommonResponse().setData(commonResponse);
        return response;
    }

    @Override
    public CommonResponse getCostOfEachItem(int tableNumber, OrderedFood orderedFoods) {
        CommonResponse response = new CommonResponse();
        //checking if the requested menu name is available in db or not
        Map<String, Double> eachItemPriceMap = gettingEachItemPrice(getItemPriceFromDb(), orderedFoods.getMenu());
        //getting matched item count of order
        Map<String, Integer> eachMatchedItemCount = gettingEachMatchedItemCount(eachItemPriceMap, orderedFoods);
        //Map<String, Integer> eachItemCount= countOfEachOrder(orderedFoods);
        response.setData(getPricePerMenuItem(eachItemPriceMap, eachMatchedItemCount));
        response.setTableNumber(tableNumber);
        //  List<Double>eachPriceCostList= calculateTotalPriceOfEachItem(eachItemPriceMap, countOfEachOrder(orderedFoods));
        //   calculatingTotalCost(eachPriceCostList);
        logger.info("The billing response is: " + response.getData());
        return response;
    }

    @Override
    public CommonResponse getTotalCostOfMenu(int tableNumber, OrderedFood orderedFoods) {
        CommonResponse response = new CommonResponse();
        List<OrderedFood> orderFoodList= Arrays.asList(orderedFoods);
        Map<String, Integer> mapItemCount= getOrderItemCount(orderFoodList);
        Map<String, Double> mapItemAndPrice = gettingEachItemPrice(getItemPriceFromDb(),orderedFoods.getMenu());
        List<Double> costOfEachOrders =calculateTotalPriceOfEachItem(mapItemAndPrice, mapItemCount);
        Double totalCost=calculatingTotalCost(costOfEachOrders);
        response.setTableNumber(tableNumber);
        response.setData(totalCost);
        return response;
    }

    private Map<String, Integer> gettingEachMatchedItemCount(Map<String, Double> eachItemPriceMap, OrderedFood orderedFoods) {
        Map<String, Integer> eachMenuCost = new HashMap<>();

        for (OrderedMenu menu : orderedFoods.getMenu()) {
            if (eachItemPriceMap.containsKey(menu.getItemName())) {
                eachMenuCost.put(menu.getItemName(), menu.getCount());
            }

        }
        return eachMenuCost;
    }

    private Map<String, Integer> countOfEachOrder(OrderedFood orderedFoods) {
        Map<String, Integer> mapOfEachOrderCount = new
                HashMap<>();
        orderedFoods.getMenu().stream().filter(Objects::nonNull).forEach(f -> {
            mapOfEachOrderCount.put(f.getItemName(), f.getCount());
        });

        return mapOfEachOrderCount;
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
            itemPrice.put(menu.getItemName().toLowerCase(), menu.getPrice());
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


    private Map<String, Double> gettingEachItemPrice(Map<String, String> menuItemFromDb, List<OrderedMenu> orderedFoods) {
        Map<String, Double> matchedItemValue = new HashMap<>();
            orderedFoods.forEach(p -> {
                p.setItemName(p.getItemName().toLowerCase());
                if (menuItemFromDb.containsKey(p.getItemName())) {
                    matchedItemValue.put(p.getItemName(), Double.parseDouble(menuItemFromDb.get(p.getItemName())));
                }
            });
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
