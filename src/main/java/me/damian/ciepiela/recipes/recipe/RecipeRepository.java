package me.damian.ciepiela.recipes.recipe;

import com.mongodb.BasicDBObject;
import me.damian.ciepiela.recipes.review.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface RecipeRepository extends MongoRepository<Recipe, String> {

    // db.recipe.find({_id: ObjectId('61e052730cb2fa676f24b752')})
    @Query("{'_id': ObjectId(?0)}")
    Optional<Recipe> findById(String userId);

    // db.recipe.find({user_id: ObjectId('61e0522e509506174a5bdfe2')})
    @Query("{'user_id': ObjectId(?0)}")
    List<Recipe> findByUserId(String userId);

    // db.recipe.find({time_created: { $gte: new Date("2022-01-13T17:00:00Z"), $lte: new Date("2022-01-13T18:00:00Z")} })
    @Query("{time_created: { $gte: ?0, $lte: ?1} }")
    List<Recipe> findByTimeCreatedWithin(Date floor, Date ceiling);

    // db.recipe.find({'steps': {$size: 2}})
    @Query("{'steps': {$size: ?0}}")
    List<Recipe> findByNumberOfSteps(int numberOfSteps);

    // db.recipe.find({'ingredient_ids': {$in: [ObjectId('61e05232509506174a5bdfe3')]}})
    @Query("{'ingredient_ids': {$in: ?0}}")
    List<Recipe> findByIngredientIds(ObjectId... ingredientsId);

    /* db.recipe.aggregate(
            {$lookup: {from: 'users', localField: 'user_id', foreignField: '_id', as: 'user'}},
            {$match : {'user.email': "test2@gmail.com" }},
            {$project: {'user': 0}}
        )
     */
    @Aggregation(pipeline = {
            "{$lookup: {from: 'users', localField: 'user_id', foreignField: '_id', as: 'user'}}",
            "{$match : {'user.email': ?0 }}",
            "{$project: {'user': 0}}"
    })
    List<Recipe> findByUserEmail(String email);

    /* db.recipe.aggregate(
         {$lookup: {from: 'review', localField: '_id', foreignField: 'recipe_id', as: 'reviews'}},
         {$match : {'_id': ObjectId('61e06591ba30430f0ead03e3') }},
         {$project: {'_id': 0, 'avg': {$avg: '$reviews.star_value'}}}
     )
  */
    @Aggregation(pipeline = {
            "{$lookup: {from: 'review', localField: '_id', foreignField: 'recipe_id', as: 'reviews'}}",
            "{$match : {'_id': ObjectId(?0) }}",
            "{$project: {'_id': 0, 'avg': {$avg: '$reviews.star_value'}}}",
    })
    Optional<BasicDBObject> findAvgStarValueForRecipe(String recipeId);

}
