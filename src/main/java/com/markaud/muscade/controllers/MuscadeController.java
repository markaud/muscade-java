package com.markaud.muscade.controllers;

import com.markaud.muscade.domain.Category;
import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.services.CategoryService;
import com.markaud.muscade.services.RecipeService;
import com.markaud.muscade.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class MuscadeController {
    private RecipeService recipeService;
    private CategoryService categoryService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("cats")
    // always inject the categories in the pages because they are used in the navigation menu
    public Iterable<Category> getCategories() {
        return categoryService.listAllCategory();
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("bests", recipeService.listBestRecipe(5));
        model.addAttribute("sources", recipeService.listBestSources(5));
        return "home";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    private Iterable<Number> getIds(Iterable<Recipe> recipes) {
        ArrayList<Number> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            result.add(recipe.getId());
        }
        return result;
    }

    @RequestMapping("/list")
    public String list(Model model) {
        Iterable<Recipe> recipes = recipeService.listAllRecipe();
        model.addAttribute("recipes", recipes);
        model.addAttribute("ids", getIds(recipes));
        model.addAttribute("showSource", "1");
        model.addAttribute("showCategory", "1");
        return "list";
    }

    @RequestMapping("/category/{id}")
    public String category(Model model, @PathVariable Integer id) {
        Iterable<Recipe> recipes = categoryService.listAllRecipeByCategory(id);
        model.addAttribute("recipes", recipes);
        model.addAttribute("ids", getIds(recipes));
        model.addAttribute("showSource", "1");
        model.addAttribute("title", categoryService.getCategoryById(id).map(Category::getName).orElse(""));
        return "list";
    }

    @RequestMapping("/source/{name}")
    public String source(Model model, @PathVariable String name) {
        Iterable<Recipe> recipes = recipeService.listAllRecipeBySource(name);
        model.addAttribute("recipes", recipes);
        model.addAttribute("ids", getIds(recipes));
        model.addAttribute("showCategory", "1");
        model.addAttribute("title", name);
        return "list";
    }

    @RequestMapping("/search")
    public String search(Model model, @RequestParam(value = "query") String query) {
        Iterable<Recipe> recipes = recipeService.listAllRecipeBySearchCriteria(query);
        model.addAttribute("recipes", recipes);
        model.addAttribute("showSource", "1");
        model.addAttribute("ids", getIds(recipes));
        model.addAttribute("showCategory", "1");
        model.addAttribute("title", query);
        return "list";
    }

    @RequestMapping("/recipe/{id}")
    public String viewRecipe(Model model, @PathVariable int id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipes", Collections.singletonList(recipe.get()));
            return "view_recipe_page";
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/fs/{id}")
    public String viewRecipeFullScreen(Model model, @PathVariable int id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "view_recipe_fs";
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping("/multiple/{ids}")
    public String viewMultiple(Model model, @PathVariable List<Integer> ids) {
        model.addAttribute("recipes", recipeService.listRecipeById(ids));
        model.addAttribute("multiple", "1");
        return "view_recipe_page";
    }

    @RequestMapping(value = "/export/{ids}", produces = "text/plain;charset=utf-8")
    public void export(HttpServletResponse response, @PathVariable List<Integer> ids) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=export.txt");
        ServletOutputStream out = response.getOutputStream();
        out.write(FileUtils.exportRecipes(recipeService.listRecipeById(ids)).getBytes());
        out.flush();
        out.close();
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        return "import";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        Iterable<Recipe> recipes = recipeService.importRecipes(file.getInputStream());

        model.addAttribute("recipes", recipes);
        model.addAttribute("showSource", "1");
        model.addAttribute("showCategory", "1");
        model.addAttribute("title", "Résultat de l'importation");
        model.addAttribute("ids", getIds(recipes));
        return "list";
    }

    @RequestMapping("/edit/{id}")
    public String editRecipe(Model model, @PathVariable int id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "edit";
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/add")
    public String addRecipe(Model model) {
        model.addAttribute("recipe", recipeService.getNewRecipe());
        return "edit";
    }

    @RequestMapping(value = "recipe", method = RequestMethod.POST)
    public String saveRecipe(Recipe recipe) {
        recipeService.saveRecipe(recipe);
        return "redirect:/recipe/" + recipe.getId();
    }

}
