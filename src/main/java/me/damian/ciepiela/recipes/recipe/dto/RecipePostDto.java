package me.damian.ciepiela.recipes.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RecipePostDto {

    @NotBlank
    private String name;

    @Size(min = 1, max = 50)
    private List<String> steps;

    @Size(min = 1, max = 50)
    @JsonProperty("ingredient_ids")
    private List<String> ingredients;

    @JsonProperty("user_id")
    @NotBlank
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
