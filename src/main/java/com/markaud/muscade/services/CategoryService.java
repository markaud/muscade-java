package com.markaud.muscade.services;

import com.markaud.muscade.domain.Category;
import com.markaud.muscade.domain.Recipe;

public interface CategoryService {
    Iterable<Category> listAllCategory();
    Category getCategoryById(Integer id);
    Iterable<Recipe> listAllRecipeByCategory(Integer categoryId);
}
