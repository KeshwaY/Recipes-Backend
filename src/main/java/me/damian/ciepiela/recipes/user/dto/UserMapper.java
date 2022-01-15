package me.damian.ciepiela.recipes.user.dto;

import me.damian.ciepiela.recipes.user.User;
import me.damian.ciepiela.recipes.utils.dto.DtoTranslator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = DtoTranslator.class
)
public interface UserMapper {

    UserGetDto userToUserGetDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "recipes", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    User UserPostDtoToUser(UserPostDto userPostDto);

}
