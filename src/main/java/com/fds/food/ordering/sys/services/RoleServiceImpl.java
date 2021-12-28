package com.fds.food.ordering.sys.services;

import com.fds.food.ordering.sys.models.Role;
import com.fds.food.ordering.sys.repositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl {

    @Autowired
    RoleRepository roleRepository;
    public Role getRole(String roleName){
        return roleRepository.getRole(roleName);

    }
}
