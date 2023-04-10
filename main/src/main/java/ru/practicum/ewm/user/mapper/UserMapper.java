package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User toUserModel(UserDto userModelDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtoList(List<User> usersList);
}