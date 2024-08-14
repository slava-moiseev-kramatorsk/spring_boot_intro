package com.example.demo.dto.user;

import com.example.demo.validation.FieldMatch;
import com.example.demo.validation.user.FirstNameAndLastName;
import com.example.demo.validation.user.Password;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@FieldMatch(firstField = "password", secondField = "repeatPassword")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Password
    private String password;
    @NotBlank
    @Password
    private String repeatPassword;
    @NotBlank
    @FirstNameAndLastName
    private String firstName;
    @NotBlank
    @FirstNameAndLastName
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
