package me.damian.ciepiela.recipes.recipe;

import com.mongodb.BasicDBObject;
import me.damian.ciepiela.recipes.ingredient.Ingredient;
import me.damian.ciepiela.recipes.ingredient.IngredientRepository;
import me.damian.ciepiela.recipes.recipe.dto.RecipeMapper;
import me.damian.ciepiela.recipes.recipe.dto.RecipePostDto;
import me.damian.ciepiela.recipes.review.Review;
import me.damian.ciepiela.recipes.user.User;
import me.damian.ciepiela.recipes.user.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    private final RecipeMapper mapper;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, IngredientRepository ingredientRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.mapper = recipeMapper;
    }

    @Transactional
    public Recipe createRecipe(RecipePostDto recipePostDto) {
        User user = userRepository.findById(recipePostDto.getUserId()).orElseThrow(IllegalStateException::new);
        Recipe recipe = mapper.RecipePostDtoToRecipe(recipePostDto);

        List<Ingredient> ingredients = recipe.getIngredients().stream()
                        .map(id -> ingredientRepository.findById(id.toString()).get())
                        .collect(Collectors.toList());
        recipe.setReviews(new ArrayList<>());
        recipe.setTimeCreated(LocalDateTime.now());

        ingredients.forEach(ingredient -> {
            ingredient.getRecipes().add(recipe);
            ingredientRepository.save(ingredient);
        });

        user.getRecipes().add(recipe);
        userRepository.save(user);

        return recipeRepository.save(recipe);
    }

    public Recipe findById(String recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow(IllegalStateException::new);
    }

    public List<Recipe> findByUserId(String userId) {
        List<Recipe> reviews = recipeRepository.findByUserId(userId);
        if (reviews.size() == 0) {
            throw new IllegalStateException();
        }
        return reviews;
    }

    public List<Recipe> findAllCreatedWithin(Date floor, Date ceiling) {
        List<Recipe> reviews = recipeRepository.findByTimeCreatedWithin(floor, ceiling);
        if (reviews.size() == 0) {
            throw new IllegalStateException();
        }
        return reviews;
    }

    public List<Recipe> findAllByNumberOfSteps(int numberOfSteps) {
        List<Recipe> reviews = recipeRepository.findByNumberOfSteps(numberOfSteps);
        if (reviews.size() == 0) {
            throw new IllegalStateException();
        }
        return reviews;
    }

    public List<Recipe> findAllByIngredientIds(ObjectId... ids) {
        List<Recipe> reviews = recipeRepository.findByIngredientIds(
                ids
        );
        if (reviews.size() == 0) {
            throw new IllegalStateException();
        }
        return reviews;
    }

    public List<Recipe> findByUserEmail(String email) {
        List<Recipe> reviews = recipeRepository.findByUserEmail(email);
        if (reviews.size() == 0) {
            throw new IllegalStateException();
        }
        return reviews;
    }

    public Float findAvgStarValueForRecipe(String recipeId) {
        BasicDBObject dbo = recipeRepository.findAvgStarValueForRecipe(recipeId).orElseThrow(IllegalStateException::new);
        if (dbo.isEmpty() || !dbo.containsField("avg") || dbo.get("avg") == null) {
            return 0f;
        }
        return  Float.parseFloat(dbo.get("avg").toString());
    }

}
