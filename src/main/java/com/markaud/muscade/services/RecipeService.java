package com.markaud.muscade.services;

import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.domain.SourceStatistic;

import java.io.InputStream;
import java.util.Collection;

public interface RecipeService {
    Iterable<Recipe> listAllRecipe();
    Iterable<Recipe> listAllRecipeBySearchCriteria(String query);

    Iterable<Recipe> listAllRecipeBySource(String source);
    Iterable<Recipe> listRecipeById(Collection<Integer> ids);
    Iterable<Recipe> listBestRecipe(Integer count);

    Iterable<SourceStatistic> listBestSources(Integer count);

    Recipe getRecipeById(Integer id);

    Recipe saveRecipe(Recipe recipe);

    void deleteRecipe(Integer id);

    Iterable<Recipe> importRecipes(InputStream inputStream);

    Recipe getNewRecipe();
}
