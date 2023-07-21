package com.company;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CurrencyDemo {
    static Scanner scanner = new Scanner(System.in);
    static Scanner scanDouble = new Scanner(System.in);

    @SneakyThrows
    public static void main(String[] args) {
        while (true) {
            System.out.println();
            // show currency "ccy - name uz"

            URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();
            @Cleanup BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            Gson gson = new Gson();
            List<Currency> list = gson.fromJson(reader, new TypeToken<List<Currency>>() {
            }.getType());


            list.forEach(currency -> System.out.println(
                    currency.getCcy() + " - " + currency.getCcyNmUZ()
            ));
            System.out.println("UZS - O'zbek so'mi");
            System.out.println("***");

            List<String> types = new ArrayList<>(list.stream().map(Currency::getCcy).toList());
            types.add("UZS");

            System.out.print("From currency : ");
            String from = scanner.nextLine().toUpperCase();

            if (!types.contains(from)) continue;

            System.out.print("To currency : ");
            String to = scanner.nextLine().toUpperCase();

            if (!types.contains(to)) continue;

            System.out.print("Enter amount : ");
            Double amount = scanDouble.nextDouble();

            if (amount <= 0) continue;

            convertor(from, to, amount, list);

            System.out.println("0. Exit");
            String option = scanner.nextLine();

            if (option.equals("0")) break;
        }
    }

    private static void convertor(String from, String to, Double amount,
                                  List<Currency> list) {
        Currency fromCurrency = list.stream()
                .filter(currency -> currency.getCcy().equalsIgnoreCase(from))
                .findFirst()
                .orElse(new Currency("1"));

        Currency toCurrency = list.stream()
                .filter(currency -> currency.getCcy().equalsIgnoreCase(to))
                .findFirst()
                .orElse(new Currency("1"));

        System.out.println("1 " + from + " = " + fromCurrency.getRate() + " UZS");
        System.out.println("1 " + to + " = " + toCurrency.getRate() + " UZS");
        System.out.println("---");

        double result = Double.parseDouble(fromCurrency.getRate()) * amount /
                Double.parseDouble(toCurrency.getRate());

        System.out.println(amount + " " + from + " = " + result + " " + to);
    }
}