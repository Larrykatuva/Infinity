package com.celica.infinity.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

public class Utils {
    public static final int OTP_LOWER_LIMIT = 100000;
    public static final int OTP_UPPER_LIMIT = 999999;

    public static boolean stringNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static String cleanString(String string) {
        return string == null ? null : string.trim();
    }

    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    public static boolean isValidName(String name) {
        String nameRegex = "^[\\p{L} .'-]+$";

        Pattern pattern = Pattern.compile(nameRegex);
        if (name == null) {
            return false;
        }
        return pattern.matcher(name).matches();
    }

    public static boolean isValidUsername(String username) {
        String nameRegex = "^(?=.{3,20}$)(?![.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![.])$";

        Pattern pattern = Pattern.compile(nameRegex);
        if (username == null) {
            return false;
        }
        return pattern.matcher(username).matches();
    }

    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static boolean isValidOTP(Integer otp) {
        return otp != null && otp >= OTP_LOWER_LIMIT && otp <= OTP_UPPER_LIMIT;
    }

    public static int generateRandomNumber(int lowerNumber, int largerNumber) {
        //the generated number will be >= lowerNumber and < largerNumber
        //a + u (b - a)
        largerNumber = largerNumber + 1;
        double randomNumber = Math.random();
        return lowerNumber + (int) (randomNumber * ((double) largerNumber - (double) lowerNumber));
    }

    public static boolean isStringNullOrEmpty(String s) {
        return s == null || cleanString(s).isEmpty();
    }

    public static boolean isSuccessfulStatusCode(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    public static String generateRandomAlphanumericString(int length) {
        String chars = "qwertyuiopasdfghjklmnbvcxzQWERTYUIOPLKJHGFDSAZXCVBNM1234567890";
        int lowerLimit = 0, upperLimit = chars.length() - 1;
        StringBuilder sb = new StringBuilder();

        for (int a = 0; a < length; a++) {
            int cIndex = generateRandomNumber(lowerLimit, upperLimit);
            char c = chars.charAt(cIndex);
            sb.append(c);
        }
        return sb.toString();
    }
}
