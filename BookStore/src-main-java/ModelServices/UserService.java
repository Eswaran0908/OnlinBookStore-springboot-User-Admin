package com.example.demo.ModelServicesFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


import com.example.demo.ModelEntitiesFinal.User;
import com.example.demo.ModelRepositoriesFinal.UserRepository;


@Service
public class UserService {
	
	   @Autowired
	private UserRepository userRepository;

 
    // Register user
    public List<String> registerUser(User user, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        // Check if username already exists
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            errors.add("Username already exists.");
        }

        // Check if there are validation errors
        if(bindingResult.hasErrors()) {
            for(ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
        }

        // Password and confirm password match validation
        if (!user.isPasswordMatching()) {
            errors.add("Password and Confirm Password must be the same.");
        }

        // If no errors, save the user
        if (errors.isEmpty()) {
            userRepository.save(user);
        }

        return errors;
    }

    
    // Login user
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Return user if credentials are correct
        }
        return null; // Return null if credentials are incorrect
    }
    
    
   
	 // If you want to get a user by username (in case of other requirements)
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get user by ID
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);

    }
 
   

	
}
