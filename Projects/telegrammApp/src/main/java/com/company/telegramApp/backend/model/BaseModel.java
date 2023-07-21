package com.company.telegramApp.backend.model;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public abstract class BaseModel {
    protected final @NonNull String id = UUID.randomUUID().toString();
}
