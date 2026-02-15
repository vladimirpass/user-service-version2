package com.example.userServiceNew.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String nameUser;
    private String email;
    private Integer age;
    private LocalDateTime createAt = LocalDateTime.now();

}
