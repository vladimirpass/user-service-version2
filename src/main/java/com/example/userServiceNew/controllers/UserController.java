package com.example.userServiceNew.controllers;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.exception.DeleteException;
import com.example.userServiceNew.services.UserCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCRUDService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto newUserDto = userService.create(userDto);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws DeleteException {
        try {
            if (userService.getById(id) != null) return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
        }catch (Exception e){
            throw new DeleteException("User " + id + " отсутствует");
        }
        return null;
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(){
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Integer id) throws DeleteException{
        try {
            if (userService.getById(id) != null) {
                userService.delete(id);
                Response response = new Response("User delete");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }catch(Exception e){
            throw new DeleteException("User с ID " + id + " не найден");
        }
        return null;
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id,@RequestBody UserDto userDto){
        UserDto updateUserDto = userService.update(id,userDto);
        userDto.setId(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }





}
