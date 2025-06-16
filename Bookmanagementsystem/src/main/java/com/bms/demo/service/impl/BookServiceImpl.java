package com.bms.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bms.demo.dto.BookDto;
import com.bms.demo.exception.BookNotFoundException;
import com.bms.demo.exception.BookSaveException;
import com.bms.demo.model.Book;
import com.bms.demo.repository.BookRepository;
import com.bms.demo.service.BookService;
@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

	   public BookServiceImpl(BookRepository bookRepository) {
	        this.bookRepository = bookRepository;
	    }
	   private final String uploadDir = "src/main/resources/static/uploads/";
	   
	   @Override
	   public Boolean saveBook(BookDto bookDto,String filename) {
	       try {
	           Book book = new Book();
	           book.setId(bookDto.getId());
	           book.setBook_id(bookDto.getBook_id());
	           book.setName(bookDto.getName());
	           book.setPrice(bookDto.getPrice());
	           book.setLocation(bookDto.getLocation());
	           book.setPhoto(filename); // Store the file name or path

	           bookRepository.save(book);
	           return true;
	       } catch (Exception e) {
	           e.printStackTrace();
	           return false;
	       }
	   }

    @Override
    public List<Book> findAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public boolean deleteBookById(Integer id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id)		
        .orElseThrow(()-> new BookNotFoundException("book not found with id:"+ id));
    }

	@Override
	public void clearAll() {
		bookRepository.deleteAll();
		
		
	}


}







//save()
// log the exception in real-world apps
// logger.error("Failed to save book: {}", book, e);
