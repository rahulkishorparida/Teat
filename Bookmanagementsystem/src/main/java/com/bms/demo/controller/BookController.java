package com.bms.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bms.demo.BookmanagementsystemApplication;
import com.bms.demo.dto.BookDto;
import com.bms.demo.model.Book;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import com.bms.demo.service.BookService;

import jakarta.servlet.http.HttpSession;



@Controller
public class BookController {

	private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/uploads/";
	
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping("/")
    public String index(Model model) { //pass c  to view
    	List<Book> allBook = bookService.findAllBook();
    	model.addAttribute("books",allBook);
    	return "book/index";
    }
    
    @GetMapping("/add-book")
  public String addBook() {
	   return "book/add-book";
  }


    @PostMapping("/save-book")
    public String saveBook(@Validated @ModelAttribute BookDto bookDto, 
                           @RequestParam("file") MultipartFile file, 
                           RedirectAttributes redirectAttributes) throws IOException {
    	 String filename = null;
        if (!file.isEmpty()) {
            filename = file.getOriginalFilename();
            String filePath = IMAGE_UPLOAD_DIR + filename;
            Files.copy(file.getInputStream(), Paths.get(filePath));
            bookDto.setPhoto(filename);
        }
        
        boolean result = bookService.saveBook(bookDto, filename);
        if (result) {
            redirectAttributes.addFlashAttribute("successMessage", "Book saved successfully!");
            return "redirect:/add-book";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save book.");
            return "redirect:/add-book";
        }
      
    }
 
    @GetMapping("/view-book/{id}")
    public String viewBook(@PathVariable Integer id, Model model) {
        try {
            Book book = bookService.findById(id);
            model.addAttribute("book", book);
            return "book/view-book";
        } catch (Exception e) {
            // You can log the error or redirect to a 404 page
            return "redirect:/?error=notfound";
        }
    }
    
    @GetMapping("/delete-book/{id}")
    public String deletebook(@PathVariable Integer id) {
     bookService.deleteBookById(id);
    	return"redirect:/";
    }

    @GetMapping("/edit-book/{id}")
    public String editBook(@PathVariable Integer id, Model model) {
        try {
            Book book = bookService.findById(id);
            model.addAttribute("bookk", book);
            return "book/edit-book";
        } catch (Exception e) {
            return "redirect:/?error=notfound";
        }
    }
 
    @PostMapping("/edit-book")
    public String updateBook(@ModelAttribute BookDto bookDto,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes redirectAttributes) throws IOException {

        String filename = bookDto.getPhoto(); // default: existing photo

        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String uniqueId = UUID.randomUUID().toString();
            filename = uniqueId + "_" + originalFilename;
            String filePath = IMAGE_UPLOAD_DIR + filename;
            Files.copy(file.getInputStream(), Paths.get(filePath));
            bookDto.setPhoto(filename);
        }

        boolean saveBook = bookService.saveBook(bookDto, filename);
        if (saveBook) {
            redirectAttributes.addFlashAttribute("successMessage", "Update successful!");
            return "redirect:/edit-book/" + bookDto.getId();
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Update failed.");
            return "redirect:/edit-book/" + bookDto.getId();
        }

    }
    
    @GetMapping("/clear-books")
    public String clearAll() {
    	bookService.clearAll();
		return "redirect:/";
    	
    }
    
    @GetMapping("/upload-book")
  public String upload() {
	   return "book/add-book";
  }

}













//
//@PostMapping("/edit-book")
//public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
//    bookService.saveBook(book);
//    return "redirect:/";
//}

