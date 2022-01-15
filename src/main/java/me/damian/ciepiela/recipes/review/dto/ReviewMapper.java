package me.damian.ciepiela.recipes.review.dto;

import me.damian.ciepiela.recipes.review.Review;
import me.damian.ciepiela.recipes.utils.dto.DtoTranslator;
import me.damian.ciepiela.recipes.utils.dto.StringIdToObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = DtoTranslator.class
)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "userId", qualifiedBy = StringIdToObjectId.class)
    @Mapping(target = "recipeId", qualifiedBy = StringIdToObjectId.class)
    Review reviewPostDtoToReview(ReviewPostDto reviewPostDto);

}
