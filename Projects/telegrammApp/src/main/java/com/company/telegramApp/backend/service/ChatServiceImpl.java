package com.company.telegramApp.backend.service;

import com.company.telegramApp.backend.db.DataBase;
import com.company.telegramApp.backend.enums.ObjectType;
import com.company.telegramApp.backend.files.WorkWithFiles;
import com.company.telegramApp.backend.model.Chat;
import com.company.telegramApp.backend.model.User;
import com.company.telegramApp.backend.model.Message;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ChatServiceImpl implements ChatService {
    @Override
    public Optional<Chat> getChatById(@NonNull String chatId) {
        try {
            return DataBase.CHATS.stream()
                    .filter(chat -> Objects.equals(chat.getId(), chatId))
                    .findFirst();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Chat createChat(@NonNull String user1Id, @NonNull String user2Id) {
        try {
            UserService userService = new UserServiceImpl();
            Optional<User> userOptional = userService.getUserById(user1Id);
            if (userOptional.isEmpty()) {
                throw new NullPointerException("User not found");
            }

            userOptional = userService.getUserById(user2Id);
            if (userOptional.isEmpty()) {
                throw new NullPointerException("User not found");
            }

            Optional<Chat> chatOptional = DataBase.CHATS.stream()
                    .filter(chat ->
                            chat.getUser1Id().equals(user1Id) &&
                                    chat.getUser2Id().equals(user2Id) ||
                                    chat.getUser1Id().equals(user2Id) &&
                                            chat.getUser2Id().equals(user1Id)
                    )
                    .findFirst();

            if(chatOptional.isPresent()){
                Chat chat = chatOptional.get();

                if(chat.isDeleted()){
                    chat.setDeleted(false);
                    WorkWithFiles.writeData(ObjectType.CHAT);
                }

                return chat;
            }

            Chat chat = new Chat(user1Id, user2Id);
            DataBase.CHATS.add(chat);

            WorkWithFiles.writeData(ObjectType.CHAT);
            return chat;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteChatById(@NonNull String chatId) {
        try {
            Optional<Chat> chatOptional = getChatById(chatId);
            if (chatOptional.isEmpty()) return false;

            Chat chat = chatOptional.get();
            if (chat.isDeleted()) return false;

            chat.setDeleted(true);
            WorkWithFiles.writeData(ObjectType.CHAT);

            MessageService messageService = new MessageServiceImpl();
            List<Message> messageList = messageService.getMessagesByChatId(chatId);

            messageList.forEach(message -> messageService.deleteMessage(message.getId()));

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Chat> getChatListByUserId(@NonNull String userId) {

        try {
            UserService userService = new UserServiceImpl();
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isEmpty()) {
                return Collections.emptyList();
            }

            return DataBase.CHATS.stream()
                    .filter(chat -> !chat.isDeleted())
                    .filter(chat ->
                            Objects.equals(chat.getUser1Id(), userId) ||
                                    Objects.equals(chat.getUser2Id(), userId))
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // todo singleton
}
