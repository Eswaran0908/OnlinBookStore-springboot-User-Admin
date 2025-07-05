package com.example.demo.ControllerFinal;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.ModelEntitiesFinal.Book;

import com.example.demo.ModelServicesFinal.BookService;


import jakarta.servlet.http.HttpSession;

@Controller
public class LibraryController {

  
        @Autowired
        private BookService bookService; // Inject BookService to access real data

    
     
        // Add a book to the user's library (session-based)
        @PostMapping("/addToLibrary")
        public String addToLibrary(@RequestParam("bookId") Long bookId, HttpSession session) {
            List<Book> library = (List<Book>) session.getAttribute("library");
            if (library == null) {
                library = new ArrayList<>();
            }
            Book book = bookService.getBookById(bookId); // Fetch the real book
            if (book != null) {
                library.add(book); // Add the book to the library
            }
            session.setAttribute("library", library);
            return "redirect:/myLibrary";
        }

        // Display the user's library (from session)
        @GetMapping("/myLibrary")
        public String viewLibrary(HttpSession session, Model model) {
            List<Book> library = (List<Book>) session.getAttribute("library");
            if (library != null) {
                model.addAttribute("library", library); // Add books to the model for display
            }
            return "myLibrary"; // Return the library view
        }
    
        
        
        // Remove a book from the user's library (session-based)
        @PostMapping("/removeFromLibrary")
        public String removeFromLibrary(@RequestParam("bookId") Long bookId, HttpSession session) {
            List<Book> library = (List<Book>) session.getAttribute("library");
            if (library != null) {
                library.removeIf(book -> book.getId().equals(bookId)); // Remove the book from the library
                session.setAttribute("library", library); // Update the session with the new library list
            }
            return "redirect:/myLibrary"; // Redirect back to the library pag
        }
  
	
        
}