package com.ptit.shopapp.services;

import com.ptit.shopapp.dtos.CategoryDTO;
import com.ptit.shopapp.models.Category;
import java.util.List;

public interface ICategoryService {
  Category createCategory(CategoryDTO categoryDTO);
  Category getCategoryById(long id);
  List<Category> getAllCategories();
  Category updateCategory(long id, CategoryDTO categoryDTO);
  void deleteCategory(long id);
}
