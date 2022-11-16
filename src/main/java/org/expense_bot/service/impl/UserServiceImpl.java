package org.expense_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.expense_bot.handler.NewUserHandler;
import org.expense_bot.model.Request;
import org.expense_bot.model.User;
import org.expense_bot.repository.UserRepository;
import org.expense_bot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final NewUserHandler firstEnteredHandler;

  @Override
  public void checkUser(Request request) {
	final String firstName = getFirstName(request);
	final Optional<User> user = getByUserId(request.getUserId());
	if(!user.isPresent()){
	  firstEnteredHandler.handle(request);
	}
	 userRepository.save(getUser(request, firstName));
  }

  private String getFirstName(Request request) {
    if(request.getUpdate().hasMessage()){
	  return request.getUpdate().getMessage().getFrom().getFirstName();
	}
    return request.getUpdate().getCallbackQuery().getFrom().getFirstName();
  }

  private Optional<User> getByUserId(Long userId) {
	return userRepository.findByUserId(userId);
  }

  private User getUser(Request request, String firstName) {
	return User.builder()
	  .name(firstName)
	  .userId(request.getUserId())
	  .build();
  }

}
