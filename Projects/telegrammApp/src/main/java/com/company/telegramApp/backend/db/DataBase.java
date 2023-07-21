package com.company.telegramApp.backend.db;

import com.company.telegramApp.backend.files.WorkWithFiles;
import com.company.telegramApp.backend.model.Chat;
import com.company.telegramApp.backend.model.Message;
import com.company.telegramApp.backend.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public interface DataBase {
    List<User> USERS = new ArrayList<>();
    List<Chat> CHATS = new ArrayList<>();
    List<Message> MESSAGES = new ArrayList<>();
    static void loadData(){
        User user1 = new User("Akbar", "akbar", "123");
        User user2 = new User("Yashnarbek", "tony", "123");
        User user3 = new User("Sardor", "sardor", "123");

        Collections.addAll(USERS, user1, user2, user3);

        try{
            WorkWithFiles.readAllData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
