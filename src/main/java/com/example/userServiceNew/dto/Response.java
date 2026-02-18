package com.example.userServiceNew.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Integer status;
    private String message;
    private List<String> error;
}