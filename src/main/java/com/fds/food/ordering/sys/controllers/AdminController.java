package com.fds.food.ordering.sys.controllers;

import com.fds.food.ordering.sys.models.FoodProduct;
import com.fds.food.ordering.sys.models.User;
import com.fds.food.ordering.sys.repositories.FoodOrderRepository;
import com.fds.food.ordering.sys.services.FoodProductService;
import com.fds.food.ordering.sys.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    FoodProductService foodProductService;

    @Autowired
    FoodOrderRepository foodOrderRepository;





    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("vendors", userDetailsService.getVendors());
        return "admin/admin-dashboard";
    }

    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("customers", userDetailsService.getCustomers());
        return "admin/admin-all-customers";
    }

    @GetMapping("/foodProducts")
    public String foodProducts(Model model) {
        model.addAttribute("allFoodProducts", foodProductService.getAll());
        return "admin/admin-foodproducts";
    }


    @GetMapping("/disableUser")
    public String disableUser(String username){
        User user = userDetailsService.getUserModelByUsername(username);
        user.setEnabled(false);
        userDetailsService.save(user);
        return "redirect:/admin/";
    }

    @GetMapping("/enableUser")
    public String enableUser(String username){
        User user = userDetailsService.getUserModelByUsername(username);
        user.setEnabled(true);
        userDetailsService.save(user);
        return "redirect:/admin/";
    }

    @GetMapping("/deleteFoodProduct")
    public String deleteFoodProduct(int id){
        FoodProduct foodProduct = foodProductService.getFoodProductById(id);
        User user = userDetailsService.getUserModelByUsername(foodProduct.getVendor_name());
        user.deleteFoodProduct(foodProduct);
        userDetailsService.save(user);
        return "redirect:/admin/foodProducts";
    }

    @GetMapping("/orders")
    public String deleteFoodProduct(Model model){
        model.addAttribute("orders", foodOrderRepository.getAll());
        return "admin/admin-orders";
    }

    @GetMapping("/orderDetails")
    public String orderDetails(int id, Model model){
        model.addAttribute("orderItems", foodOrderRepository.findById(id).get().getOrderItems());
        return "admin/admin-order_details";
    }
}
