package com.fds.food.ordering.sys.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue
    private int id;

    private int productId;
    private String product_name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_id;
    private int quantity=1;
    private double price;
    private String vendor_name;


    public CartItem setProductId(int productId) {
        this.productId = productId;
        return this;
    }





    public CartItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }


    public CartItem setPrice(double price) {
        this.price = price;
        return this;
    }
}
