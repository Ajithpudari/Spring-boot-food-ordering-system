package com.fds.food.ordering.sys.controllers;

import com.fds.food.ordering.sys.models.*;
import com.fds.food.ordering.sys.services.FoodProductService;
import com.fds.food.ordering.sys.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/")
public class CustomerController {

    @Autowired
    FoodProductService foodProductService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("allFoodProducts", foodProductService.getAll());
        return "Customer/customer-dashboard";
    }

    @GetMapping("/addToCart")
    public String addToCart(int id, Principal principal) { //id is product_id
        //id = product_id
        FoodProduct foodProduct = foodProductService.getFoodProductById(id);
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Set<CartItem> cartItems = user.getCartItems().size() == 0 ? (new HashSet<>()) : user.getCartItems();
        AtomicBoolean isPresent = new AtomicBoolean(false);
        user.setCartItems(cartItems.stream().map(cartItem -> {
            if (cartItem.getProductId() == id) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                isPresent.set(true);
            }
            return cartItem;
        }).collect(Collectors.toSet()));
        if (isPresent.get()) {
            userDetailsService.save(user);
            return "redirect:/user/";
        }

        CartItem cartItem = new CartItem();
        cartItem.setProductId(id).setUser_id(user);
        cartItem.setPrice(foodProduct.getPrice());
        cartItem.setProduct_name(foodProduct.getName());
        cartItem.setVendor_name(foodProduct.getVendor_name());
        user.addToCart(cartItem);
        userDetailsService.save(user);
        return "redirect:/user/";
    }

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Set<CartItem> cartItems = user.getCartItems() == null ? new HashSet<>() : user.getCartItems();
        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        cartItems.stream().forEach(cartItem -> {
            totalPrice.updateAndGet(v -> (double) (v + (cartItem.getPrice() * cartItem.getQuantity())));
        });

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        return "Customer/cart";
    }

    @RequestMapping(value = "/addQuantity", method = {RequestMethod.PUT, RequestMethod.GET})
    public String addQuantity(int id, Principal principal) { //id = product_id
        //id = product_id
        user = userDetailsService.getUserModelByUsername(principal.getName());
        user.setCartItems(user.getCartItems().stream().map(cartItem -> {
            if (cartItem.getProductId() == id)
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            return cartItem;
        }).collect(Collectors.toSet()));
        userDetailsService.save(user);
        return "redirect:/user/cart";
    }

    @RequestMapping(value = "/decrementQuantity", method = {RequestMethod.PUT, RequestMethod.GET})
    public String decrementQuantity(int id, Principal principal) { //id = product_id
        //id = product_id
        AtomicBoolean isLast = new AtomicBoolean(false);
        user = userDetailsService.getUserModelByUsername(principal.getName());
        user.setCartItems(user.getCartItems().stream().map(cartItem -> {
            if (cartItem.getProductId() == id) {
                if (cartItem.getQuantity() == 1) {
                    isLast.set(true);
                } else
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
            }
            return cartItem;
        }).collect(Collectors.toSet()));

        if (isLast.get()) {
            user.setCartItems(user.getCartItems().stream().filter(cartItem -> cartItem.getProductId() != id
            ).collect(Collectors.toSet()));
        }
        userDetailsService.save(user);
        return "redirect:/user/cart";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(String address, Principal principal) {

        user = userDetailsService.getUserModelByUsername(principal.getName());
        Set<FoodOrder> foodOrders = user.getFoodOrdersCustomer() == null ? new HashSet<>() : user.getFoodOrdersCustomer();
        Set<CartItem> cartItems = user.getCartItems();
        Set<String> vendors = new HashSet<>();
        cartItems.forEach(cartItem -> vendors.add(cartItem.getVendor_name()));
        Set<FoodOrder> newOrders = new HashSet<>();
        vendors.forEach(vendor -> {
            Set<OrderItem> orderItems = new HashSet<>();
            FoodOrder order = new FoodOrder();
            cartItems.forEach(cartItem -> {
                if (cartItem.getVendor_name().equals(vendor)) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setOrder_id(order);
                    orderItem.setProduct_name(cartItem.getProduct_name());
                    orderItems.add(orderItem);
                }
                order.setVendor_id((User) userDetailsService.getUserModelByUsername(vendor))
                        .setCustomer_id(user).setOrderItems(orderItems)
                        .setStatus(true).setAddress(address).setPlacedAt(new Timestamp(System.currentTimeMillis()))
                        .setVendor_name(userDetailsService.getUserModelByUsername(vendor).getUsername());
                double ordertotPrice = order.getOrderItems().stream().mapToDouble(OrderItem::getPrice).sum();
                order.setTotalPrice(ordertotPrice);
                user.addFoodOrderCustomer(order);

            });
        });
        user.clearCart();
        userDetailsService.save(user);
        return "redirect:/user/orders";
    }

    @GetMapping("/orders")
    public String customersOrders(Model model, Principal principal) {
        user = userDetailsService.getUserModelByUsername(principal.getName());
        model.addAttribute("customersOrders", user.getFoodOrdersCustomer());
        return "Customer/customer-orders";
    }

    @GetMapping("/profile")
    public String profile() {
        return "Customer/customer-profile";
    }


    @GetMapping("/changePasswordForm")
    public String changePasswordForm() {
        return "Customer/customer-change-password";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(String currentPassword, String newPassword, Principal principal, Model model) {
        user = userDetailsService.getUserModelByUsername(principal.getName());

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userDetailsService.save(user);
        } else {
            model.addAttribute("error", "wrong current password");
            return "Customer/customer-change-password";
        }
        return "redirect:/user/";
    }

    @GetMapping("showOrderItems")
    public String showOrderItems(int id, Principal principal, Model model) {
        user = userDetailsService.getUserModelByUsername(principal.getName());
        Optional<FoodOrder> foodOrder = user.getFoodOrdersCustomer().stream().filter(foodOrder1 -> foodOrder1.getId() == id).findFirst();
        model.addAttribute("orderItems", foodOrder.get().getOrderItems());
        return "Customer/customer-order-details";
    }

}
