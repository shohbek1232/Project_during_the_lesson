package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.dto.Response;
import com.company.telegramApp.backend.model.User;
import lombok.NonNull;

import java.util.Optional;

public interface UserService {
    Response addUser(@NonNull User user);
    Optional<User> getUserById(@NonNull String userId);
    Optional<User> getUserByUsername(@NonNull String username);
    Response login(@NonNull String username, @NonNull String password);
}
