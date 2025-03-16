package com.example.TelBot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Абстрактный класс с базовой функциональностью для команд
 */
public abstract class AbstractCommand implements Command {
    protected final String commandName;

    protected AbstractCommand(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public boolean canExecute(String commandText) {
        return commandText.startsWith(commandName) || 
               commandText.startsWith(commandName.toLowerCase());
    }

    /**
     * Получает аргументы команды из сообщения
     *
     * @param update объект с информацией о сообщении
     * @return массив аргументов команды
     */
    protected String[] getArguments(Update update) {
        String text = update.getMessage().getText();
        String[] parts = text.split("\\s+", 3);
        if (parts.length <= 1) {
            return new String[0];
        }
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);
        return args;
    }
} 