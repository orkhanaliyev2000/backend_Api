package com.example.javabackendtemplate.api.controller;

import com.example.javabackendtemplate.api.dto.CreateUserRequest;
import com.example.javabackendtemplate.api.dto.UpdateUserRequest;
import com.example.javabackendtemplate.api.dto.UserResponse;
import com.example.javabackendtemplate.api.mapper.UserMapper;
import com.example.javabackendtemplate.application.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userMapper.toResponse(userService.create(request.email(), request.username()));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return userMapper.toResponse(userService.getById(id));
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.list().stream().map(userMapper::toResponse).toList();
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userMapper.toResponse(userService.update(id, request.email(), request.username()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
