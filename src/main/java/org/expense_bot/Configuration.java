package org.expense_bot;

import org.expense_bot.handler.categories.action_state.CategoryAddNewHandler;
import org.expense_bot.handler.categories.action_state.CategoryDefaultHandler;
import org.expense_bot.handler.categories.action_state.CategoryDeleteHandler;
import org.expense_bot.service.impl.TelegramService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

  private static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

  @Bean
  public CategoryAddNewHandler categoryAddNewHandler() {
    context.registerBean(CategoryAddNewHandler.class);
    context.refresh();
    return context.getBean(CategoryAddNewHandler.class);
  }

  @Bean
  public CategoryDeleteHandler categoryDeleteHandler() {
    context.registerBean(CategoryDeleteHandler.class);
    context.refresh();
    return context.getBean(CategoryDeleteHandler.class);
  }

  @Bean
  public CategoryDefaultHandler categoryDefaultHandler() {
    context.register(CategoryDefaultHandler.class);
    context.refresh();
    return context.getBean(CategoryDefaultHandler.class);
  }

}
