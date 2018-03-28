package com.markaud.muscade.services;

import com.markaud.muscade.domain.Category;
import com.markaud.muscade.domain.Recipe;

import java.util.Optional;

public interface CategoryService {
    Iterable<Category> listAllCategory();
    Optional<Category> getCategoryById(Integer id);
    Iterable<Recipe> listAllRecipeByCategory(Integer categoryId);
}
