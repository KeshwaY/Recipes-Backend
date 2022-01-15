package me.damian.ciepiela.recipes.ingredient;


import me.damian.ciepiela.recipes.ingredient.dto.IngredientGetDto;
import me.damian.ciepiela.recipes.ingredient.dto.IngredientMapper;
import me.damian.ciepiela.recipes.ingredient.dto.IngredientPostDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper mapper;

    public IngredientService(IngredientRepository ingredientService, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientService;
        this.mapper = ingredientMapper;
    }

    public Ingredient createIngredient(IngredientPostDto ingredientPostDto) {
        Ingredient ingredient = mapper.ingredientPostDtoToIngredient(ingredientPostDto);
        ingredient.setTimeCreated(LocalDateTime.now());
        ingredient.setRecipes(new ArrayList<>());
        return ingredientRepository.save(ingredient);
    }

    public IngredientGetDto getIngredient(String ingredientId) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
        if (ingredient.isEmpty()) {
            return null;
        }
        return mapper.ingredientToIngredientGetDto(ingredient.get());
    }

}
