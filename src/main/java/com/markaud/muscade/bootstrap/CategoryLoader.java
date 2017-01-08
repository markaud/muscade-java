package com.markaud.muscade.bootstrap;

import com.markaud.muscade.domain.Category;
import com.markaud.muscade.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CategoryLoader implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;

    private Logger log = Logger.getLogger(CategoryLoader.class.getName());

    @Autowired
    public void setProductRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (categoryRepository.count() == 0) {
            Category cat;

            cat = new Category();
            cat.setName("Entrées");
            categoryRepository.save(cat);
            log.info("Saved " + cat.getName() + " - id: " + cat.getId());

            cat = new Category();
            cat.setName("Repas principaux");
            categoryRepository.save(cat);
            log.info("Saved " + cat.getName() + " - id: " + cat.getId());

            cat = new Category();
            cat.setName("Desserts");
            categoryRepository.save(cat);
            log.info("Saved " + cat.getName() + " - id: " + cat.getId());

            cat = new Category();
            cat.setName("Accompagnements");
            categoryRepository.save(cat);
            log.info("Saved " + cat.getName() + " - id: " + cat.getId());

            cat = new Category();
            cat.setName("Autres");
            categoryRepository.save(cat);
            log.info("Saved " + cat.getName() + " - id: " + cat.getId());
        }

    }
}
