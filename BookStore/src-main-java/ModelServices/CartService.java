package com.example.demo.ModelServicesFinal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ModelEntitiesFinal.Book;
import com.example.demo.ModelRepositoriesFinal.BookRepository;

@Service
public class CartService {
	
	
	private Map<Long, Integer> cart = new HashMap<>(); // Key: Book ID, Value: Quantity

    @Autowired
    private BookRepository bookRepository;

    //AddtoBookCart
    public void addBookToCart(Long bookId, int quantity) {
        cart.put(bookId, cart.getOrDefault(bookId, 0) + quantity);
    }

    public Map<Long, Integer> getCartItems() {
        return cart;
    }

    //Calculate Total
    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long bookId = entry.getKey();
            Integer quantity = entry.getValue();
            Book book = bookRepository.findById(bookId).orElse(null);
            if (book != null) {
                total += book.getPrice() * quantity;
            }
        }
        return total;
    }

    public double convertToUSD(double totalPriceInRs) {
        double conversionRate = 0.012;  // Assuming 1 Rs = 0.012 USD (just an example, update with real rate)
        return totalPriceInRs * conversionRate;
    }

    
    
    //UpdateCart
    public void updateCart(Long bookId, int quantity) {
        // Get the current cart
        Map<Long, Integer> cartItems = getCartItems();

        // Update the quantity for the given bookId
        if (cartItems.containsKey(bookId)) {
            cartItems.put(bookId, quantity);
        } else {
            // If the bookId doesn't exist, you could optionally handle that
            System.out.println("Book not found in the cart");
        }
    }


    //RemoveCart
    public void removeFromCart(Long bookId) {
        cart.remove(bookId);
    }

}
