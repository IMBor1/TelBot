package com.example.TelBot.service;

import com.example.TelBot.model.Category;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelService {
    private final CategoryService categoryService;

    public byte[] exportToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Categories");
            
            // Создаем заголовки
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Parent");

            // Заполняем данные
            List<Category> categories = categoryService.getAllCategories();
            int rowNum = 1;
            for (Category category : categories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(category.getId());
                row.createCell(1).setCellValue(category.getName());
                if (category.getParent() != null) {
                    row.createCell(2).setCellValue(category.getParent().getName());
                }
            }

            // Автоматически регулируем ширину столбцов
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public void importFromExcel(byte[] fileBytes) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileBytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Category> categoryMap = new HashMap<>();
            Map<String, String> parentChildMap = new HashMap<>();

            // Пропускаем заголовок
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            // Первый проход: создаем все категории
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String name = row.getCell(1).getStringCellValue();
                String parentName = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null;

                if (!categoryMap.containsKey(name)) {
                    Category category = categoryService.addCategory(name);
                    categoryMap.put(name, category);
                }

                if (parentName != null && !parentName.isEmpty()) {
                    parentChildMap.put(name, parentName);
                }
            }

            // Второй проход: устанавливаем связи родитель-потомок
            for (Map.Entry<String, String> entry : parentChildMap.entrySet()) {
                String childName = entry.getKey();
                String parentName = entry.getValue();

                Category child = categoryMap.get(childName);
                Category parent = categoryMap.get(parentName);

                if (parent != null && child != null) {
                    categoryService.addCategory(parentName, childName);
                }
            }
        }
    }
} 