package com.example.demo.ControllerFinal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ModelServicesFinal.CartService;

@Controller
public class PaymentController {
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("/payment-checkout")
	public String showPaymentPage(Model model) {
	    // Add cart details to the model to display it on the payment page
	    model.addAttribute("cartItems", cartService.getCartItems());
	    model.addAttribute("totalPrice", cartService.calculateTotal());
	    return "payment";  // This loads payment.html
	}


	@PostMapping("/process-payment")
	public String processPayment(@RequestParam("paymentMethod") String paymentMethod,
	                             @RequestParam("cardNumber") String cardNumber,
	                             @RequestParam("expiryDate") String expiryDate,
	                             @RequestParam("cvv") String cvv,
	                             RedirectAttributes redirectAttributes) {
	    // Handle payment processing logic here (interact with payment gateway)
	    
	    // Add any success message or data to redirect
	    redirectAttributes.addFlashAttribute("message", "Payment successful!");
	    return "redirect:/order-success";  // Redirect to order success page
	}

    
    
	@GetMapping("/order-success")
	public String orderSuccess(Model model) {
	    // Pass the payment amount in Rs.
	    double totalPrice = cartService.calculateTotal(); 
	    model.addAttribute("totalPrice", totalPrice); 

	    // Assuming you have a method to convert the Rs. total into USD
	    double total = cartService.convertToUSD(totalPrice);  // Convert Rs. to USD if needed
	    model.addAttribute("total", total); // Pass the total price in USD

	    return "order-success"; // Name of the Thymeleaf template
	}


}
