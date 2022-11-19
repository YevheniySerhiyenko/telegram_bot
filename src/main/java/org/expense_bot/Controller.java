package org.expense_bot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


  @GetMapping("/")
  public String health() {
	return "Hello & Welcome to CloudKatha !!!";
  }
}
