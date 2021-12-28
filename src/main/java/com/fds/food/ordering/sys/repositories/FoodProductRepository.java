package com.fds.food.ordering.sys.repositories;

import com.fds.food.ordering.sys.models.FoodProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodProductRepository extends JpaRepository<FoodProduct, Integer> {
    @Query(
            value="SELECT * FROM food_products where vendor_name=?1",
            nativeQuery = true)
    List<FoodProduct> findVendorsAllProducts(String vendorId);

//    @Query("SELECT f FROM vendors_products f AND p from food_products p where f.product_id = p.id")
    @Query(
            value = "select f.id,f.name,f.price,f.vendor_name from vendors_products v, food_products f where v.product_id=f.id",
            nativeQuery = true)
    List<FoodProduct> getAllProducts();
}
