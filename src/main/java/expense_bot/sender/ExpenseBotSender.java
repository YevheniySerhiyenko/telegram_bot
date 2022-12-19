package expense_bot.sender;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Getter
@Component
public class ExpenseBotSender extends DefaultAbsSender {

  @Value("${bot.token}")
  private String botToken;

  public ExpenseBotSender() {
    super(new DefaultBotOptions());
  }

}