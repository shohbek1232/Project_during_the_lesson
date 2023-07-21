package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.model.Message;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    boolean createMessage(@NonNull String senderId, @NonNull String receiverId,
                          @NonNull String messageBody);
    Optional<Message> getMessageById(@NonNull String messageId);
    boolean editMessage(@NonNull String messageId, @NonNull String messageBody);
    boolean deleteMessage(@NonNull String messageId);
    boolean readMessage(@NonNull String messageId);
    List<Message> getMessagesByChatId(@NonNull String chatId);
}