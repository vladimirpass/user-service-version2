package com.example.userServiceNew.controllers;

import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.services.UserCRUDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCRUDService userService;

    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto();
        testUserDto.setId(1);
        testUserDto.setNameUser("Иван Петров");
        testUserDto.setEmail("ivan@example.com");
        testUserDto.setAge(25);
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Arrange
        int userId = 1;

        when(userService.getById(userId)).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nameUser").value("Иван Петров"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService, times(2)).getById(userId); // В контроллере вызывается два раза
    }

    @Test
    void createUserTest() throws Exception{

        when(userService.create(any(UserDto.class))).thenReturn(testUserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nameUser").value("Иван Петров"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"))
                .andExpect(jsonPath("$.age").value("25"));

        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers_WhenUsersExist() throws Exception {
        // Arrange
        UserDto user2 = new UserDto();
        user2.setId(2);
        user2.setNameUser("Петр Иванов");
        user2.setEmail("petr@example.com");

        Collection<UserDto> users = Arrays.asList(testUserDto, user2);
        when(userService.getAll()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(userService, times(1)).getAll();
    }

    @Test
    void deleteUser_ShouldReturnSuccessResponse_WhenUserExists() throws Exception {
        // Arrange
        when(userService.getById(1)).thenReturn(testUserDto);
        doNothing().when(userService).delete(1);

        // Act & Assert
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User delete"));

        verify(userService, times(1)).getById(1);
        verify(userService, times(1)).delete(1);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() throws Exception {
        // Arrange
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setNameUser("Иван Петров Обновленный");
        updatedUserDto.setEmail("ivan.updated@example.com");
        updatedUserDto.setAge(26);

        when(userService.update(eq(1), any(UserDto.class))).thenReturn(updatedUserDto);

        // Act & Assert
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nameUser").value("Иван Петров Обновленный"))
                .andExpect(jsonPath("$.email").value("ivan.updated@example.com"))
                .andExpect(jsonPath("$.age").value(26));

        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }



}
