package org.expense_bot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


  @RequestMapping("/")
  public String health() {
	return "thymeleaf_template";
  }

}
