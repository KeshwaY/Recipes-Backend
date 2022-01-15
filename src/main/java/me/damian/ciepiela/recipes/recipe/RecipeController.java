package me.damian.ciepiela.recipes.recipe;

import me.damian.ciepiela.recipes.recipe.dto.RecipePostDto;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Validated
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRecipe(@RequestBody @Valid RecipePostDto recipePostDto) {
        Recipe response = service.createRecipe(recipePostDto);
        return new ResponseEntity<>(response.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") @NotBlank String reviewId) {
        Recipe recipe = getFromService(service::findById, reviewId);
        if (recipe == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @GetMapping(value = "/get", params = {"avg", "filter"})
    public ResponseEntity<Float> getReviewsCountBy(
            @RequestParam("avg") String avgMethod,
            @RequestParam("filter") List<String> filters
    ) {
        if (avgMethod.equals("-starValue")) {
            if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            Float recipeStarAvg = service.findAvgStarValueForRecipe(filters.get(0));
            return new ResponseEntity<>(recipeStarAvg, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(value = "/get", params = {"sort", "filter"})
    public ResponseEntity<List<Recipe>> getRecipeBy(
            @RequestParam("sort") @NotBlank String sortMethod,
            @RequestParam("filter") @NotEmpty List<String> filters,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "perPage", required = false) String perPage
    ) {
        switch (sortMethod) {
            case "-userId":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllUserRecipesByUserId(filters.get(0));
            case "-userEmail":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllUserRecipesByEmail(filters.get(0));
            case "-createdAt":
                if (filters.size() != 2) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllCreatedWithin(
                        Date.from(Instant.parse(filters.get(0))),
                        Date.from(Instant.parse(filters.get(1)))
                );
            case "-numberOfSteps":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                return getAllByNumbersOfStep(
                        Integer.parseInt(filters.get(0))
                );
            case "-ingredientIds":
                return getAllByIngredientIds(
                        filters.stream()
                                .map(ObjectId::new).toArray(ObjectId[]::new)
                );
            default:
                return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
        }
    }

    public ResponseEntity<List<Recipe>> getAllUserRecipesByUserId(String userId) {
        List<Recipe> queryResponse = getFromService(service::findByUserId, userId);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Recipe>> getAllUserRecipesByEmail(String userEmail) {
        List<Recipe> queryResponse = getFromService(service::findByUserEmail, userEmail);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Recipe>> getAllCreatedWithin(Date startDate, Date endDate) {
        List<Recipe> queryResponse = getFromService(service::findAllCreatedWithin, startDate, endDate);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Recipe>> getAllByNumbersOfStep(int numberOfSteps) {
        List<Recipe> queryResponse = getFromService(service::findAllByNumberOfSteps, numberOfSteps);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Recipe>> getAllByIngredientIds(ObjectId... ids) {
        List<Recipe> queryResponse = getFromService(service::findAllByIngredientIds, ids);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public <T, R> R getFromService(Function<T, R> function, T arg) {
        try {
            return function.apply(arg);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public <T, U, R> R getFromService(BiFunction<T, U, R> function, T firstArg, U secondArg) {
        try {
            return function.apply(firstArg, secondArg);
        } catch (IllegalStateException e) {
            return null;
        }
    }

}
