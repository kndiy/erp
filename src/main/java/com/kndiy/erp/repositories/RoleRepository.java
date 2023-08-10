package com.kndiy.erp.repositories;

import com.kndiy.erp.entities.userCluster.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.role = ?1")
    Role findByRole(String role);


}
