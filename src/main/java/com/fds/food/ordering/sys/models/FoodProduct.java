package com.fds.food.ordering.sys.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "food_products")
@Data
@NoArgsConstructor
public class FoodProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private String vendor_name;

    public FoodProduct(String name, int price){
        this.name = name;
        this.price = price;
    }


}
