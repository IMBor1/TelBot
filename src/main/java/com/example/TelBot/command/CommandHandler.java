package com.example.TelBot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Обработчик команд бота
 */
@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final List<Command> commands;

    /**
     * Обрабатывает команду и возвращает ответ
     *
     * @param update объект с информацией о сообщении
     * @return текст ответа на команду
     */
    public String handle(Update update) {
        String messageText = update.getMessage().getText();
        
        return commands.stream()
                .filter(command -> command.canExecute(messageText))
                .findFirst()
                .map(command -> command.execute(update))
                .orElse("Неизвестная команда. Используйте /help для списка команд.");
    }
} 