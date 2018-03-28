package com.markaud.muscade.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity

@SqlResultSetMappings({
        @SqlResultSetMapping(
                name="sourceStatistic",
                columns= {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "itemCount", type = Integer.class)
                }
        )
})

@NamedNativeQuery(name = "Recipe.listSourceStatistic", query = "select source as name, count(id) as itemCount from recipe where LENGTH(source) > 0 group by source order by itemCount DESC ", resultSetMapping = "sourceStatistic")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    private Integer version;

    private String name;

    @Column(insertable = false, updatable = false)
    private Integer categoryId;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Lob
    @Column(length=2048)
    private String ingredient;
    @Lob
    @Column(length=2048)
    private String instruction;
    private String source;
    private Integer portion;
    private Integer preparationTime;
    private Integer cookingTime;
    private Integer rating;
    private Integer usageCount;
    private Date creationDate;
    private Date updateDate;

    public Recipe(Integer id, String name, Category category, String source, Integer rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.source = source;
        this.rating = rating;
    }

    public Recipe() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIngredient() {
        return ingredient;
    }

    public List<String> getIngredients() {
        return ingredient == null ? new ArrayList<>() : Arrays.asList(ingredient.split("\n"));
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getInstruction() {
        return instruction;
    }

    public List<String> getInstructions() {
        return instruction == null ? new ArrayList<>() : Arrays.asList(instruction.split("\n"));
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getStarCount() {
        return rating / 2;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
