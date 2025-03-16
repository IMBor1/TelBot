package com.example.TelBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс для реализации паттерна Command
 */
public interface Command {
    /**
     * Выполняет команду
     *
     * @param update объект с информацией о сообщении
     * @return текст ответа на команду
     */
    String execute(Update update);

    /**
     * Проверяет, может ли команда обработать данное сообщение
     *
     * @param commandText текст команды
     * @return true, если команда может обработать сообщение
     */
    boolean canExecute(String commandText);
} 