package com.fds.food.ordering.sys.services;

import com.fds.food.ordering.sys.models.FoodProduct;
import com.fds.food.ordering.sys.repositories.FoodProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodProductServiceImpl implements FoodProductService {
    @Autowired
    FoodProductRepository foodProductRepository;

    @Override
    public List<FoodProduct> findVendorsAllProducts(String vendorName) {
        return foodProductRepository.findVendorsAllProducts(vendorName);
    }

    @Override
    public List<FoodProduct> getAll() {
        return foodProductRepository.getAllProducts();
    }

    @Override
    public void saveFoodProduct(FoodProduct foodProduct) {
        foodProductRepository.save(foodProduct);
    }

    @Override
    public FoodProduct getFoodProductById(int id) {
        Optional<FoodProduct> optional =  foodProductRepository.findById(id);
        FoodProduct foodProduct = null;
        foodProduct = optional.get();
        if(optional.isPresent()){
            foodProduct = optional.get();
        }
        else{
            throw new RuntimeException("Food Product not found for id: "+id);
        }
        return foodProduct;
    }

    @Override
    public void updateFoodProduct(FoodProduct foodProduct) {
        foodProductRepository.save(foodProduct);
    }

    @Override
    public void deleteFoodProduct(Integer id) {
        foodProductRepository.deleteById(id);
    }
}
