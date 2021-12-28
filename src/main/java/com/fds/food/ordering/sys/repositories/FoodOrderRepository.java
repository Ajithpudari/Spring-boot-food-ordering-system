package com.fds.food.ordering.sys.repositories;

import com.fds.food.ordering.sys.models.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder,Integer> {

    @Query(value = "Select * from food_orders",
        nativeQuery = true)
    Set<FoodOrder> getAll();
}
