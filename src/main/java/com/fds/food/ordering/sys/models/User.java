package com.fds.food.ordering.sys.models;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true)
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    public User(String username, String password, boolean enabled, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public int getId() {
        return id;
    }



    @OneToMany(targetEntity = FoodProduct.class, cascade = CascadeType.ALL)
    @JoinTable(
            name = "vendors_products",
            joinColumns = @JoinColumn(
                    name = "vendor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id", referencedColumnName = "id"))
    private Collection<FoodProduct> foodProducts;


    public Set<FoodOrder> getFoodOrdersCustomer() {
        return foodOrdersCustomer;
    }

    public void addFoodOrderCustomer(FoodOrder foodOrder){
        this.foodOrdersCustomer.add(foodOrder);
    }

    public void setFoodOrdersCustomer(Set<FoodOrder> foodOrdersCustomer) {
        this.foodOrdersCustomer = foodOrdersCustomer;
    }

    @OneToMany(mappedBy = "vendor_id",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
    fetch = FetchType.EAGER)
    private Set<FoodOrder> foodOrders;  //only for vendor

    @OneToMany(mappedBy = "customer_id",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<FoodOrder> foodOrdersCustomer;  //only for custoeer


    @OneToMany(mappedBy = "user_id",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private Set<CartItem> cartItems ;

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
            this.cartItems.clear();
    }

    public void addToCart(CartItem cartItem){
        this.cartItems.add(cartItem);
    }

    public void setCartItems(Set<CartItem> cartItems){
        this.cartItems.clear();
        this.cartItems.addAll(cartItems);
    }

    public Set<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(Set<FoodOrder> foodOrders) {
        this.foodOrders.clear();
        this.foodOrders.addAll(foodOrders);
//        this.foodOrders = foodOrders;
    }

    public Collection<FoodProduct> getFoodProducts() {
        return foodProducts;
    }

    public void setFoodProducts(Collection<FoodProduct> foodProducts) {
        this.foodProducts = foodProducts;
    }

    public void deleteFoodProduct(FoodProduct foodProduct){
        this.foodProducts.remove(foodProduct);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}