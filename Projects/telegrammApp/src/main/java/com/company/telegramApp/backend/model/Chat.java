package com.company.telegramApp.backend.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Chat extends BaseModel{
    private final @NonNull String user1Id;
    private final @NonNull String user2Id;
    private boolean deleted;
    private int counter;
    private String lastWriterId = "";
}
