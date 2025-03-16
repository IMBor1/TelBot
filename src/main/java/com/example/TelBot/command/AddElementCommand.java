package com.example.TelBot.command;

import com.example.TelBot.service.CategoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AddElementCommand extends AbstractCommand {
    private final CategoryService categoryService;

    public AddElementCommand(CategoryService categoryService) {
        super("/addElement");
        this.categoryService = categoryService;
    }

    @Override
    public String execute(Update update) {
        String[] args = getArguments(update);
        
        if (args.length == 0) {
            return "Использование: /addElement <название> или /addElement <родитель> <дочерний>";
        }
        
        try {
            if (args.length == 1) {
                categoryService.addCategory(args[0]);
                return "Категория успешно добавлена: " + args[0];
            } else {
                categoryService.addCategory(args[0], args[1]);
                return "Категория " + args[1] + " добавлена к родителю " + args[0];
            }
        } catch (IllegalArgumentException e) {
            return "Ошибка: " + e.getMessage();
        }
    }
} 