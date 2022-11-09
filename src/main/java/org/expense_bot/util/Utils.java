package org.expense_bot.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.expense_bot.model.UserRequest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.EntityType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class Utils {

  public static User getEffectiveUser(Update update) {
    User user = null;
    if (update.hasMessage()) {
      user = update.getMessage().getFrom();
    } else if (update.hasEditedMessage()) {
      user = update.getEditedMessage().getFrom();
    } else if (update.hasInlineQuery()) {
      user = update.getInlineQuery().getFrom();
    } else if (update.hasChosenInlineQuery()) {
      user = update.getChosenInlineQuery().getFrom();
    } else if (update.hasCallbackQuery()) {
      user = update.getCallbackQuery().getFrom();
    } else if (update.hasShippingQuery()) {
      user = update.getShippingQuery().getFrom();
    } else if (update.hasPreCheckoutQuery()) {
      user = update.getPreCheckoutQuery().getFrom();
    } else if (update.hasPollAnswer()) {
      user = update.getPollAnswer().getUser();
    }

    return user;
  }

  public static boolean hasCallBack(UserRequest request){
    return request.getUpdate().hasCallbackQuery();
  }

  public static String getUpdateData(UserRequest userRequest) {
    String data = null;
    if(userRequest.getUpdate().hasMessage()){
      data = userRequest.getUpdate().getMessage().getText();
    }
    if(userRequest.getUpdate().hasCallbackQuery()){
      data = userRequest.getUpdate().getCallbackQuery().getData();
    }
    return data;
  }

  public static String getFileId(final Message message) {
    if (message == null) {
      return null;
    }

    String fileId = null;
    if (message.hasPhoto()) {
      fileId = message.getPhoto().get(0).getFileId();
    } else if (message.hasVideo()) {
      fileId = message.getVideo().getFileId();
    } else if (message.hasVideoNote()) {
      fileId = message.getVideoNote().getFileId();
    } else if (message.hasDocument()) {
      fileId = message.getDocument().getFileId();
    } else if (message.hasAudio()) {
      fileId = message.getAudio().getFileId();
    }
    return fileId;
  }

  public static Chat getEffectiveChat(Update update) {
    Chat chat = null;
    final Message message = getEffectiveMessage(update);
    if (message != null) {
      chat = message.getChat();
    }

    return chat;
  }

  public static Message getEffectiveMessage(Update update) {
    Message message = null;
    if (update.hasMessage()) {
      message = update.getMessage();
    } else if (update.hasEditedMessage()) {
      message = update.getEditedMessage();
    } else if (update.hasCallbackQuery()) {
      message = update.getCallbackQuery().getMessage();
    } else if (update.hasChannelPost()) {
      message = update.getChannelPost();
    } else if (update.hasEditedChannelPost()) {
      message = update.getEditedChannelPost();
    }

    return message;
  }

  /**
   * If the user has a username, returns a t.me link of the user.
   *
   * @param user the target user
   * @return t.me link or null
   */
  public static String getUserLink(User user) {
    if (user.getUserName() != null) {
      return "https://t.me/" + user.getUserName();
    }
    return null;
  }

  /**
   * If the chat has a username, returns a t.me link of the chat.
   *
   * @param chat the target chat
   * @return t.me link or null
   */
  public static String getChatLink(Chat chat) {
    if (chat.getUserName() != null) {
      return "https://t.me/" + chat.getUserName();
    }
    return null;
  }

  /**
   * If the chat of the message is not a private chat or normal group, returns a t.me link of the
   * message.
   *
   * @param message the target message
   * @return t.me link or null
   */
  public static String getMessageLink(Message message) {
    final Chat chat = message.getChat();
    if (!chat.isUserChat() && !chat.isGroupChat()) {
      String toLink;
      if (chat.getUserName() != null) {
        toLink = chat.getUserName();
      } else {
        // Get rid of leading -100 for supergroups
        toLink = "c/" + chat.getId().toString().substring(4);
      }
      return String.format("https://t.me/%s/%s", toLink, message.getMessageId());
    }
    return null;
  }

  public static InlineKeyboardButton buildButton(String text, String callback) {
    return InlineKeyboardButton.builder().text(text).callbackData(callback).build();
  }

  public static InlineKeyboardMarkup getOneButtonBoard(String text, String callback) {
    final List<InlineKeyboardButton> button = singletonList(buildButton(text, callback));
    return InlineKeyboardMarkup.builder().keyboardRow(button).build();
  }

  public static InlineKeyboardMarkup getManyOneButtonBoard(Map<String, String> buttons) {
    InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

    for (Entry<String, String> button : buttons.entrySet()) {
      keyboardBuilder.keyboardRow(
          List.of(Utils.buildButton(button.getKey(), button.getValue()))
      );
    }

    return keyboardBuilder.build();
  }

  @SafeVarargs
  public static Predicate<Update> all(Predicate<Update>... predicates) {
    return Arrays.stream(predicates).reduce(x -> true, Predicate::and);
  }

  @SafeVarargs
  public static Predicate<Update> or(Predicate<Update>... predicates) {
    return Arrays.stream(predicates).reduce(x -> true, Predicate::or);
  }

  public static String formatUrlAsTag(String url, String text) {
    return format("<a href=\"%s\">%s</a>", url, text);
  }

  public static String toHtmlTag(MessageEntity entity) {
    String start = getStartTag(entity);
    String end = getEndTag(entity.getType());
    return start + entity.getText() + end;
  }

  private static String getStartTag(MessageEntity entity) {
    switch (entity.getType()) {
      case EntityType.BOLD:
        return "<b>";
      case EntityType.ITALIC:
        return "<i>";
      case "underline":
        return "<u>";
      case "strikethrough":
        return "<s>";
      case EntityType.CODE:
        return "<code>";
      case EntityType.PRE:
        return "<pre>";
      case EntityType.TEXTLINK:
        return "<a href=\"" + entity.getUrl() + "\">";
      case EntityType.MENTION:
        return "<a href=\"https://t.me/" + entity.getText().substring(1) + "\">";
      case EntityType.URL:
        return "<a href=\"" + entity.getText() + "\">";
    }
    return "";
  }

  private static String getEndTag(String entityType) {
    switch (entityType) {
      case EntityType.BOLD:
        return "</b>";
      case EntityType.ITALIC:
        return "</i>";
      case "underline":
        return "</u>";
      case "strikethrough":
        return "</s>";
      case EntityType.CODE:
        return "</code>";
      case EntityType.PRE:
        return "</pre>";
      case EntityType.TEXTLINK:
      case EntityType.MENTION:
      case EntityType.URL:
        return "</a>";
    }
    return "";
  }

  public static Predicate<Update> getFilterSendBySomeUsers(final List<Long> chatIds) {
    return update -> chatIds.isEmpty() || chatIds.contains(getEffectiveUser(update).getId());
  }

  public static Predicate<Update> getFilterSendByExactUsers(final List<Long> chatIds) {
    return update -> chatIds.contains(getEffectiveUser(update).getId());
  }

  public static InlineKeyboardMarkup getYesNoKeyboard() {
    return InlineKeyboardMarkup.builder()
        .keyboardRow(asList(
            buildButton("Yes", "yes"),
            buildButton("No", "no")
        ))
        .build();
  }

  public interface Filters {

    Predicate<Update> privateChat = update -> getEffectiveChat(update).isUserChat();
    Predicate<Update> channel = update -> getEffectiveMessage(update).isChannelMessage();
    Predicate<Update> group = update -> getEffectiveChat(update).isGroupChat();
    Predicate<Update> supergroup = update -> getEffectiveChat(update).isSuperGroupChat();
    Predicate<Update> groups = update -> {
      final Chat chat = getEffectiveChat(update);
      return chat.isGroupChat() || chat.isSuperGroupChat();
    };
    Predicate<Update> command = update -> getEffectiveMessage(update).isCommand();
    Predicate<Update> notCommand = command.negate();
    Predicate<Update> forwarded = update -> getEffectiveMessage(update).getForwardDate() != null;
    Predicate<Update> video = update -> getEffectiveMessage(update).hasVideo();
    Predicate<Update> videoNote = update -> getEffectiveMessage(update).hasVideoNote();
    Predicate<Update> photo = update -> getEffectiveMessage(update).hasPhoto();
    Predicate<Update> text = update -> getEffectiveMessage(update).hasText();
    Predicate<Update> entities = update -> getEffectiveMessage(update).hasEntities();
    Predicate<Update> newMembers = update -> !getEffectiveMessage(update).getNewChatMembers()
        .isEmpty();
  }
}
