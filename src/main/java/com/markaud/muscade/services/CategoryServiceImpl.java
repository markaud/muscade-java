package com.markaud.muscade.services;

import com.markaud.muscade.domain.Category;
import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<Category> listAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Iterable<Recipe> listAllRecipeByCategory(Integer categoryId) {
        return getCategoryById(categoryId).map(Category::getRecipes).orElse(Collections.emptyList());
    }
}
