package com.example.TelBot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая категорию в дереве категорий.
 * Каждая категория может иметь родительскую категорию и список дочерних категорий.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    /**
     * Создает новую категорию с указанным именем.
     *
     * @param name имя категории
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Добавляет дочернюю категорию к текущей категории.
     *
     * @param child дочерняя категория для добавления
     */
    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    /**
     * Удаляет дочернюю категорию из текущей категории.
     *
     * @param child дочерняя категория для удаления
     */
    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }
} 