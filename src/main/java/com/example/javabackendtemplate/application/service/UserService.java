package com.example.javabackendtemplate.application.service;

import com.example.javabackendtemplate.application.exception.NotFoundException;
import com.example.javabackendtemplate.application.port.UserRepositoryPort;
import com.example.javabackendtemplate.domain.User;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final Clock clock;

    public UserService(UserRepositoryPort userRepositoryPort, Clock clock) {
        this.userRepositoryPort = userRepositoryPort;
        this.clock = clock;
    }

    public User create(String email, String username) {
        userRepositoryPort.findByEmail(email).ifPresent(user -> {
            throw new IllegalArgumentException("Email already exists");
        });
        userRepositoryPort.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("Username already exists");
        });
        return userRepositoryPort.save(new User(email, username, Instant.now(clock)));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepositoryPort
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<User> list() {
        return userRepositoryPort.findAll();
    }

    public User update(Long id, String email, String username) {
        User user = getById(id);
        user.update(email, username);
        return userRepositoryPort.save(user);
    }

    public void delete(Long id) {
        userRepositoryPort.deleteById(id);
    }
}
