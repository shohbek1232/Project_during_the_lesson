package com.company.telegramApp.frontend;

import java.util.Scanner;

public interface ScannerUtil {
    static String getInputAsString(String title){
        System.out.print(title);
        return new Scanner(System.in).nextLine();
    }
}