package com.fds.food.ordering.sys.repositories;


import com.fds.food.ordering.sys.models.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

    @Query("SELECT r FROM Role r WHERE r.name = :rolename")
    public Role getRole(@Param("rolename") String rolename);

}
