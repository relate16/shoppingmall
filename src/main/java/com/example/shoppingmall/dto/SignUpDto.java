package com.example.shoppingmall.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SignUpDto {
    @NotEmpty
    private String username;
    @NotNull
    private int password;
    @NotNull
    private int password_re;
    @NotEmpty
    private String city;
    @NotEmpty
    private String zipcode;
}
