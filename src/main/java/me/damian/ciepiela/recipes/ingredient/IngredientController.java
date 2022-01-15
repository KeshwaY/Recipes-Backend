package me.damian.ciepiela.recipes.ingredient;


import me.damian.ciepiela.recipes.ingredient.dto.IngredientGetDto;
import me.damian.ciepiela.recipes.ingredient.dto.IngredientPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createIngredient(@RequestBody @Valid IngredientPostDto ingredientPostDto) {
        return new ResponseEntity<>(service.createIngredient(ingredientPostDto).getId(), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<IngredientGetDto> getIngredient(@PathVariable("id") @NotBlank String ingredientId) {
        IngredientGetDto ingredient = service.getIngredient(ingredientId);
        if (ingredient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

}
