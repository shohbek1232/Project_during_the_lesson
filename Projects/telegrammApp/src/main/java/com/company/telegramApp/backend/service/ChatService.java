package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.model.Chat;
import lombok.NonNull;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> getChatById(@NonNull String chatId);
    Chat createChat(@NonNull String user1Id, @NonNull String user2Id);
    boolean deleteChatById(@NonNull String chatId);
    List<Chat> getChatListByUserId(@NonNull String userId);
}

