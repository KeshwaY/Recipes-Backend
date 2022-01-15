package me.damian.ciepiela.recipes.ingredient.dto;


import me.damian.ciepiela.recipes.ingredient.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface IngredientMapper {

    IngredientGetDto ingredientToIngredientGetDto(Ingredient ingredient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "recipes", ignore = true)
    Ingredient ingredientPostDtoToIngredient(IngredientPostDto ingredientPostDto);

}
