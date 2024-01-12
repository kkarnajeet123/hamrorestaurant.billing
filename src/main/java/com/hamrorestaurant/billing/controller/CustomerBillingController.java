package com.hamrorestaurant.billing.controller;

import com.hamrorestaurant.billing.model.OrderedFood;
import com.hamrorestaurant.billing.services.MenuServiceImpl;
import com.hamrorestaurant.billing.web.rest.common.CommonResponse;
import com.hamrorestaurant.billing.web.rest.common.ErrorResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.hamrorestaurant.billing.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerBillingController {

    @Autowired
    private MenuServiceImpl menuService;

    @ApiOperation(
            value = "Get all user information", notes = "Get All User Information", response = CommonResponse.class, tags = {"CustomerBilling",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request. Missing required parameters", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 200, message = "Array of Lines", response = CommonResponse.class)})
    @RequestMapping(value = "/billing/getBillAmount", produces = {"application/json"}, method = RequestMethod.POST)
    public CommonResponse getCustomerBilling(@RequestParam int tableNumber , @RequestBody List<OrderedFood> orderedFoods) {

        return menuService.getCustomerMenuItem(tableNumber, orderedFoods);
    }

    @ApiOperation(
            value = "Get all user information", notes = "Get All User Information", response = CommonResponse.class, tags = {"CustomerBilling",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request. Missing required parameters", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 200, message = "Array of Lines", response = CommonResponse.class)})
    @RequestMapping(value = "/billing/getTotalPriceForEachItem", produces = {"application/json"}, method = RequestMethod.POST)
    public CommonResponse getCostOfEachItem(@RequestParam int tableNumber,@RequestBody OrderedFood orderedFoods) {

        return menuService.getCostOfEachItem(tableNumber, orderedFoods);
    }

    @ApiOperation(
            value = "Get all user information", notes = "Get All User Information", response = CommonResponse.class, tags = {"CustomerBilling",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request. Missing required parameters", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 200, message = "Array of Lines", response = CommonResponse.class)})
    @RequestMapping(value = "/billing/getTotalBillAmount", produces = {"application/json"}, method = RequestMethod.POST)
    public CommonResponse getCostOfEachItem1(@RequestParam int tableNumber, @RequestBody OrderedFood orderedFoods) {

        return menuService.getTotalCostOfMenu(tableNumber, orderedFoods);
    }
}
