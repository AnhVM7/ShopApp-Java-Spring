package com.ptit.shopapp.controllers;

import com.ptit.shopapp.components.LocalizationUtil;
import com.ptit.shopapp.dtos.CategoryDTO;
import com.ptit.shopapp.models.Category;
import com.ptit.shopapp.responses.UpdateCategoryResponse;
import com.ptit.shopapp.services.impl.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private final LocalizationUtil localizationUtil;

  @PostMapping("")
  public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
      BindingResult result){
    if(result.hasErrors()){
      List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    categoryService.createCategory(categoryDTO);
    return ResponseEntity.ok("insert category " + categoryDTO + " successfully");
  }

  // hien thi tat ca categories
  @GetMapping("") //api/va/categories?page=1&limit=10
  public ResponseEntity<List<Category>> getAllCategories(
      @RequestParam("page") int page,
      @RequestParam("limit") int limit
  ){
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  // neu tham so truyen vao la 1 doi tuong thi phai cho vaof 1 class goi la dto

  @PutMapping("/{id}")
  public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable Long id,
      @Valid @RequestBody CategoryDTO categoryDTO){
    categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok(UpdateCategoryResponse.builder()
        .message(localizationUtil.getLocalizedMessage("category.update_category.update_successfully"))
        .build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id){
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("delete category with id = " + id + " successfully");
  }
}
