package com.example.javabackendtemplate.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.javabackendtemplate.application.port.UserRepositoryPort;
import com.example.javabackendtemplate.application.service.UserService;
import com.example.javabackendtemplate.domain.User;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserServiceTest {

    private final UserRepositoryPort userRepositoryPort = Mockito.mock(UserRepositoryPort.class);
    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);
    private final UserService userService = new UserService(userRepositoryPort, clock);

    @Test
    void createUser() {
        when(userRepositoryPort.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(userRepositoryPort.findByUsername("user")).thenReturn(Optional.empty());
        when(userRepositoryPort.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.create("user@example.com", "user");

        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getCreatedAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test
    void createUserFailsWhenEmailExists() {
        when(userRepositoryPort.findByEmail("user@example.com"))
                .thenReturn(Optional.of(new User("user@example.com", "user", Instant.now(clock))));

        assertThatThrownBy(() -> userService.create("user@example.com", "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");
    }
}
