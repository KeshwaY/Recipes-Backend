package me.damian.ciepiela.recipes.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class ReviewPostDto {

    @NotNull
    @DecimalMax(value = "5.0")
    @DecimalMin(value = "1.0")
    @JsonProperty("star_value")
    private float starValue;

    @NotBlank
    private String description;

    @NotBlank
    @JsonProperty("recipe_id")
    private String recipeId;

    @NotBlank
    @JsonProperty("user_id")
    private String userId;

    public float getStarValue() {
        return starValue;
    }

    public void setStarValue(float starValue) {
        this.starValue = starValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
