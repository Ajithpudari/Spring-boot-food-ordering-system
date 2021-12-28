package com.fds.food.ordering.sys.controllers;

import com.fds.food.ordering.sys.models.*;
import com.fds.food.ordering.sys.services.FoodProductService;
import com.fds.food.ordering.sys.services.RoleServiceImpl;
import com.fds.food.ordering.sys.services.UserDetailsServiceImpl;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;

    @GetMapping("/")
    public String home(Model model, Principal principal){
        user = userDetailsService.getUserModelByUsername(principal.getName());
        model.addAttribute("vendorsAllProducts",user.getFoodProducts());
        return "Vendor/vendor-dashboard";
    }

    @PostMapping("/addFoodProduct")
    public String addFoodProduct(FoodProduct foodProduct, Principal principal){
        foodProduct.setVendor_name(principal.getName());
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Collection<FoodProduct> foodProducts = user.getFoodProducts()==null?new ArrayList<>():user.getFoodProducts();
        foodProducts.add(foodProduct);
        user.setFoodProducts(foodProducts);
        userDetailsService.save(user);
        return "redirect:/vendor/";
    }

    @RequestMapping(value = "/updateFoodProduct", method = {RequestMethod.PUT, RequestMethod.GET})
    public String updateFoodProduct(FoodProduct foodProduct, Principal principal){
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Collection<FoodProduct> foodProducts = user.getFoodProducts();
        foodProducts = foodProducts.stream().map(fp -> {
            if(fp.getId()==foodProduct.getId()){
                fp.setName(foodProduct.getName());
                fp.setPrice(foodProduct.getPrice());
            }
            return fp;
        }).collect(Collectors.toList());
        user.setFoodProducts(foodProducts);
        userDetailsService.save(user);
        return "redirect:/vendor/";
    }

    @RequestMapping("/getFoodProduct")
    @ResponseBody
    public FoodProduct getFoodProduct(int id, Principal principal){
//        return foodProductService.getFoodProductById(id);
        user = userDetailsService.getUserModelByUsername(principal.getName());
        return user.getFoodProducts().stream().filter(foodProduct1 -> foodProduct1.getId()==id).findFirst().orElseThrow();
    }

    @RequestMapping(value = "/deleteFoodProduct", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String delete(Integer id, Principal principal){
        user = userDetailsService.getUserModelByUsername(principal.getName());
//        foodProductService.deleteFoodProduct(id);
        Collection<FoodProduct> foodProducts = user.getFoodProducts();
        foodProducts.removeIf(fp -> fp.getId()==id);
        user.setFoodProducts(foodProducts);
        userDetailsService.save(user);
        return "redirect:/vendor/";
    }


    @GetMapping("/profile")
    public String profile(){
        return "Vendor/vendor-profile";
    }

    @GetMapping("/changePasswordForm")
    public String changePasswordForm(){
        return "Vendor/vendor-change-password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(String currentPassword, String newPassword, Principal principal, Model model){
        user = userDetailsService.getUserModelByUsername(principal.getName());

        if(passwordEncoder.matches(currentPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userDetailsService.save(user);
        }
        else {
            model.addAttribute("error", "wrong current password");
            return "Vendor/vendor-change-password";
        }
        return "redirect:/vendor/";
    }

    @GetMapping("/orders")
    public String orders(Model model, Principal principal){
        user = userDetailsService.getUserModelByUsername(principal.getName());
        model.addAttribute("allOrders", user.getFoodOrders());
        return "Vendor/vendor-orders";
    }

    @GetMapping("/showOrderItems")
    public String showOrderItems(int id, Principal principal, Model model){
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Optional<FoodOrder> foodOrder = user.getFoodOrders().stream().filter(foodOrder1 -> foodOrder1.getId()==id).findFirst();
        model.addAttribute("orderItems",foodOrder.get().getOrderItems());
        return "Vendor/vendor-order-details";
    }

    @GetMapping("/setDelivered")
    public String setDelivered(Principal principal, int id){

        user = userDetailsService.getUserModelByUsername(principal.getName());
        user.setFoodOrders(user.getFoodOrders().stream().map(foodOrder -> {
            if(foodOrder.getId()==id){
                foodOrder.setStatus(false);
            }
            return foodOrder;
        }).collect(Collectors.toSet()));
        userDetailsService.save(user);
        return "redirect:/vendor/orders";
    }




}
