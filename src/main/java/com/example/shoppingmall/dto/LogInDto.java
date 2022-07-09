package com.example.shoppingmall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LogInDto {
    @NotEmpty
    private String username;
    @NotNull
    private int password;
}
