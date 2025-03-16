package com.example.TelBot.command;

import com.example.TelBot.service.CategoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ViewTreeCommand extends AbstractCommand {
    private final CategoryService categoryService;

    public ViewTreeCommand(CategoryService categoryService) {
        super("/viewTree");
        this.categoryService = categoryService;
    }

    @Override
    public String execute(Update update) {
        return categoryService.buildTreeView();
    }
} 