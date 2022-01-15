package me.damian.ciepiela.recipes.review;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Document
public class Review {

    @Id private String id;

    @Field("star_value")
    private float starValue;

    @NotBlank
    private String description;

    @Field("time_created")
    private LocalDateTime timeCreated;

    @Field("recipe_id")
    private ObjectId recipeId;

    @Field("user_id")
    private ObjectId userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public ObjectId getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(ObjectId recipeId) {
        this.recipeId = recipeId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
