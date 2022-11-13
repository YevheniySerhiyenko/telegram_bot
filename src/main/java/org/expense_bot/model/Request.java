package org.expense_bot.model;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@Builder
public class Request {
    private Update update;
    private Long userId;
    private Session session;
}
