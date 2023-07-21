package com.company.telegramApp;

import com.company.telegramApp.backend.db.DataBase;
import com.company.telegramApp.frontend.MainUI;

public class TelegramApp {
    public static void main(String[] args) {
        try {
            DataBase.loadData();

            MainUI mainUI = new MainUI();
            mainUI.window();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
