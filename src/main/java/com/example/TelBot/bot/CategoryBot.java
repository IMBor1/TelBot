package com.example.TelBot.bot;

import com.example.TelBot.command.CommandHandler;
import com.example.TelBot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Основной класс Telegram бота для управления категориями
 */
@Component
@RequiredArgsConstructor
public class CategoryBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final CommandHandler commandHandler;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            try {
                String response = commandHandler.handle(update);
                sendMessage(chatId, response);
            } catch (Exception e) {
                sendMessage(chatId, "Произошла ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Отправляет сообщение пользователю
     *
     * @param chatId идентификатор чата
     * @param text текст сообщения
     */
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