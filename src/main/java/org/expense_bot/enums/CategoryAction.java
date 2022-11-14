package org.expense_bot.enums;

import org.expense_bot.Configuration;
import org.expense_bot.constant.Messages;
import org.expense_bot.handler.categories.action_state.CategoryActionState;
import org.expense_bot.handler.categories.action_state.CategoryAddNewHandler;
import org.expense_bot.handler.categories.action_state.CategoryDefaultHandler;
import org.expense_bot.handler.categories.action_state.CategoryDeleteHandler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.beans.beancontext.BeanContext;

public enum CategoryAction {

  ADD_NEW_CATEGORY(Messages.ADD_CATEGORY){
	@Override
	public CategoryActionState handle() {
	  return new CategoryAddNewHandler();
	}
  },
  DELETE_MY_CATEGORIES(Messages.DELETE_MY_CATEGORIES){
	@Override
	public CategoryActionState handle() {
	  return new CategoryDeleteHandler();
	}
  },
  ADD_FROM_DEFAULT(Messages.ADD_FROM_DEFAULT){
	@Override
	public CategoryActionState handle() {
	  return new CategoryDefaultHandler();
	}
  };

  private final String value;

  public abstract CategoryActionState handle();


  CategoryAction(String value) {
	this.value = value;
  }

  public String getValue() {
	return value;
  }

  public static CategoryAction parseAction(String text) {
	switch (text) {
	  case Messages.ADD_CATEGORY:
		return CategoryAction.ADD_NEW_CATEGORY;
	  case Messages.DELETE_MY_CATEGORIES:
		return CategoryAction.DELETE_MY_CATEGORIES;
	  case Messages.ADD_FROM_DEFAULT:
		return CategoryAction.ADD_FROM_DEFAULT;
	  default:
		return null;
	}
  }
}
