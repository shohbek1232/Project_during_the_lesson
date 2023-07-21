package com.company.telegramApp.frontend;

import com.company.telegramApp.backend.dto.Response;
import com.company.telegramApp.backend.model.Chat;
import com.company.telegramApp.backend.model.Message;
import com.company.telegramApp.backend.model.User;
import com.company.telegramApp.backend.service.*;

import java.util.List;
import java.util.Optional;

public class MainUI {

    public void window() {
        while (true) {
            System.out.println("""
                                        
                    1. Login
                    2. Register
                    0. Exit""");
            String option = ScannerUtil.getInputAsString("?: ");
            if (option.equals("0")) break;

            switch (option) {
                case "1" -> login();
                case "2" -> register();
            }

        }
    }

    private void register() {
        String firstName = ScannerUtil.getInputAsString("First name: ");
        String username = ScannerUtil.getInputAsString("Username: ");
        String password = ScannerUtil.getInputAsString("Password: ");
        UserService userService = new UserServiceImpl();
        User user = new User(firstName, username, password);
        Response response = userService.addUser(user);
        System.out.println(response);
    }

    private void login() {
        String username = ScannerUtil.getInputAsString("Username: ");
        String password = ScannerUtil.getInputAsString("Password: ");
        UserService userService = new UserServiceImpl();
        Response response = userService.login(username, password);
        if (response.success()) {
            System.out.println("Welcome");
            cabinet(response.message());
        } else {
            System.out.println(response.message());
        }
    }

    private void cabinet(String userId) {
        while (true) {
            System.out.println("""
                                        
                    1. Create new chat
                    2. Show my chats
                    3. Delete chat
                    0. Log out""");
            String option = ScannerUtil.getInputAsString("?: ");
            if (option.equals("0")) break;

            switch (option) {
                case "1" -> createChat(userId);
                case "2" -> showMyChats(userId);
                case "3" -> deleteChat(userId);
            }
        }
    }

    private void deleteChat(String userId) {
        UserService userService = new UserServiceImpl();
        ChatService chatService = new ChatServiceImpl();


        List<Chat> chatList = chatService.getChatListByUserId(userId);
        if (chatList.isEmpty()) {
            System.out.println("No chats");
        } else {
            while (true) {
                for (Chat chat : chatList) {
                    String otherUserId = chat.getUser1Id().equals(userId) ? chat.getUser2Id() :
                            chat.getUser1Id();
                    Optional<User> userOptional = userService.getUserById(otherUserId);
                    userOptional.ifPresent(System.out::print);
                    if (chat.getCounter() > 0 && !chat.getLastWriterId().equals(userId)) {
                        System.out.println("(%s)".formatted(chat.getCounter()));
                    } else {
                        System.out.println();
                    }
                }

                String username = ScannerUtil.getInputAsString("Username (for delete chat) or 0 (back) : ");
                if (username.equals("back") || username.equals("0")) break;

                Optional<User> userOptional = userService.getUserByUsername(username);
                userOptional.ifPresentOrElse(user ->
                                chatService.deleteChatById(chatService.createChat(userId, user.getId()).getId()),
                        () -> System.out.println("User not found"));

                chatList = chatService.getChatListByUserId(userId);
                if (chatList.isEmpty()) break;
            }
        }
    }

    private void showMyChats(String userId) {
        UserService userService = new UserServiceImpl();
        ChatService chatService = new ChatServiceImpl();


        List<Chat> chatList = chatService.getChatListByUserId(userId);
        if (chatList.isEmpty()) {
            System.out.println("No chats");
        } else {
            while (true) {
                for (Chat chat : chatList) {
                    String otherUserId = chat.getUser1Id().equals(userId) ? chat.getUser2Id() :
                            chat.getUser1Id();
                    Optional<User> userOptional = userService.getUserById(otherUserId);
                    userOptional.ifPresent(System.out::print);
                    if (chat.getCounter() > 0 && !chat.getLastWriterId().equals(userId)) {
                        System.out.println("(%s)".formatted(chat.getCounter()));
                    } else {
                        System.out.println();
                    }
                }

                String username = ScannerUtil.getInputAsString("Username (for show chat) or 0(back) : ");
                if (username.equals("back")) break;
                if (username.equals("0")) break;

                Optional<User> userOptional = userService.getUserByUsername(username);
                userOptional.ifPresentOrElse(user -> chatWindow(userId, user.getId()),
                        () -> System.out.println("User not found"));

                chatList = chatService.getChatListByUserId(userId);
            }
        }
    }

    private void createChat(String userId) {
        while (true) {
            String username = ScannerUtil.getInputAsString("Username (for create chat) or 0 (back) : ");
            if (username.equals("back") || username.equals("0")) break;

            UserService userService = new UserServiceImpl();
            Optional<User> userOptional = userService.getUserByUsername(username);
            if (userOptional.isPresent()) {
                chatWindow(userId, userOptional.get().getId());
                break;
            } else {
                System.out.println("User not found");
            }
        }
    }

    private void chatWindow(String user1Id, String user2Id) {
        ChatService chatService = new ChatServiceImpl();
        MessageService messageService = new MessageServiceImpl();

        Chat chat = chatService.createChat(user1Id, user2Id);
        if (chat == null) return;

        while (true) {
            System.out.println("\n\n\n");
            List<Message> messageList = messageService.getMessagesByChatId(chat.getId());
            for (Message message : messageList) {

                if (!message.isRead() && message.getSenderId().equals(user2Id)) {
                    messageService.readMessage(message.getId());
                }

                String format = message.getSenderId().equals(user1Id) ? "%60s" : "%s";
                System.out.println(format.formatted(message.getBody()));
                System.out.println(format.formatted(message.getCreatedAt()));
                if (message.getSenderId().equals(user1Id)) {
                    System.out.println(format.formatted(message.isRead() ? "✔✔" : "✔"));
                    System.out.println(format.formatted(message.getId()));
                }
                System.out.println();
            }
            System.out.println("1. Write message");
            if (!messageList.isEmpty()) {
                System.out.println("2. Edit message");
                System.out.println("3. Delete message");
            }
            System.out.println("0. Back");
            String option = ScannerUtil.getInputAsString("?: ");
            if (option.equals("0")) break;

            switch (option) {
                case "1" -> {
                    String messageBody = ScannerUtil.getInputAsString("Enter text: ");
                    messageService.createMessage(user1Id, user2Id, messageBody);
                }
                case "2" -> {
                    String messageId = ScannerUtil.getInputAsString("Enter message id (for edit) : ");
                    String newMessageBody = ScannerUtil.getInputAsString("Enter new text of message  : ");
                    messageService.editMessage(messageId, newMessageBody);
                }
                case "3" -> {
                    String messageId = ScannerUtil.getInputAsString("Enter message id (for delete) : ");
                    messageService.deleteMessage(messageId);
                }

            }
        }
    }
}

