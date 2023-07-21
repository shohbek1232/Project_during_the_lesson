package com.company.telegramApp.backend.model;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class User extends BaseModel {
    private final @NonNull String firstName;
    private final @NonNull String username;
    private final @NonNull String password;
}
