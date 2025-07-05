package com.example.demo.ControllerFinal;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ModelEntitiesFinal.Book;
import com.example.demo.ModelRepositoriesFinal.BookRepository;
import com.example.demo.ModelServicesFinal.CartService;



@Controller
public class CartController {

	  @Autowired
	    private CartService cartService;

	    @PostMapping("/add-to-cart")
	    public String addToCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
	        cartService.addBookToCart(bookId, quantity);
	        redirectAttributes.addFlashAttribute("message", "Book added to cart");
	        return "redirect:/cart";  // Redirect to cart view, which will show the cart page
	    }

	    @PostMapping("/update-cart")
	    public String updateCart(@RequestParam("bookId") Long bookId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
	        System.out.println("Received bookId: " + bookId);
	        System.out.println("Received quantity: " + quantity);

	        cartService.updateCart(bookId, quantity);
	        redirectAttributes.addFlashAttribute("message", "Cart updated");
	        return "redirect:/cart";
	    }



	    @PostMapping("/remove-from-cart")
	    public String removeFromCart(@RequestParam("bookId") Long bookId, RedirectAttributes redirectAttributes) {
	        cartService.removeFromCart(bookId);
	        redirectAttributes.addFlashAttribute("message", "Book removed from cart");
	        return "redirect:/cart";  // Redirect to cart view after removal
	    }

	    
	    @Autowired
	    private BookRepository bookRepository;
	   
	    @GetMapping("/cart")
	    public String showCart(Model model) {
	        // Get cart items (bookId -> quantity)
	        Map<Long, Integer> cartItems = cartService.getCartItems();

	        // Fetch book details for the cart's book IDs
	        Map<Long, Book> books = new HashMap<>();
	        for (Long bookId : cartItems.keySet()) {
	            bookRepository.findById(bookId).ifPresent(book -> books.put(bookId, book));
	        }

	        // Calculate the total price
	        double total = 0;
	        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
	            Book book = books.get(entry.getKey());
	            if (book != null) {
	                total += book.getPrice() * entry.getValue();
	            }
	        }

	        // Add attributes to the model
	        model.addAttribute("cartItems", cartItems); // Cart items (bookId -> quantity)
	        model.addAttribute("books", books);         // Books details (bookId -> Book)
	        model.addAttribute("total", total);         // Total price

	        return "cart"; // Render the cart.html template
	    }



	    

    
}
