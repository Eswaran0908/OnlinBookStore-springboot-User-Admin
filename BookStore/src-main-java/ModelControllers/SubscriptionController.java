package com.example.demo.ControllerFinal;

import com.example.demo.ModelEntitiesFinal.User;
import com.example.demo.ModelRepositoriesFinal.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;

@Controller
public class SubscriptionController {

    @Autowired
    private UserRepository userRepository;

    // Method to choose the subscription plan
    @PostMapping("/subscription/choose")
    public String chooseSubscription(@RequestParam("plan") String plan, HttpSession session) {
        // Get the username of the currently logged-in user from the session
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }

        // Retrieve the user from the database using the username
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();

            // Set the subscription plan and price based on the selected plan
            switch (plan) {
                case "daily":
                    currentUser.setSubscriptionPlan("Daily Plan");
                    currentUser.setSubscriptionPrice(10.0);
                    break;
                case "weekly":
                    currentUser.setSubscriptionPlan("Weekly Plan");
                    currentUser.setSubscriptionPrice(50.0);
                    break;
                case "monthly":
                    currentUser.setSubscriptionPlan("Monthly Plan");
                    currentUser.setSubscriptionPrice(150.0);
                    break;
                case "yearly":
                    currentUser.setSubscriptionPlan("Yearly Plan");
                    currentUser.setSubscriptionPrice(1500.0);
                    break;
                case "none":
                    currentUser.setSubscriptionPlan("No Plan");
                    currentUser.setSubscriptionPrice(0.0);
                    break;
                default:
                    // Handle the case if no valid plan is selected
                    return "error";
            }

            // Save the updated user to the database
            userRepository.save(currentUser);

            session.setAttribute("subscriptionPlan", currentUser.getSubscriptionPlan());
            session.setAttribute("subscriptionPrice", currentUser.getSubscriptionPrice());
        }

        // Redirect to the books page (or another page that shows the updated subscription)
        return "redirect:/user/books";
    }

    @GetMapping("/subscription")
    public String homePage(HttpSession session, Model model) {
        // Retrieve the username from the session
        String username = (String) session.getAttribute("username");

        // Check if the username is not null
        if (username != null) {
            // Retrieve the user by username from the database
            Optional<User> optionalUser = userRepository.findByUsername(username);

            // Check if the user is found and add to the model
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                model.addAttribute("user", user);  // Pass user object to Thymeleaf
            } else {
                // Handle case where user is not found
                model.addAttribute("user", null);
            }
        } else {
            model.addAttribute("user", null);  // User is not logged in
        }

        // Redirect to the "bhome" template
        return "bhome";  // Thymeleaf template name
    }
    
    
}


    
    

