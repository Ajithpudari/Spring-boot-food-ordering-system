package com.fds.food.ordering.sys;

import com.fds.food.ordering.sys.models.Role;
import com.fds.food.ordering.sys.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class FoodOrderingSysApplication {
    @Autowired
    RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(FoodOrderingSysApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeRoleTable() {
        return args -> {
            Role adminRole = roleRepository.getRole("ADMIN");
            Role vendorRole = roleRepository.getRole("VENDOR");
            Role customerRole = roleRepository.getRole("USER");
            try {
                adminRole.getName();
            } catch (Exception e) {
                roleRepository.save(new Role("ADMIN"));
            }

            try {
                vendorRole.getName();
            } catch (Exception e) {
                roleRepository.save(new Role("VENDOR"));
            }

            try {
                customerRole.getName();
            } catch (Exception e) {
                roleRepository.save(new Role("USER"));
            }


        };
    }

}
