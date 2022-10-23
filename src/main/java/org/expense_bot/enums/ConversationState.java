package org.expense_bot.enums;

public enum ConversationState {
    CONVERSATION_STARTED,
    WAITING_FOR_CATEGORY,
    WAITING_FOR_SUM,
    WAITING_CHECK_CATEGORY,
    WAITING_FOR_PERIOD;

    ConversationState() {
    }
}
