package com.example.demo.service.user;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with this " + requestDto.getEmail()
            + " already exist");
        }
        Role userRole = roleRepository
                .findByRole(Role.RoleName.USER)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find role: "
                                + Role.RoleName.USER));
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }
}
