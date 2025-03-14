package com.example.TelBot.bot;

import com.example.TelBot.config.BotConfig;
import com.example.telbot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class CategoryBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CategoryService categoryService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();

        try {
            String response = handleCommand(messageText);
            sendMessage(chatId, response);
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, "Ошибка: " + e.getMessage());
        }
    }

    private String handleCommand(String message) {
        String[] parts = message.split("\\s+", 3);
        String command = parts[0].toLowerCase();

        return switch (command) {
            case "/help" -> getHelpMessage();
            case "/viewtree" -> categoryService.buildTreeView();
            case "/addelement" -> {
                if (parts.length < 2) {
                    yield "Использование: /addElement <название> или /addElement <родитель> <дочерний>";
                }
                if (parts.length == 2) {
                    categoryService.addCategory(parts[1]);
                    yield "Категория успешно добавлена: " + parts[1];
                } else {
                    categoryService.addCategory(parts[1], parts[2]);
                    yield "Категория " + parts[2] + " добавлена к родителю " + parts[1];
                }
            }
            case "/removeelement" -> {
                if (parts.length < 2) {
                    yield "Использование: /removeElement <название>";
                }
                categoryService.removeCategory(parts[1]);
                yield "Категория успешно удалена: " + parts[1];
            }
            default -> "Неизвестная команда. Используйте /help для списка команд.";
        };
    }

    private String getHelpMessage() {
        return """
                Доступные команды:
                /viewTree - Просмотр дерева категорий
                /addElement <название> - Добавить корневую категорию
                /addElement <родитель> <дочерний> - Добавить дочернюю категорию
                /removeElement <название> - Удалить категорию
                /help - Показать это сообщение
                """;
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
} 