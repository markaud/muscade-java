package com.markaud.muscade.repositories;

import com.markaud.muscade.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}
