package com.hamrorestaurant.billing.entity;

import lombok.Data;

import javax.persistence.*;

@Entity (name = "MenuItemTable")
@Table (name = "MenuItemTable")
public class MenuItem {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;
        private String itemName;
        private String description;
        private String category;
        private String price;

        public long getId() {
                return id;
        }

        public String getItemName() {
                return itemName;
        }

        public String getDescription() {
                return description;
        }

        public String getCategory() {
                return category;
        }

        public String getPrice() {
                return price;
        }
}
