package com.ptit.shopapp.services.impl;

import com.ptit.shopapp.dtos.CategoryDTO;
import com.ptit.shopapp.models.Category;
import com.ptit.shopapp.repositories.CategoryRepository;
import com.ptit.shopapp.services.ICategoryService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional
  public Category createCategory(CategoryDTO categoryDTO) {
    Category newCategory = Category.builder()
        .name(categoryDTO.getName())
        .build();
    return categoryRepository.save(newCategory);
  }

  @Override
  public Category getCategoryById(long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("category not found"));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  @Transactional
  public Category updateCategory(long id, CategoryDTO categoryDTO) {
    Category existingCategory = getCategoryById(id);
    existingCategory.setName(categoryDTO.getName());
    categoryRepository.save(existingCategory);
    return existingCategory;
  }

  @Override
  @Transactional
  public void deleteCategory(long id) {
    categoryRepository.deleteById(id);
  }
}
