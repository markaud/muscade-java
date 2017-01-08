package com.markaud.muscade.repositories;

import com.markaud.muscade.domain.Recipe;
import com.markaud.muscade.domain.SourceStatistic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

@SqlResultSetMapping(
        name="sourceStatistic",
        classes={
                @ConstructorResult(
                        targetClass=SourceStatistic.class,
                        columns={
                                @ColumnResult(name="name"),
                                @ColumnResult(name="itemCount")
                        }
                )
        }
)
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer> {

    @Query(
            nativeQuery = true,
            value = "select * from recipe r where r.source = (:source)"

            )
    List<Recipe> findAllBySource(@Param("source") String source);

    List<SourceStatistic> listSourceStatistic();

}
