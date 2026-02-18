package com.example.userServiceNew.controller;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.dto.UserResponseDto;
import com.example.userServiceNew.service.UserCRUDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCRUDService userService;

    private UserDto userDto;
    private UserResponseDto userResponseDto;
    private Response successResponse;
    private Response errorResponse;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setNameUser("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setAge(30);
        userDto.setCreateAt(fixedDateTime);

        userResponseDto = new UserResponseDto(0, userDto, null);

        successResponse = new Response(0, "Operation successful", null);
        errorResponse = new Response(1, "Operation failed", List.of("Error message"));
    }


    @Test
    @DisplayName("Should create user successfully")
    void createUser_Success() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(successResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.message").value("Operation successful"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(userService, times(1)).create(any(UserDto.class));
    }

    @Test
    @DisplayName("Should get user by id successfully")
    void getUserById_Success() throws Exception {
        when(userService.getById(1)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.userDto.id").value(1))
                .andExpect(jsonPath("$.userDto.nameUser").value("John Doe"))
                .andExpect(jsonPath("$.userDto.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.userDto.age").value(30))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(userService, times(1)).getById(1);
    }

    @Test
    @DisplayName("Should return error when user not found")
    void getUserById_NotFound() throws Exception {
        UserResponseDto notFoundResponse = new UserResponseDto(1, null, List.of("User not found"));
        when(userService.getById(999)).thenReturn(notFoundResponse);

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.userDto").isEmpty())
                .andExpect(jsonPath("$.error[0]").value("User not found"));

        verify(userService, times(1)).getById(999);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void getAllUsers_Success() throws Exception {
        UserDto userDto2 = new UserDto();
        userDto2.setId(2);
        userDto2.setNameUser("Jane Smith");
        userDto2.setEmail("jane.smith@example.com");
        userDto2.setAge(25);
        userDto2.setCreateAt(fixedDateTime);

        UserResponseDto response1 = new UserResponseDto(0, userDto, null);
        UserResponseDto response2 = new UserResponseDto(0, userDto2, null);

        Collection<UserResponseDto> users = Arrays.asList(response1, response2);

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(0))
                .andExpect(jsonPath("$[0].userDto.id").value(1))
                .andExpect(jsonPath("$[0].userDto.nameUser").value("John Doe"))
                .andExpect(jsonPath("$[1].status").value(0))
                .andExpect(jsonPath("$[1].userDto.id").value(2))
                .andExpect(jsonPath("$[1].userDto.nameUser").value("Jane Smith"));

        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_Success() throws Exception {
        when(userService.delete(1)).thenReturn(successResponse);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.message").value("Operation successful"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(userService, times(1)).delete(1);
    }

    @Test
    @DisplayName("Should return error when deleting non-existent user")
    void deleteUser_NotFound() throws Exception {
        when(userService.delete(999)).thenReturn(errorResponse);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.message").value("Operation failed"))
                .andExpect(jsonPath("$.error[0]").value("Error message"));

        verify(userService, times(1)).delete(999);
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_Success() throws Exception {
        when(userService.update(eq(1), any(UserDto.class))).thenReturn(successResponse);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.message").value("Operation successful"))
                .andExpect(jsonPath("$.error").isEmpty());

        verify(userService, times(1)).update(eq(1), any(UserDto.class));
    }
}
