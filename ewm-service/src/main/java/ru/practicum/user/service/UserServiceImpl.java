package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.model.User;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserAdminService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        User user = UserMapper.toUserFromRequest(userRequest);
        log.info("Создан новый user");
        return UserMapper.toUserDto(repository.save(user));
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("Удален user c id={}", userId);
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Запрошен лист пользователей по параметрам ids={}", ids);
        return repository.findAllByAdmin(ids, PageRequest.of(from / size, size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
