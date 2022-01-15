package me.damian.ciepiela.recipes.review;

import me.damian.ciepiela.recipes.review.dto.ReviewPostDto;
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
@RequestMapping("api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReview(@RequestBody @Valid ReviewPostDto reviewPostDto) {
        Review review = reviewService.createReview(reviewPostDto);
        return new ResponseEntity<>(review.getId(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Review> getReview(@PathVariable("id") @NotBlank String reviewId) {
        Review review = getFromService(reviewService::findById, reviewId);
        if (review == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping(value = "/get", params = {"count", "filter"})
    public ResponseEntity<Integer> getReviewsCountBy(
            @RequestParam("count") String countMethod,
            @RequestParam("filter") List<String> filters
    ) {
        if (countMethod.equals("-userReviews")) {
            if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            Integer reviewsCount = reviewService.findAllUserReviewsCount(filters.get(0));
            return new ResponseEntity<>(reviewsCount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    //localhost:8080/api/v1/reviews/get?filter=2022-01-13T17:00:00Z&filter=2022-01-15T18:00:00Z&sort=-createdAt&page=1&perPage=20
    @GetMapping(value = "/get", params = {"sort", "filter"})
    public ResponseEntity<List<Review>> getReviewsBy(
            @RequestParam("sort") @NotBlank String sortMethod,
            @RequestParam("filter") @NotEmpty List<String> filters,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "perPage", required = false) String perPage
    ) {
        switch (sortMethod) {
            case "-userId":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllUserReviewsByUserId(filters.get(0));
            case "-userEmail":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllUserReviewsByEmail(filters.get(0));
            case "-recipeId":
                if (filters.size() != 1) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllRecipeReviewsByRecipeId(filters.get(0));
            case "-recipeIdStarValueWithin":
                if (filters.size() != 3) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getAllRecipeReviewsByRecipeIdAndStarValueBetween(
                        filters.get(0),
                        Float.parseFloat(filters.get(1)),
                        Float.parseFloat(filters.get(2))
                );
            case "-recipeIdStarValue":
                if (filters.size() != 2) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getReviewsByRecipeIdAndStarValue(
                        filters.get(0),
                        Float.parseFloat(filters.get(1))
                );
            case "-starValueWithin":
                if (filters.size() != 2) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getReviewsByStarValueWithin(
                        Float.parseFloat(filters.get(0)),
                        Float.parseFloat(filters.get(1))
                );
            case "-createdAt":
                if (filters.size() != 2) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                return getReviewsByCreatedWithin(
                        Date.from(Instant.parse(filters.get(0))),
                        Date.from(Instant.parse(filters.get(1)))
                );
            default:
                return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
        }
    }

    public ResponseEntity<List<Review>> getAllUserReviewsByUserId(String userId) {
        List<Review> queryResponse = getFromService(reviewService::findAllUserReviews, userId);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getAllUserReviewsByEmail(String userEmail) {
        List<Review> queryResponse = getFromService(reviewService::findReviewsByUserEmail, userEmail);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getAllRecipeReviewsByRecipeId(String recipeId) {
        List<Review> queryResponse = getFromService(reviewService::findByRecipeId, recipeId);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getAllRecipeReviewsByRecipeIdAndStarValueBetween(String recipeId, float floor, float ceiling) {
        List<Review> queryResponse = getFromService(reviewService::findByRecipeIdWithinStarValues, recipeId, floor, ceiling);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getReviewsByRecipeIdAndStarValue(String recipeId, float starValue) {
        List<Review> queryResponse = getFromService(reviewService::findByRecipeIdExactStarValue, recipeId, starValue);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getReviewsByStarValueWithin(float floor, float ceiling) {
        List<Review> queryResponse = getFromService(reviewService::findByStarValueBetween, floor, ceiling);
        return queryResponse != null ? new ResponseEntity<>(queryResponse, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Review>> getReviewsByCreatedWithin(Date startDate, Date endDate) {
        List<Review> queryResponse = getFromService(reviewService::findByTimeBetween, startDate, endDate);
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

    public <T, U, V, R> R getFromService(TriFunction<T, U, V, R> function, T firstArg, U secondArg, V thirdArg) {
        try {
            return function.apply(firstArg, secondArg, thirdArg);
        } catch (IllegalStateException e) {
            return null;
        }
    }

}
