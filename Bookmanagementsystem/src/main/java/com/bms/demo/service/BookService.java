package com.bms.demo.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bms.demo.dto.BookDto;
import com.bms.demo.model.Book;


public interface BookService {
	
	public Boolean saveBook(BookDto bookDto, String filename);
	public List<Book> findAllBook();
	public boolean deleteBookById(Integer id);
	public Book findById(Integer id);
	public void clearAll();
	
}
