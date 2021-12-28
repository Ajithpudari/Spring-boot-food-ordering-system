package com.fds.food.ordering.sys.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "food_orders")
@Getter
@Setter
@NoArgsConstructor
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer_id;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private User vendor_id;

    private String vendor_name;

    private double totalPrice;
    private boolean status;
    private String address;
    private Timestamp placedAt;



    @OneToMany(mappedBy = "order_id",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Collection<OrderItem> orderItems;

    public FoodOrder setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
        return this;
    }

    public FoodOrder setCustomer_id(User customer_id) {
        this.customer_id = customer_id;
        return this;
    }

    public FoodOrder setVendor_id(User vendor_id) {
        this.vendor_id = vendor_id;
        return this;
    }

    public FoodOrder setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public FoodOrder setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public FoodOrder setAddress(String address) {
        this.address = address;
        return this;
    }

    public FoodOrder setPlacedAt(Timestamp placedAt) {
        this.placedAt = placedAt;
        return this;
    }

    public FoodOrder setOrderItems(Collection<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }
}
