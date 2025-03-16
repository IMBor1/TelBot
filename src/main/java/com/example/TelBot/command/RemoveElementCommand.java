package com.example.TelBot.command;

import com.example.TelBot.service.CategoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component

public class RemoveElementCommand extends AbstractCommand {
    private final CategoryService categoryService;

    public RemoveElementCommand(CategoryService categoryService) {
        super("/removeElement");
        this.categoryService = categoryService;
    }

    @Override
    public String execute(Update update) {
        String[] args = getArguments(update);
        
        if (args.length == 0) {
            return "Использование: /removeElement <название>";
        }
        
        try {
            categoryService.removeCategory(args[0]);
            return "Категория успешно удалена: " + args[0];
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        }
    }
} 