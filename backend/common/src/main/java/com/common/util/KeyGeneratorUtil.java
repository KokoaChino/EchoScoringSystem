package com.common.util;

import java.util.concurrent.ThreadLocalRandom;


public class KeyGeneratorUtil {

    private static final char[] CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String generate(int n) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] buf = new char[n];
        for (int i = 0; i < n; i++) {
            buf[i] = CHARS[random.nextInt(CHARS.length)];
        }
        return new String(buf);
    }

    public static void main(String[] args) {
        System.out.println(generate(32));
    }
}