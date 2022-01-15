package me.damian.ciepiela.recipes.user;

import me.damian.ciepiela.recipes.user.dto.UserGetDto;
import me.damian.ciepiela.recipes.user.dto.UserMapper;
import me.damian.ciepiela.recipes.user.dto.UserPostDto;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserGetDto> getUser(@PathVariable("id") @NotBlank String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userMapper.userToUserGetDto(user), HttpStatus.OK);
    }

    @GetMapping(value = "/get", params = "email")
    public ResponseEntity<UserGetDto> getUserByEmail(
            @RequestParam(name = "email") String userEmail
    ) {
        try {
            User user = userService.findUserByEmail(userEmail);
            return new ResponseEntity<>(userMapper.userToUserGetDto(user), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserPostDto userPostDto) {
        try {
            User createdUser = userService.createUser(userMapper.UserPostDtoToUser(userPostDto));
            return new ResponseEntity<>(createdUser.getId(), HttpStatus.CREATED);
        } catch (IllegalStateException | IncorrectResultSizeDataAccessException e) {
            return new ResponseEntity<>("User email and username should be unique!", HttpStatus.BAD_REQUEST);
        }
    }

}
