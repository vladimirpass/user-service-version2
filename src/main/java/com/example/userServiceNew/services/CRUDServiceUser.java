package com.example.userServiceNew.services;

import com.example.userServiceNew.dto.UserDto;

import java.util.Collection;

public interface CRUDServiceUser<T> {
    T getById(Integer id);
    Collection<T> getAll();
    UserDto create(T item);
    UserDto update(Integer id, T item);
    void delete(Integer id);
}
