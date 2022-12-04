package org.expense_bot.exception;

public class UserNotFoundException extends RuntimeException{

  public UserNotFoundException(String message) {
	super(message);
  }

}
