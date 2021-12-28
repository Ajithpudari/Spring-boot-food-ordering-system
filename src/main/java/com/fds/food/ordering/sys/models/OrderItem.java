package com.fds.food.ordering.sys.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue
    private int id;
    private int productId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private FoodOrder order_id;
    private String product_name;
    private int quantity;
    private double price;


}
