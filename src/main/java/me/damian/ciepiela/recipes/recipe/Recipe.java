package me.damian.ciepiela.recipes.recipe;

import me.damian.ciepiela.recipes.review.Review;
import me.damian.ciepiela.recipes.user.User;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document
public class Recipe {

    @Id private String id;

    private String name;

    private List<String> steps;

    @Field("ingredient_ids")
    private List<ObjectId> ingredients;

    @Field(name = "time_created")
    private LocalDateTime timeCreated;

    @Field(name = "user_id")
    private ObjectId userId;

    @ReadOnlyProperty
    @DocumentReference(lookup="{'recipe_id':?#{#self._id} }")
    private List<Review> reviews;

    public Recipe() {}

    public Recipe(String id, String name, List<String> steps, List<ObjectId> ingredients, LocalDateTime timeCreated, ObjectId userId, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
        this.timeCreated = timeCreated;
        this.userId = userId;
        this.reviews = reviews;
    }

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

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public List<ObjectId> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ObjectId> ingredients) {
        this.ingredients = ingredients;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
