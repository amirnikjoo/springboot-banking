package com.amir.banking.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionLogger implements TransactionObserver {

    @Value("${app.log.file.name}")
    private String filename;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss,SSSSSS");

    @Async
    @Override  
    public void onTransaction(String traceId, String accountNumber, String transactionType, double amount) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            out.printf("%s, %s, %s, %s, %.2f%n", LocalDateTime.now().format(formatter), traceId, accountNumber, transactionType, amount);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void onTransaction(String traceId, String message) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            out.println(String.format("%s, %s, %s", LocalDateTime.now().format(formatter), traceId, message));
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Log file name: " + filename);
    }
}