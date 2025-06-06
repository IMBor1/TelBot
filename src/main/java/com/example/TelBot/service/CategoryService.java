package com.example.TelBot.service;

import com.example.TelBot.model.Category;
import com.example.TelBot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category addCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Категория с таким именем уже существует");
        }
        return categoryRepository.save(new Category(name));
    }

    @Transactional
    public Category addCategory(String parentName, String childName) {
        Category parent = categoryRepository.findByName(parentName)
                .orElseThrow(() -> new IllegalArgumentException("Родительская категория не найдена"));

        if (categoryRepository.existsByName(childName)) {
            throw new IllegalArgumentException("Категория с таким именем уже существует");
        }

        Category child = new Category(childName);
        parent.addChild(child);
        return categoryRepository.save(child);
    }

    @Transactional
    public void removeCategory(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Строит текстовое представление дерева категорий
     *
     * @return строка с деревом категорий
     */
    @Transactional(readOnly = true)
    public String buildTreeView() {
        List<Category> rootCategories = categoryRepository.findAll().stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());

        if (rootCategories.isEmpty()) {
            return "Дерево категорий пусто";
        }

        StringBuilder result = new StringBuilder("Дерево категорий:\n");
        for (Category root : rootCategories) {
            buildTreeRecursive(root, "", result);
        }
        return result.toString();
    }

    private void buildTreeRecursive(Category category, String prefix, StringBuilder result) {
        result.append(prefix).append("└─ ").append(category.getName()).append("\n");
        String childPrefix = prefix + "   ";
        for (Category child : category.getChildren()) {
            buildTreeRecursive(child, childPrefix, result);
        }
    }
} 