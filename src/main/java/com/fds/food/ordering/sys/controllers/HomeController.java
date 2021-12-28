package com.fds.food.ordering.sys.controllers;

import com.fds.food.ordering.sys.models.Role;
import com.fds.food.ordering.sys.models.User;
import com.fds.food.ordering.sys.repositories.RoleRepository;
import com.fds.food.ordering.sys.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class HomeController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/")
    public String home(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Principal principal){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //below is code for if some logged in user tries to access login page then redirect them to respective homepage
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            User user = userDetailsService.getUserModelByUsername(principal.getName());
            try {
                if (user.getRoles().iterator().next().getName().equals("VENDOR"))
                    return "redirect:/vendor/";
                else if (user.getRoles().iterator().next().getName().equals("USER"))
                    return "redirect:/user/";
            }
            catch (Exception r){return "redirect:/admin/"; }
        }
        return "loginPage";
    }
    @GetMapping("/register")
    public String register(Model model){
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("VENDOR");
        model.addAttribute("roles",roles);
        return "register";
    }

    @PostMapping("/register")
    public String register(String username, String password, String confirmPassword, String role, Model model){
        User user = new User();
        if(!password.equals(confirmPassword)){
            String error = "Passwords do not match";
            model.addAttribute("error", error);
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            roles.add("VENDOR");
            model.addAttribute("roles",roles);
            return "register";
        }
        AtomicBoolean taken = new AtomicBoolean(false);
        userDetailsService.findAll().stream().forEach(user1 -> {
            if(user1.getUsername().equals(username)){

                taken.set(true);

            }
        });
        if(taken.get()){
            String error = "Username already taken";
            model.addAttribute("error", error);
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            roles.add("VENDOR");
            model.addAttribute("roles",roles);

            return "register";
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getRole(role));
        user.setRoles(roles);
        userDetailsService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/forbidden")
    public String forbidden(Principal principal, Model model) {
        return "forbidden";
    }





}
