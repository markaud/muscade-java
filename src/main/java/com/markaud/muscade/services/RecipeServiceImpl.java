package com.markaud.muscade.services;

import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.domain.ISourceStatistic;
import com.markaud.muscade.repositories.RecipeRepository;
import com.markaud.muscade.utils.FileUtils;
import com.markaud.muscade.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;
    private FileUtils fileUtils;

    @Autowired
    public void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Autowired
    public void setFileUtils(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Override
    public Iterable<Recipe> listAllRecipe() {
        return recipeRepository.findAll();
    }

    @Override
    public Iterable<Recipe> listAllRecipeBySearchCriteria(String query) {
        // FIXME use full text search engine
        query = query.toLowerCase();
        Iterable<Recipe> candidates = recipeRepository.findAll();
        Collection<Recipe> newCandidates = new ArrayList<>();
        for (String currentQuery : query.split(" ")) {
            for (Recipe recipe : candidates) {
                if (StringUtils.performString(recipe.getName()).toLowerCase().contains(currentQuery)
                        || StringUtils.performString(recipe.getIngredient()).toLowerCase().contains(currentQuery)
                        || StringUtils.performString(recipe.getInstruction()).toLowerCase().contains(currentQuery)
                        || StringUtils.performString(recipe.getSource()).toLowerCase().contains(currentQuery)) {
                    newCandidates.add(recipe);
                }
            }
            candidates = newCandidates;
            newCandidates = new ArrayList<>();
        }
        return candidates;
    }

    @Override
    public Iterable<Recipe> listAllRecipeBySource(String source) {
        return recipeRepository.findAllBySource(source);
    }

    @Override
    public Iterable<Recipe> listRecipeById(Collection<Integer> ids) {
        return recipeRepository.findAllById(ids);
    }

    @Override
    public Iterable<Recipe> listBestRecipe(Integer count) {
        Iterable<Recipe> recipeIterable = recipeRepository.findAll(new Sort(Sort.Direction.DESC,  "rating" ));
        ArrayList<Recipe> recipes = new ArrayList<>(count);
        int i=0;

        for (Recipe recipe : recipeIterable) {
            if (recipe.getRating() != null) {
                recipes.add(recipe);
                if (++i >= count) {
                    break;
                }
            }
        }

        return recipes;
    }

    @Override
    public Iterable<ISourceStatistic> listBestSources(Integer count) {
        Iterable<ISourceStatistic> statisticIterable = recipeRepository.listSourceStatistic();
        ArrayList<ISourceStatistic> result = new ArrayList<>(count);
        int i=0;

        for (ISourceStatistic ss : statisticIterable) {
            result.add(ss);
            if (++i >= count) {
                break;
            }
        }

        return result;
    }

    @Override
    public Optional<Recipe> getRecipeById(Integer id) {
        return recipeRepository.findById(id);
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipe(Integer id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Iterable<Recipe> importRecipes(InputStream inputStream) {
        Iterable<Recipe> recipes = fileUtils.loadRecipesFromFile(inputStream);
        recipeRepository.saveAll(recipes);
        return recipes;
    }

    @Override
    public Recipe getNewRecipe() {
        return new Recipe();
    }
}
