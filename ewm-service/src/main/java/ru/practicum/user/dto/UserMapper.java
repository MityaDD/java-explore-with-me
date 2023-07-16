package ru.practicum.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public static User toUserFromRequest(NewUserRequest userRequest) {
        return User.builder()
                .id(userRequest.getId())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
