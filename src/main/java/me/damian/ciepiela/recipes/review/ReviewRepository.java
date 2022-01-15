package me.damian.ciepiela.recipes.review;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    // db.review.find({ user_id: ObjectId('61e06582ba30430f0ead03e1') })
    @Query("{ user_id: ObjectId(?0) }")
    List<Review> findByUserId(String userId);

    // db.review.find({ star_value: { $gte: 1.5, $lte: 2 } })
    @Query("{ star_value: { $gte: ?0, $lte: ?1 } }")
    List<Review> findByStarValueWithin(float floor, float ceiling);

    // db.review.find({time_created: { $gte: new Date("2022-01-13T17:00:00Z"), $lte: new Date("2022-01-13T18:00:00Z")} })
    @Query("{time_created: { $gte: ?0, $lte: ?1} }")
    List<Review> findByTimeCreatedWithin(Date floor, Date ceiling);

    // db.review.aggregate({$match: {"user_id": ObjectId("61e06582ba30430f0ead03e1")}}, {$count: "user_reviews"})
    @Query(value = "{ user_id: ObjectId(?0) }", count = true)
    int findUserReviewCount(String userId);

    /* db.review.aggregate(
            {$lookup: {from: 'users', localField: 'user_id', foreignField: '_id', as: 'user'}},
            {$match : {'user.email': 'test2@gmail.com' }},
            {$project: {'user': 0}}
        )
    */
    @Aggregation(pipeline = {
            "{$lookup: {from: 'users', localField: 'user_id', foreignField: '_id', as: 'user'}}",
            "{$match : {'user.email': ?0 }}",
            "{$project: {'user': 0}}",
    })
    List<Review> findReviewsByUserEmail(String email);

    // db.review.find({'recipe_id': ObjectId('61e06591ba30430f0ead03e3')})
    @Query("{'recipe_id': ObjectId(?0)}")
    List<Review> findReviewsByRecipeId(String recipeId);

    // db.review.find({'recipe_id': ObjectId('61e06591ba30430f0ead03e3'), 'star_value': {$gte: 1.5, $lte: 2}})
    @Query("{'recipe_id': ObjectId(?0), 'star_value': {$gte: ?1, $lte: ?2}}")
    List<Review> findReviewsByRecipeIdWithinStarValues(String recipeId, float floor, float ceiling);

    // db.review.find({'recipe_id': ObjectId('61e06591ba30430f0ead03e3'), 'star_value': 1.5})
    @Query("{'recipe_id': ObjectId(?0), 'star_value': ?1}")
    List<Review> findReviewsByRecipeIdWithExactStarValue(String recipeId, float starValue);

}
