package com.example.javabackendtemplate.api.mapper;

import com.example.javabackendtemplate.api.dto.UserResponse;
import com.example.javabackendtemplate.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getCreatedAt());
    }
}
