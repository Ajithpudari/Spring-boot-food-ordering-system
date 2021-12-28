package com.fds.food.ordering.sys.repositories;

import com.fds.food.ordering.sys.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users_roles RU INNER JOIN users U ON U.id = RU.user_id INNER JOIN roles R ON RU.role_id = R.id  WHERE R.name = 'VENDOR'",
            nativeQuery = true)
    public Set<User> getVendors();

    @Query(value = "SELECT * FROM users_roles RU INNER JOIN users U ON U.id = RU.user_id INNER JOIN roles R ON RU.role_id = R.id  WHERE R.name = 'USER'",

            nativeQuery = true)
    public Set<User> getCustomers();


}