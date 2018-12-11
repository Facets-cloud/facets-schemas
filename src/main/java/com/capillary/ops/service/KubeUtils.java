package com.capillary.ops.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

public class KubeUtils {

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";

    private static SecureRandom secureRandom = new SecureRandom();

    public static String generatePassword(int passwordLength) {
        String charSet = ALPHA_CAPS + ALPHA + SPECIAL_CHARS + NUMERIC;
        String result = "";
        for (int i = 0; i < passwordLength; i++) {
            int index = secureRandom.nextInt(charSet.length());
            result += charSet.charAt(index);
        }

        return result;
    }
}
