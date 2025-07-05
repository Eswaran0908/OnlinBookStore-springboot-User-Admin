package com.example.demo.ControllerFinal;


import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ModelEntitiesFinal.Book;

import com.example.demo.ModelEntitiesFinal.User;
import com.example.demo.ModelRepositoriesFinal.UserRepository;
import com.example.demo.ModelServicesFinal.BookService;
import com.example.demo.ModelServicesFinal.UserService;

import jakarta.servlet.http.HttpSession;


@Controller

public class BookController {

	
	@Autowired
    private BookService bookService;
	private UserService userService;
	
	public BookController(UserService userService) {
        this.userService = userService;  // Dependency injection via constructor
    }

	
    @GetMapping("home/book")
    public String bookHome(Model model,
                            @RequestParam(defaultValue = "") String query,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "20") int size,
                            @RequestParam(defaultValue = "0") double minPrice,
                            @RequestParam(defaultValue = "0") double maxPrice,
                            @RequestParam(defaultValue = "") String authorName) {
        
        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the filtered books from the service
        Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

        // Add the books and filters to the model
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("authorName", authorName);
  

        return "book"; // Return to book.html for rendering
    }

    
/*
   
	// Show books page with pagination and search filters
    @GetMapping("/user/books")
    public String showFiltersBooks(Model model,
                            @RequestParam(defaultValue = "") String query,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "6") int size,
                            @RequestParam(defaultValue = "0") double minPrice,
                            @RequestParam(defaultValue = "0") double maxPrice,
                            @RequestParam(defaultValue = "") String authorName) {
        
    	 // Assuming you have a way to fetch the currently logged-in user
         
        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the filtered books from the service
        Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

        // Add the books and filters to the model
       
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("authorName", authorName);

        return "userBooks"; // Return to book.html for rendering
    }
*/

 // Inside your controller method where you retrieve the Book
    @GetMapping("/book/details/{id}")
    public String getBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);

        // Format the dates before adding them to the model
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedCreatedDate = book.getCreatedDate().format(formatter);
        String formattedModifiedDate = book.getModifiedDate().format(formatter);

        model.addAttribute("book", book);
        model.addAttribute("formattedCreatedDate", formattedCreatedDate);
        model.addAttribute("formattedModifiedDate", formattedModifiedDate);

        return "bookDetails"; // or your specific view name
    }
	
	/*
    @GetMapping("/user/books")
    public String showFilterUserBooks(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "0") double minPrice,
            @RequestParam(defaultValue = "0") double maxPrice,
            @RequestParam(defaultValue = "") String authorName,
            Model model, HttpSession session) {

        // Create Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch the filtered books from the service (assuming bookService is available)
        Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

        // Add the books and filters to the model
        model.addAttribute("books", books);
        model.addAttribute("query", query);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("authorName", authorName);

        // Retrieve the username from the session
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "redirect:/login";  // Redirect to login if the user is not logged in
        }

        // Fetch user details (you can modify this to get the full user object if needed)
        User user = userService.getCurrentUser(username);

        if (user == null) {
            return "redirect:/login";  // If no user found in the database, redirect to login
        }

        // Add user information to the model
        model.addAttribute("user", user); // Add the user object to the model

        // Retrieve subscription details from the session
        String subscriptionPlan = (String) session.getAttribute("subscriptionPlan");
        Double subscriptionPrice = (Double) session.getAttribute("subscriptionPrice");

        // Add subscription details to the model
        model.addAttribute("subscriptionPlan", subscriptionPlan);
        model.addAttribute("subscriptionPrice", subscriptionPrice);

        // Return the view to show the books page
        return "userBooks";  // Thymeleaf template for the userBooks page
    }

    */
    
    

        @Autowired
        private UserRepository userRepository;
      // Assuming bookService handles book-related functionality
        
        // Controller method for userBooks
        @GetMapping("/user/books")
        public String userBooks(
                @RequestParam(defaultValue = "") String query,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "6") int size,
                @RequestParam(defaultValue = "0") double minPrice,
                @RequestParam(defaultValue = "0") double maxPrice,
                @RequestParam(defaultValue = "") String authorName,
                Model model, HttpSession session) {
            
            // Create Pageable object for pagination
            Pageable pageable = PageRequest.of(page, size);

            // Fetch the filtered books from the service
            Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

            // Add the books and filters to the model
            model.addAttribute("books", books);
            model.addAttribute("query", query);
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
            model.addAttribute("authorName", authorName);

            // Retrieve the username from the session
            String username = (String) session.getAttribute("username");

            if (username == null) {
                return "redirect:/login";  // Redirect to login if the user is not logged in
            }

            // Fetch user details from the repository (assuming userRepository is available)
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                model.addAttribute("user", user);  // Add user details to the model
                
                // Retrieve subscription details from the user object
                String subscriptionPlan = user.getSubscriptionPlan();  // Assuming getSubscriptionPlan() exists in User class
                Double subscriptionPrice = user.getSubscriptionPrice();  // Assuming getSubscriptionPrice() exists in User class

                // Add subscription details to the model
                model.addAttribute("subscriptionPlan", subscriptionPlan != null ? subscriptionPlan : "No Plan Selected");
                model.addAttribute("subscriptionPrice", subscriptionPrice != null ? subscriptionPrice : 0.0);
            } else {
                // If user not found, redirect to login or handle accordingly
                return "redirect:/login";
            }

            // Return the view (userBooks.html) to show the books page
            return "userBooks";
        }
    

}
