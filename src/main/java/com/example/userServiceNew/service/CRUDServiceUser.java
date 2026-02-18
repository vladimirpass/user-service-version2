package com.example.userServiceNew.service;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.dto.UserResponseDto;

import java.util.Collection;

public interface CRUDServiceUser<T> {
    UserResponseDto getById(Integer id);
    Collection<UserResponseDto> getAll();
    Response create(T item);
    Response update(Integer id, T item);
    Response delete(Integer id);
}
