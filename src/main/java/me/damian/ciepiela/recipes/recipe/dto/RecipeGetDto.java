package me.damian.ciepiela.recipes.recipe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class RecipeGetDto {

    private String name;

    private List<String> steps;

    @JsonProperty("time_created")
    private LocalDateTime timeCreated;

    @JsonProperty("user_id")
    private String user;

}
