package com.example.demo.ControllerFinal;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.ModelEntitiesFinal.Book;
import com.example.demo.ModelServicesFinal.BookService;


@Controller
public class AdminController {

	// Static variables for admin login credentials
	private static final String ADMIN_USERNAME = "BOOKS";
	private static final String ADMIN_PASSWORD = "BOOKS#123";

	@Autowired
	private BookService bookService;

	// Method to show the admin login page
	@GetMapping("/admin")
	public String showAdminLogin() {
		return "admin/adminLogin"; // Returns the login page view
	}

	// Method to handle login authentication
	@PostMapping("/admin/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model) {
		if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
			return "redirect:/admin/books"; // Redirect to products page after successful login
		} else {
			model.addAttribute("error", "Invalid Username or Password!...");
			return "admin/adminLogin"; // Return to the login page if credentials are incorrect
		}
	}
	

	// Show all books with pagination
	@GetMapping("/admin/books")
	public String showAdminBooks(Model model, Pageable pageable) {
		// Set a default page size of 5 (you can adjust this)
		Pageable paginated = PageRequest.of(pageable.getPageNumber(), 6);
		Page<Book> books = bookService.getAvailableBooks(paginated);
		model.addAttribute("books", books);
		return "admin/books";
	}

	@GetMapping("/admin/books/search")
	public String listBooks(Model model,
			@RequestParam(defaultValue = "") String query,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "6") int size,
			@RequestParam(defaultValue = "0") double minPrice, 
			@RequestParam(defaultValue = "0") double maxPrice,
			@RequestParam(defaultValue = "") String authorName) {

		Pageable pageable = PageRequest.of(page, 6, Sort.by("name").ascending());
		Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

		model.addAttribute("books", books);
		model.addAttribute("query", query);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("authorName", authorName);
		return "admin/books"; // or your specific view for admin books
	}

	
	@GetMapping("admin/books/view")
	public String listBooksView(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "6") int size,
	    @RequestParam(defaultValue = "") String query, 
	    @RequestParam(defaultValue = "0") double minPrice,
	    @RequestParam(defaultValue = "0") double maxPrice,
	    @RequestParam(defaultValue = "") String authorName,
	    @RequestParam(defaultValue = "") String sort, // Keep empty string as the default
	    Model model) {

	    // Determine the sort order based on the sort parameter
	    Sort sortOrder;
	    switch (sort) {
	        case "name_asc":
	            sortOrder = Sort.by("name").ascending();
	            break;
	        case "name_desc":
	            sortOrder = Sort.by("name").descending();
	            break;
	        default:
	            sortOrder = Sort.by("id").ascending(); // Default sort order by 'id'
	            break;
	    }

	    Pageable pageable = PageRequest.of(page, size, sortOrder);

	    // Fetch the filtered and sorted books
	    Page<Book> books = bookService.searchBooks(query, minPrice, maxPrice, authorName, pageable);

	    model.addAttribute("books", books);
	    model.addAttribute("query", query);
	    model.addAttribute("minPrice", minPrice);
	    model.addAttribute("maxPrice", maxPrice);
	    model.addAttribute("authorName", authorName);
	    model.addAttribute("sort", sort); // Add the sort parameter so the form is pre-populated

	    return "admin/viewBooks"; // Return the view
	}

	/*
	 * // Show the form to edit an existing book
	 * 
	 * @GetMapping("/admin/edit-book/{id}") public String
	 * showEditBookForm(@PathVariable Long id, Model model) { Book book =
	 * bookService.getBookById(id); // Fetch the book to edit
	 * model.addAttribute("book", book); // Pass the book to the view return
	 * "admin/edit-books"; // Return the edit form view }
	 */

// Show the form to edit an existing book
	@GetMapping("/admin/edit-book/{id}")
	public String editBook(@PathVariable Long id, Model model) {
	    Book book = bookService.getBookById(id);  // Fetch the book from the service
	    model.addAttribute("book", book);  // Add the book to the model
	    return "admin/edit-books";  // Return the template
	}

	// Step 2: Handle Book Update (Including File Uploads)
	@PostMapping("/admin/edit-book/{id}")
	public String editBook(@PathVariable Long id, @ModelAttribute Book book) {
	    // Fetch the existing book from the database
	    Book existingBook = bookService.getBookById(id);

	    // If the book does not exist, redirect to an error page or handle the case
	    if (existingBook == null) {
	        // You can redirect the user to an error page or handle it differently
	        return "redirect:/error";  // Example redirection if the book isn't found
	    }

	    // Ensure the book ID is maintained during the update
	    book.setId(id);

	    // Set the modifiedDate (this will be updated on each edit)
	    book.setModifiedDate(LocalDateTime.now());

	    // Set the createdDate to be the same as the original (it should not change during edits)
	    book.setCreatedDate(existingBook.getCreatedDate());

	    // Save the updated book
	    bookService.saveBook(book);

	    // Redirect back to the books list or show success page
	    return "redirect:/admin/books";
	}




		//Show the form to add a new book
		@GetMapping("/admin/books/add")
		public String showAddBooksForm(Model model) {
			// Create a new Book instance with the default constructor
			model.addAttribute("book", new Book()); // This creates an empty Book object
			return "admin/add-books"; // Return the view where the form will be displayed
		}

	// Handle book addition
		@PostMapping("/admin/books/add")
		public String addBook(@ModelAttribute Book book) {
		    // Set createdDate and modifiedDate when adding a new book
		    book.setCreatedDate(LocalDateTime.now());  // Set the current date and time
		    book.setModifiedDate(LocalDateTime.now()); // Initially, modifiedDate is the same as createdDate
		    bookService.saveBook(book); // Save the book to the service
		    return "redirect:/admin/books"; // Redirect back to the books page
	}
		
	
	/* * @PostMapping("/admin/books/add") public String
	 * addBook(@ModelAttribute("book") Book book, Model model) { // After the form
	 * is submitted, save the book to the database
	 * bookService.createBook(book.getName(), book.getImageURL(),
	 * book.getDescription(), book.getPrice(), book.getAuthorName());
	 * model.addAttribute("message", "Book added successfully!"); return
	 * "redirect:/admin/books"; // Redirect to the books list page }
	 */

// Method to delete a book by ID
	@GetMapping("/admin/delete-book/{id}")
	public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		boolean isDeleted = bookService.deleteBookById(id);
		if (isDeleted) {
			redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
		} else {
			redirectAttributes.addFlashAttribute("error", "Error deleting book.");
		}
		return "redirect:/admin/books";
	}

}
