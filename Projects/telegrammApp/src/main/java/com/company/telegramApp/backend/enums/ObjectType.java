package com.company.telegramApp.backend.enums;

import com.company.telegramApp.backend.db.DataBase;
import com.company.telegramApp.backend.model.Chat;
import com.company.telegramApp.backend.model.Message;
import com.company.telegramApp.backend.model.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public enum ObjectType {
    USER("users.json", new TypeToken<List<User>>(){}.getType(), DataBase.USERS),
    CHAT("chats.json", new TypeToken<List<Chat>>(){}.getType(), DataBase.CHATS),
    MESSAGE("messages.json", new TypeToken<List<Message>>(){}.getType(), DataBase.MESSAGES);

    private final String fileName;
    private final Type type;
    private final List objectList;

    ObjectType(String fileName, Type type, List objectList) {
        this.fileName = fileName;
        this.type = type;
        this.objectList = objectList;
    }

    public String getFileName() {
        return fileName;
    }

    public Type getType() {
        return type;
    }

    public List getObjectList() {
        return objectList;
    }
}
