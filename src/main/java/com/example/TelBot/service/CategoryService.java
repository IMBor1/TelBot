package com.example.TelBot.service;

import com.example.TelBot.model.Category;
import com.example.TelBot.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public String buildTreeView() {
        List<Category> rootCategories = categoryRepository.findAll().stream()
                .filter(category -> category.getParent() == null)
                .toList();
        
        StringBuilder tree = new StringBuilder();
        for (Category root : rootCategories) {
            buildTreeRecursive(root, "", tree);
        }
        return tree.toString();
    }

    private void buildTreeRecursive(Category category, String prefix, StringBuilder tree) {
        tree.append(prefix).append("└── ").append(category.getName()).append("\n");
        String childPrefix = prefix + "    ";
        for (Category child : category.getChildren()) {
            buildTreeRecursive(child, childPrefix, tree);
        }
    }
} 