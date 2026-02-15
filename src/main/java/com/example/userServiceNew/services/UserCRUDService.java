package com.example.userServiceNew.services;

import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.entity.User;
import com.example.userServiceNew.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCRUDService implements CRUDServiceUser<UserDto>{

    private final UserRepository userRepository;

    @Override
    public UserDto getById(Integer id) {
        log.info("Get by id " + id);
        return mapToDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserCRUDService::mapToDto)
                .toList();
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Create");
        User user = userRepository.save(mapToEntity(userDto));
        return mapToDto(user);
    }

    @Override
    public UserDto update(Integer id, UserDto userDto) {
        log.info("Update");
        userDto.setId(id);
        User user = userRepository.save(mapToEntity(userDto));
        return mapToDto(user);
    }

    @Override
    public void delete(Integer id) {
        log.info("Delete user " + id);
        userRepository.deleteById(id);
    }


    public static User mapToEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setNameUser(userDto.getNameUser());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setCreateAt(userDto.getCreateAt());
        return user;
    }

    public static UserDto mapToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setNameUser(user.getNameUser());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setCreateAt(user.getCreateAt());
        return userDto;
    }
}
