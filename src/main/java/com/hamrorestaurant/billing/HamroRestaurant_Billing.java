package com.hamrorestaurant.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class HamroRestaurant_Billing
{
    public static void main( String[] args )
    {
        SpringApplication.run(HamroRestaurant_Billing.class,args);
    }
}
