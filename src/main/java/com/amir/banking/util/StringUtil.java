package com.amir.banking.util;

import java.security.SecureRandom;

public class StringUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomTraceId(int len) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < len; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println("Random String: " + generateRandomTraceId(16));
        }
    }
}
