package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.db.DataBase;
import com.company.telegramApp.backend.enums.ObjectType;
import com.company.telegramApp.backend.files.WorkWithFiles;
import com.company.telegramApp.backend.model.Chat;
import com.company.telegramApp.backend.model.Message;
import com.company.telegramApp.backend.model.User;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MessageServiceImpl implements MessageService {
    @Override
    public boolean createMessage(@NonNull String senderId, @NonNull String receiverId,
                                 @NonNull String messageBody) {

        try {
            UserService userService = new UserServiceImpl();

            Optional<User> userOptional = userService.getUserById(senderId);
            if (userOptional.isEmpty()) return false;

            userOptional = userService.getUserById(receiverId);
            if (userOptional.isEmpty()) return false;

            ChatService chatService = new ChatServiceImpl();
            Chat chat = chatService.createChat(senderId, receiverId);
            if (chat == null) return false;

            if (messageBody.isBlank()) return false;

            Message message = new Message(chat.getId(), senderId, messageBody);
            DataBase.MESSAGES.add(message);
            WorkWithFiles.writeData(ObjectType.MESSAGE);

            chat.setCounter(chat.getCounter() + 1);
            chat.setLastWriterId(senderId);
            WorkWithFiles.writeData(ObjectType.CHAT);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Message> getMessageById(@NonNull String messageId) {
        try {
            return DataBase.MESSAGES.stream()
                    .filter(message -> message.getId().equals(messageId))
                    .findFirst();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean editMessage(@NonNull String messageId, @NonNull String messageBody) {
        try {
            Optional<Message> messageOptional = getMessageById(messageId);
            if (messageOptional.isEmpty()) return false;

            if (messageBody.isBlank()) return false;

            Message message = messageOptional.get();
            message.setBody(messageBody);
            WorkWithFiles.writeData(ObjectType.MESSAGE);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteMessage(@NonNull String messageId) {
        try {
            Optional<Message> messageOptional = getMessageById(messageId);
            if (messageOptional.isEmpty()) return false;

            Message message = messageOptional.get();
            if (message.isDeleted()) return false;

            message.setDeleted(true);
            WorkWithFiles.writeData(ObjectType.MESSAGE);

            if (!message.isRead()) {
                ChatService chatService = new ChatServiceImpl();
                Optional<Chat> chatOptional = chatService.getChatById(message.getChatId());
                chatOptional.ifPresent(chat -> {
                    chat.setCounter(chat.getCounter() - 1);
                    WorkWithFiles.writeData(ObjectType.CHAT);
                });
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean readMessage(@NonNull String messageId) {
        try {
            Optional<Message> messageOptional = getMessageById(messageId);
            if (messageOptional.isEmpty()) return false;

            Message message = messageOptional.get();
            if (message.isRead() || message.isDeleted()) return false;

            message.setRead(true);
            WorkWithFiles.writeData(ObjectType.MESSAGE);

            ChatService chatService = new ChatServiceImpl();
            Optional<Chat> chatOptional = chatService.getChatById(message.getChatId());
            chatOptional.ifPresent(chat -> {
                chat.setCounter(0);
                WorkWithFiles.writeData(ObjectType.CHAT);
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Message> getMessagesByChatId(@NonNull String chatId) {
        try {
            return DataBase.MESSAGES.stream()
                    .filter(message -> !message.isDeleted())
                    .filter(message -> message.getChatId().equals(chatId))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // todo singleton
}