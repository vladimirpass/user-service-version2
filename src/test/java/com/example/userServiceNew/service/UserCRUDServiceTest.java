package com.example.userServiceNew.service;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.dto.UserDto;
import com.example.userServiceNew.dto.UserResponseDto;
import com.example.userServiceNew.model.User;
import com.example.userServiceNew.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserCRUDServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCRUDService userService;

    private User user;
    private UserDto userDto;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        user = new User();
        user.setId(1);
        user.setNameUser("John Doe");
        user.setEmail("john.doe@example.com");
        user.setAge(30);
        user.setCreateAt(fixedDateTime);

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setNameUser("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setAge(30);
        userDto.setCreateAt(fixedDateTime);
    }

    @Nested
    @DisplayName("getById() method tests")
    class GetByIdTests {

        @Test
        @DisplayName("Should return user when user exists")
        void getById_UserExists_ReturnsUser() {
            when(userRepository.findById(1)).thenReturn(Optional.of(user));

            UserResponseDto result = userService.getById(1);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(0);
            assertThat(result.getError()).isNull();
            assertThat(result.getUserDto()).isNotNull();
            assertThat(result.getUserDto().getId()).isEqualTo(1);
            assertThat(result.getUserDto().getNameUser()).isEqualTo("John Doe");
            assertThat(result.getUserDto().getEmail()).isEqualTo("john.doe@example.com");
            assertThat(result.getUserDto().getAge()).isEqualTo(30);

            verify(userRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Should return error response when user not found")
        void getById_UserNotFound_ReturnsError() {
            when(userRepository.findById(999)).thenThrow(new RuntimeException("User not found"));

            UserResponseDto result = userService.getById(999);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(1);
            assertThat(result.getUserDto()).isNull();
            assertThat(result.getError()).isNotNull();
            assertThat(result.getError().get(0)).contains("User not found");

            verify(userRepository, times(1)).findById(999);
        }
    }


    @Test
    @DisplayName("Should return all users when users exist")
    void getAll_UsersExist_ReturnsAllUsers() {
        User user2 = new User();
        user2.setId(2);
        user2.setNameUser("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setAge(25);
        user2.setCreateAt(fixedDateTime);

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        Collection<UserResponseDto> result = userService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        List<UserResponseDto> resultList = result.stream().toList();

        assertThat(resultList.get(0).getStatus()).isEqualTo(0);
        assertThat(resultList.get(0).getUserDto().getId()).isEqualTo(1);
        assertThat(resultList.get(0).getUserDto().getNameUser()).isEqualTo("John Doe");

        assertThat(resultList.get(1).getStatus()).isEqualTo(0);
        assertThat(resultList.get(1).getUserDto().getId()).isEqualTo(2);
        assertThat(resultList.get(1).getUserDto().getNameUser()).isEqualTo("Jane Smith");

        verify(userRepository, times(1)).findAll();
    }

    @Nested
    @DisplayName("create() method tests")
    class CreateTests {

        @Test
        @DisplayName("Should create user when valid data provided")
        void create_ValidData_ReturnsSuccess() {
            when(userRepository.save(any(User.class))).thenReturn(user);

            Response result = userService.create(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(0);
            assertThat(result.getMessage()).isEqualTo("Пользователь сохранен");
            assertThat(result.getError()).isNull();

            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("Should return error when name is null")
        void create_NameNull_ReturnsError() {
            userDto.setNameUser(null);

            Response result = userService.create(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(1);
            assertThat(result.getMessage()).isEqualTo("Произошла ошибка при попытке сохранения пользователя");
            assertThat(result.getError()).contains("Не все данные были заполнены");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should return error when email is null")
        void create_EmailNull_ReturnsError() {
            userDto.setEmail(null);

            Response result = userService.create(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(1);
            assertThat(result.getError()).contains("Не все данные были заполнены");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should return error when age is null")
        void create_AgeNull_ReturnsError() {
            userDto.setAge(null);

            Response result = userService.create(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(1);
            assertThat(result.getError()).contains("Не все данные были заполнены");

            verify(userRepository, never()).save(any(User.class));
        }
    }


    @Test
    @DisplayName("Should update user when valid data provided")
    void update_ValidData_ReturnsSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        Response result = userService.update(1, userDto);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(0);
        assertThat(result.getMessage()).isEqualTo("Пользователь обновлен");
        assertThat(result.getError()).isNull();

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Nested
    @DisplayName("delete() method tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete user when user exists")
        void delete_UserExists_ReturnsSuccess() {
            when(userRepository.findById(1)).thenReturn(Optional.of(user));
            doNothing().when(userRepository).deleteById(1);

            Response result = userService.delete(1);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(0);
            assertThat(result.getMessage()).isEqualTo("Пользователь удален");
            assertThat(result.getError()).isNull();

            verify(userRepository, times(1)).findById(1);
            verify(userRepository, times(1)).deleteById(1);
        }

        @Test
        @DisplayName("Should return error when user not found")
        void delete_UserNotFound_ReturnsError() {
            when(userRepository.findById(999)).thenReturn(Optional.empty());

            Response result = userService.delete(999);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(1);
            assertThat(result.getMessage()).isEqualTo("Удаление пользователя не возможно");
            assertThat(result.getError()).contains("Пользователя с таким id не существует");

            verify(userRepository, times(1)).findById(999);
            verify(userRepository, never()).deleteById(any());
        }
    }

}
