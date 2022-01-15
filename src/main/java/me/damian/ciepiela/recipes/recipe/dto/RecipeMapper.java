package me.damian.ciepiela.recipes.recipe.dto;

import me.damian.ciepiela.recipes.recipe.Recipe;
import me.damian.ciepiela.recipes.utils.dto.DtoTranslator;
import me.damian.ciepiela.recipes.utils.dto.ListStringIdToListObjectId;
import me.damian.ciepiela.recipes.utils.dto.StringIdToObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = DtoTranslator.class
        )
public interface RecipeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "userId", qualifiedBy = StringIdToObjectId.class)
    @Mapping(target = "ingredients", qualifiedBy = ListStringIdToListObjectId.class)
    Recipe RecipePostDtoToRecipe(RecipePostDto recipePostDto);

}
