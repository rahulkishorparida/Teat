package com.bms.demo.exception;

public class BookSaveException extends RuntimeException {
	public BookSaveException (String message) {
		super(message); //immidiate parent
	}

}
