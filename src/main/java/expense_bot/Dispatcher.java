package expense_bot;

import expense_bot.handler.RequestHandler;
import expense_bot.model.Request;
import expense_bot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Dispatcher {

  private final List<RequestHandler> handlers;

  @Autowired
  private UserService userService;

  /**
   * Pay attention at this constructor
   * Since we have some global handlers,
   * like command /start which can interrupt any conversation flow.
   * These global handlers should go first in the list
   */
  public Dispatcher(List<RequestHandler> handlers) {
    this.handlers = handlers
      .stream()
      .sorted(Comparator
        .comparing(RequestHandler::isGlobal)
        .reversed())
      .collect(Collectors.toList());
  }

  public boolean dispatch(Request request) {
    userService.checkUser(request);

    for (RequestHandler requestHandler : handlers) {
      if (requestHandler.isApplicable(request)) {
        requestHandler.handle(request);
        userService.updateActionTime(request.getUserId());
        return true;
      }
    }
    return false;
  }

}

