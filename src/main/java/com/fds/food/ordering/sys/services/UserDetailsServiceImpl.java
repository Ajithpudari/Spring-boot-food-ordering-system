package com.fds.food.ordering.sys.services;


import com.fds.food.ordering.sys.models.MyUserDetails;
import com.fds.food.ordering.sys.models.User;
import com.fds.food.ordering.sys.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }

    public User save(User user){
        userRepository.save(user);
        return user;
    }

    public User getUserModelByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public Set<User> getVendors(){
        return userRepository.getVendors();
    }

    public Set<User> getCustomers(){
        return userRepository.getCustomers();
    }

    public void deleteById(int id){
        userRepository.deleteById( id);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }



}