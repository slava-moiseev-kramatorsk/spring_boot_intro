package com.example.demo.service.user;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;


public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto);

}
