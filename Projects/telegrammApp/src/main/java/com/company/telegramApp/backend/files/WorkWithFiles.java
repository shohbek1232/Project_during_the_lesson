package com.company.telegramApp.backend.files;

import com.company.telegramApp.backend.enums.ObjectType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WorkWithFiles {
    private static final Path BASE_PATH = Path.of("telegramapp");

    static {
        if (!Files.exists(BASE_PATH)) {
            try {
                Files.createDirectory(BASE_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    @SneakyThrows
    public static void readData(@NonNull ObjectType objectType) {
        Path path = BASE_PATH.resolve(objectType.getFileName());
        objectType.getObjectList().clear();

        if (!Files.exists(path)) {
            Files.writeString(path, "[]");
        } else {
            List list = GSON.fromJson(Files.readString(path), objectType.getType());
            objectType.getObjectList().addAll(list);
        }
    }

    @SneakyThrows
    public static void writeData(@NonNull ObjectType objectType){
        Path path = BASE_PATH.resolve(objectType.getFileName());
        String json = GSON.toJson(objectType.getObjectList());
        Files.writeString(path, json);
    }

    public static void readAllData(){
        for (ObjectType objectType : ObjectType.values()) {
            readData(objectType);
        }
    }

    public static void writeAllData(){
        for (ObjectType objectType : ObjectType.values()) {
            writeData(objectType);
        }
    }
}
