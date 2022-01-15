package me.damian.ciepiela.recipes.ingredient;

import me.damian.ciepiela.recipes.recipe.Recipe;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Ingredient {

    @Id private String id;

    @Indexed(unique = true)
    private String name;

    @Field("time_created")
    private LocalDateTime timeCreated;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'ingredient_ids':?#{#self._id} }")
    private List<Recipe> recipes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
