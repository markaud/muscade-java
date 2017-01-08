package com.markaud.muscade.utils;

import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class FileUtils {
    private static final String LINE_RETURN = "\r\n";
    private static final String EMPTY_LINE_ESCAPE = "\\";

    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private interface ParserState {
        ParserState next();

        ParserState populateRecipeFromLine(Recipe recipe, String line);

        boolean isNewRecipe();

    }

    private class RecipeNameState implements ParserState {
        @Override
        public ParserState next() {
            return new RecipeInfoState();
        }

        @Override
        public ParserState populateRecipeFromLine(Recipe recipe, String line) {
            if (line != null) {
                recipe.setName(line.trim());
            }
            return next();
        }

        @Override
        public boolean isNewRecipe() {
            return true;
        }


    }

    private class RecipeInfoState implements ParserState {
        @Override
        public ParserState next() {
            return new IngredientState();
        }

        @Override
        public ParserState populateRecipeFromLine(Recipe recipe, String line) {
            if (line != null) {
                String[] values = line.trim().split("\t");
                recipe.setCategory(categoryService.getCategoryById(Integer.parseInt(values[0])));
                recipe.setSource(values[1]);

                try {
                    recipe.setRating(Integer.parseInt(values[2]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                    //ignore
                }
                try {
                    recipe.setPreparationTime(Integer.parseInt(values[3]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                    //ignore
                }
                try {
                    recipe.setCookingTime(Integer.parseInt(values[4]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                    //ignore
                }
                try {
                    recipe.setPortion(Integer.parseInt(values[5]));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                    //ignore
                }
            }
            return this;
        }

        @Override
        public boolean isNewRecipe() {
            return false;
        }
    }

    private class IngredientState implements ParserState {
        @Override
        public ParserState next() {
            return new InstructionsState();
        }

        @Override
        public ParserState populateRecipeFromLine(Recipe recipe, String line) {
            if (line != null) {
                recipe.setIngredient(recipe.getIngredient() + (line.trim().equals(EMPTY_LINE_ESCAPE) ? "" : line.trim()) + LINE_RETURN);
            }
            return this;
        }

        @Override
        public boolean isNewRecipe() {
            return false;
        }
    }

    private class InstructionsState implements ParserState {
        @Override
        public ParserState next() {
            return new RecipeNameState();
        }

        @Override
        public ParserState populateRecipeFromLine(Recipe recipe, String line) {
            if (line != null) {
                recipe.setInstruction(recipe.getInstruction() + (line.trim().equals(EMPTY_LINE_ESCAPE) ? "" : line.trim()) + LINE_RETURN);
            }
            return this;
        }

        @Override
        public boolean isNewRecipe() {
            return false;
        }
    }


    public Iterable<Recipe> loadRecipesFromFile(InputStream inputStream) {
        ArrayList<Recipe> result = new ArrayList<>();
        ParserState state = new RecipeNameState();
        Recipe recipe = new Recipe();
        recipe.setIngredient("");
        recipe.setInstruction("");
        boolean blankLine = true;

        Scanner s = new Scanner(inputStream).useDelimiter(LINE_RETURN);

        while (s.hasNext()) {
            String line = s.nextLine();
            if (line.isEmpty()) {
                if (!blankLine) {
                    state = state.next();
                    if (state.isNewRecipe() && recipe.getName() != null) {
                        // we add the current recipe to the list before creating a new one
                        recipe.setIngredient(recipe.getIngredient().trim());
                        recipe.setInstruction(recipe.getInstruction().trim());
                        result.add(recipe);
                        recipe = new Recipe();
                        recipe.setIngredient("");
                        recipe.setInstruction("");
                    }
                }
                blankLine = true;
            } else {
                blankLine = false;
                state = state.populateRecipeFromLine(recipe, line);
            }
        }

        // save the last recipe
        if (recipe.getName() != null) {
            recipe.setIngredient(recipe.getIngredient().trim());
            recipe.setInstruction(recipe.getInstruction().trim());
            result.add(recipe);
        }

        return result;
    }

    public static String exportRecipes(Iterable<Recipe> recipes) {
        StringBuilder result = new StringBuilder();

        for (Recipe recipe : recipes) {
            result.append(StringUtils.performString(recipe.getName()));
            result.append(LINE_RETURN);
            result.append(recipe.getCategoryId());
            result.append("\t");
            result.append(StringUtils.performString(recipe.getSource()));
            result.append("\t");
            result.append(StringUtils.performString(recipe.getRating()));
            result.append("\t");
            result.append(StringUtils.performString(recipe.getPreparationTime()));
            result.append("\t");
            result.append(StringUtils.performString(recipe.getCookingTime()));
            result.append("\t");
            result.append(StringUtils.performString(recipe.getPortion()));
            result.append(LINE_RETURN);
            result.append(LINE_RETURN);
            for (String line : recipe.getIngredients()) {
                result.append(StringUtils.performString(line, EMPTY_LINE_ESCAPE).trim());
                result.append(LINE_RETURN);
            }
            result.append(LINE_RETURN);
            for (String line : recipe.getInstructions()) {
                result.append(StringUtils.performString(line, EMPTY_LINE_ESCAPE).trim());
                result.append(LINE_RETURN);
            }
            result.append(LINE_RETURN);
        }
        return result.toString();
    }
}
