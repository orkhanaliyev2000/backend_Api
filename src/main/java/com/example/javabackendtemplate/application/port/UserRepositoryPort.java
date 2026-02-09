package com.example.javabackendtemplate.application.port;

import com.example.javabackendtemplate.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void deleteById(Long id);
}
