package com.bms.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.demo.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
