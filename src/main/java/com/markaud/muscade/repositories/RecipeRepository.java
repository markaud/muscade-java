package com.markaud.muscade.repositories;

import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.domain.ISourceStatistic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer> {

    @Query(
            nativeQuery = true,
            value = "select * from recipe r where r.source = (:source)"

            )
    List<Recipe> findAllBySource(@Param("source") String source);

    List<ISourceStatistic> listSourceStatistic();

}
