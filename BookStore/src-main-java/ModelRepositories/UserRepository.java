package com.example.demo.ModelRepositoriesFinal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.ModelEntitiesFinal.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
  
    // Custom query method to find a user by username
  
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNo(String phoneNo);

    
    
    
}