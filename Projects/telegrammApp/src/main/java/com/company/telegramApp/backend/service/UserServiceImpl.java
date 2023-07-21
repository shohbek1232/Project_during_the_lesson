package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.db.DataBase;
import com.company.telegramApp.backend.dto.Response;
import com.company.telegramApp.backend.enums.ObjectType;
import com.company.telegramApp.backend.files.WorkWithFiles;
import com.company.telegramApp.backend.model.User;
import lombok.NonNull;

import java.util.Objects;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Override
    public Response login(@NonNull String username, @NonNull String password) {
        Optional<User> userOptional = getUserByUsername(username);
        if (userOptional.isEmpty()) {
            return new Response("Username or password wrong", false);
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return new Response("Username or password wrong", false);
        }

        return new Response(user.getId(), true);
    }

    @Override
    public Response addUser(@NonNull User user) {
        try {
            Optional<User> userOptional = getUserByUsername(user.getUsername());
            if (userOptional.isPresent())
                return new Response("User exist with this username", false);

            DataBase.USERS.add(user);
            WorkWithFiles.writeData(ObjectType.USER);

            return new Response("User successfully registered", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(Objects.toString(e.getMessage(), "some exception"), false);
        }
    }

    @Override
    public Optional<User> getUserById(@NonNull String userId) {
        try {
            return DataBase.USERS.stream()
                    .filter(user -> Objects.equals(user.getId(), userId))
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Optional<User> getUserByUsername(@NonNull String username) {
        try {
            return DataBase.USERS.stream()
                    .filter(user -> Objects.equals(user.getUsername(), username))
                    .findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // todo singleton
}

