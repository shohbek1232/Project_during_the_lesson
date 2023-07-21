package com.company.telegramApp.backend.model;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class Message extends BaseModel{
    private final @NonNull String chatId;
    private final @NonNull String senderId;
    private @NonNull String body;
    private boolean read;
    private final Date createdAt = new Date();
    private boolean deleted;
}
