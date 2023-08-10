package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.userCluster.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u ORDER BY u.username ASC")
    List<User> findAllOrderASC();

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
}
