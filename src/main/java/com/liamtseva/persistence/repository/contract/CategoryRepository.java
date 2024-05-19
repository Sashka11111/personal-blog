package com.liamtseva.persistence.repository.contract;

import com.liamtseva.domain.exception.EntityNotFoundException;
import com.liamtseva.persistence.entity.Category;
import java.util.List;

public interface CategoryRepository {
  void addCategory(Category category) throws EntityNotFoundException;
  Category getCategoryById(int id) throws EntityNotFoundException;
  List<Category> getAllCategories();
  void updateCategory(Category category) throws EntityNotFoundException;
  void deleteCategory(int id) throws EntityNotFoundException;
}