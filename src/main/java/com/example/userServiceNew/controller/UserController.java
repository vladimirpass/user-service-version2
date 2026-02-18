package com.example.userServiceNew.controller;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.dto.UserResponseDto;
import com.example.userServiceNew.service.UserCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCRUDService userService;

    @PostMapping
    public Response createUser(@RequestBody UserDto userDto){
        return userService.create(userDto);
    }


    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping
    public Collection<UserResponseDto> getAllUsers(){
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public Response deleteUser(@PathVariable Integer id){
        return userService.delete(id);
    }

    @PutMapping("/{id}")
    public Response updateUser(@PathVariable Integer id,@RequestBody UserDto userDto){
        return userService.update(id, userDto);
    }





}
