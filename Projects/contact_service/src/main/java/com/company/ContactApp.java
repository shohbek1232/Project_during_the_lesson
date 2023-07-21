package com.company;

import com.company.model.Contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactApp {

    private static List<Contact> contacts = new ArrayList<>();

    static {
        readData();
    }

    public static void main(String[] args) {
        while (true){
            try {
                System.out.println();
                System.out.println("1. Add contacts");
                System.out.println("2. Show contacts");
                System.out.println("3. Delete contact");
                System.out.println("0. Exit");
                String operation = getInputAsString("?: ");

                if(operation.equals("0")) break;

                switch (operation){
                    case "1" -> addContact();
                    case "2" -> showContacts();
                    case "3" -> deleteContact();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private static void addContact() {
        String fullName = getInputAsString("Full name : ");
        String phoneNumber = getInputAsString("Phone number: ");

        Contact contact = getContactByPhoneNumber(phoneNumber);
        if (contact != null) {
            System.out.println("Contact exists with this number");
            return;
        }

        contacts.add(new Contact(fullName, phoneNumber));
        System.out.println("New contact was added");
        writeData();
    }

    private static Contact getContactByPhoneNumber(String phoneNumber) {
        return contacts.stream()
                .filter(contact -> contact.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(null);
    }


    private static void showContacts() {
        if(contacts.isEmpty()){
            System.out.println("No contacts");
        }else{
            contacts.forEach(System.out::println);
        }
    }

    private static void deleteContact() {
        String phoneNumber = getInputAsString("Phone number: ");

        boolean removeIf = contacts.removeIf(contact ->
                contact.getPhoneNumber().equals(phoneNumber));

        if(removeIf){
            System.out.println("Contact deleted");
            writeData();
        }else{
            System.out.println("Contact not found");
        }
    }

    private static String getInputAsString(String demo) {
        System.out.print(demo);
        return new Scanner(System.in).nextLine();
    }

    @SneakyThrows
    private static void readData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String jsonData = Files.readString(Path.of("contacts.json"));
            ArrayList<Contact> contactArrayList = gson.fromJson(jsonData,
                    new TypeToken<ArrayList<Contact>>() {}.getType());

            contacts.addAll(contactArrayList);

        }catch (Exception e){

        }
    }

    @SneakyThrows
    private static void writeData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Runnable runnable  = () -> {
            try {
                @Cleanup PrintWriter writer = new PrintWriter("contacts.json");
                gson.toJson(contacts, writer);
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(runnable);
        service.shutdown();

    }
}
