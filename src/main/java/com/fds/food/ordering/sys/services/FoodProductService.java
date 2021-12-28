package com.fds.food.ordering.sys.services;

import com.fds.food.ordering.sys.models.FoodProduct;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FoodProductService{
    List<FoodProduct> findVendorsAllProducts(String vendorName);
    List<FoodProduct> getAll();
    void saveFoodProduct(FoodProduct foodProduct);
    FoodProduct getFoodProductById(int id);
    void updateFoodProduct(FoodProduct foodProduct);
    void deleteFoodProduct(Integer id);
}
