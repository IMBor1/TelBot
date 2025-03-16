package com.example.TelBot.bot;

import com.example.TelBot.command.CommandHandler;
import com.example.TelBot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
                sendMessage(chatId, response, createKeyboard(update.getMessage().getText()));
            } catch (Exception e) {
                sendMessage(chatId, "Произошла ошибка: " + e.getMessage(), null);
            }
        }
    }

    /**
     * Создает клавиатуру с кнопками в зависимости от текущей команды
     *
     * @param command текущая команда
     * @return объект клавиатуры
     */
    private ReplyKeyboardMarkup createKeyboard(String command) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        if (command.equals("/help") || command.equals("/start")) {
            // Первый ряд с основными командами
            KeyboardRow row1 = new KeyboardRow();
            row1.add("/viewTree");
            row1.add("/help");
            keyboard.add(row1);

            // Второй ряд с примерами добавления
            KeyboardRow row2 = new KeyboardRow();
            row2.add("/addElement Продукты");
            row2.add("/addElement Продукты Фрукты");
            keyboard.add(row2);

            // Третий ряд с удалением
            KeyboardRow row3 = new KeyboardRow();
            row3.add("/removeElement Продукты");
            keyboard.add(row3);
        } else if (command.startsWith("/addElement") || command.startsWith("/removeElement")) {
            KeyboardRow row = new KeyboardRow();
            row.add("/help");
            row.add("/viewTree");
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setSelective(true);

        return keyboard.isEmpty() ? null : keyboardMarkup;
    }

    /**
     * Отправляет сообщение пользователю
     *
     * @param chatId идентификатор чата
     * @param text текст сообщения
     * @param keyboardMarkup клавиатура с кнопками (может быть null)
     */
    private void sendMessage(String chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        if (keyboardMarkup != null) {
            message.setReplyMarkup(keyboardMarkup);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
} 