package com.jsmorales.controlpersonalrampa.utils;

public class ContainsString {

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }
}
