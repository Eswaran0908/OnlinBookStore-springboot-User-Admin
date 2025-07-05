package com.example.demo.ControllerFinal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.example.demo.ModelEntitiesFinal.Book;
import com.example.demo.ModelEntitiesFinal.User;
import com.example.demo.ModelServicesFinal.BookService;

import com.example.demo.ModelServicesFinal.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

	 private final UserService userService;
	    private final BookService bookService;
	   
	    @Autowired
	    public UserController(UserService userService, BookService bookService) {
	        this.userService = userService;
	        this.bookService = bookService;
	    }
    
	//Show Home Page
	@GetMapping("/")
	public String showFrontPage() {
		return "frontindex";
	}
	
	 // Show Login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty User object to the model
        return "blogin"; // Return login page
    }
    
    // Show Registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty User object to the model
        return "bregister"; // Return registration page
    }
    
    // Handle user registration
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        // Register the user using the service and handle validation errors
        List<String> errors = userService.registerUser(user, bindingResult);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors); // Add errors to the model to display in the view
            return "bregister"; // Return to the registration page if there are errors
        }

        return "redirect:/login"; // Redirect to login page on successful registration
    }
    
   /* 
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "blogin"; // Return to the login page if validation fails
        }

        // Authenticate user
        User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());

        if (authenticatedUser != null) {
            // Store user information in session after successful login
            session.setAttribute("username", authenticatedUser.getUsername()); // This is important for session persistence

            // Fetch books and proceed with your logic
            Pageable pageable = PageRequest.of(0, 20); // Fetch the first 20 books by default
            Page<Book> books = bookService.searchBooks("", 0, 0, "", pageable);
            
            if (books != null && books.hasContent()) {
                model.addAttribute("books", books);  // Add books to the model
            } else {
                model.addAttribute("books", Page.empty()); // If no books are found, return an empty page
            }

            model.addAttribute("username", authenticatedUser.getUsername());  // Set the authenticated username
            return "book"; // Redirect to book.html
        }

        // If login fails, show error message
        model.addAttribute("error", "Invalid username or password");
        return "blogin";  // Return to the login page
    }
*/
    
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "blogin";  // Return to the login page if validation fails
        }

        User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());

        if (authenticatedUser != null) {
            session.setAttribute("username", authenticatedUser.getUsername());  // Store username in session
            return "bhome";  // Redirect to home after successful login
        }

        model.addAttribute("error", "Invalid username or password");
        return "blogin";  // Return to login page on failure
    }

    
  //Show Home Page
  	@GetMapping("/ex")
  	public String showVideoPage() {
  		return "ex";
  	} 
    
}
