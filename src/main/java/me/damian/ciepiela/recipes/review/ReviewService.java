package me.damian.ciepiela.recipes.review;

import me.damian.ciepiela.recipes.recipe.Recipe;
import me.damian.ciepiela.recipes.recipe.RecipeRepository;
import me.damian.ciepiela.recipes.review.dto.ReviewMapper;
import me.damian.ciepiela.recipes.review.dto.ReviewPostDto;
import me.damian.ciepiela.recipes.user.User;
import me.damian.ciepiela.recipes.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, RecipeRepository recipeRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.reviewMapper = reviewMapper;
    }

    public Review findById(String id) {
        return reviewRepository.findById(id).orElseThrow(IllegalStateException::new);
    }

    @Transactional
    public Review createReview(ReviewPostDto reviewPostDto) {
        Recipe recipe = recipeRepository.findById(reviewPostDto.getRecipeId()).orElseThrow(IllegalAccessError::new);
        User user = userRepository.findById(reviewPostDto.getUserId()).orElseThrow(IllegalAccessError::new);

        Review review = reviewMapper.reviewPostDtoToReview(reviewPostDto);
        review.setTimeCreated(LocalDateTime.now());

        recipe.getReviews().add(review);
        recipeRepository.save(recipe);

        user.getReviews().add(review);
        userRepository.save(user);

        return reviewRepository.save(review);
    }

    public List<Review> findAllUserReviews(String userId) {
        List<Review> response = reviewRepository.findByUserId(userId);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public List<Review> findByStarValueBetween(float floor, float ceiling) {
        List<Review> response = reviewRepository.findByStarValueWithin(floor, ceiling);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public List<Review> findByTimeBetween(Date start_time, Date end_time) {
        List<Review> response = reviewRepository.findByTimeCreatedWithin(start_time, end_time);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public int findAllUserReviewsCount(String userId) {
        return reviewRepository.findUserReviewCount(userId);
    }

    public List<Review> findReviewsByUserEmail(String userEmail) {
        List<Review> response = reviewRepository.findReviewsByUserEmail(userEmail);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public List<Review> findByRecipeId(String recipeId) {
        List<Review> response = reviewRepository.findReviewsByRecipeId(recipeId);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public List<Review> findByRecipeIdWithinStarValues(String recipeId, float floor, float ceiling) {
        List<Review> response = reviewRepository.findReviewsByRecipeIdWithinStarValues(recipeId, floor, ceiling);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

    public List<Review> findByRecipeIdExactStarValue(String recipeId, float starValue) {
        List<Review> response = reviewRepository.findReviewsByRecipeIdWithExactStarValue(recipeId, starValue);
        if (response.size() == 0) {
            throw new IllegalStateException();
        }
        return response;
    }

}
