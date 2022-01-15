package me.damian.ciepiela.recipes.ingredient.dto;

import javax.validation.constraints.NotBlank;

public class IngredientPostDto {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
