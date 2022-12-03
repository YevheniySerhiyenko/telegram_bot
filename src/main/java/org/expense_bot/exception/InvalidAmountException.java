package org.expense_bot.exception;

public class InvalidAmountException extends RuntimeException{

  public InvalidAmountException(String message) {
	super(message);
  }

}
