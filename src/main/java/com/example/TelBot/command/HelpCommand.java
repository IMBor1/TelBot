package com.example.TelBot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("/help");
    }

    @Override
    public String execute(Update update) {
        return """
                Доступные команды:
                /viewTree - Просмотр дерева категорий
                /addElement <название> - Добавить корневую категорию
                /addElement <родитель> <дочерний> - Добавить дочернюю категорию
                /removeElement <название> - Удалить категорию
                /help - Показать это сообщение
                """;
    }
} 