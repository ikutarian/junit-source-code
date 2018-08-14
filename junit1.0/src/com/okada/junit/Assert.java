package com.okada.junit;

public class Assert {

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            assertEquals(notEqualsMessage("", expected, actual), false);
        }
    }

    public static void assertEquals(String message, boolean condition) {
        if (!condition) {
            throw new AssertionFailedError(message);
        }
    }

    private static String notEqualsMessage(String message, Object expected, Object actual) {
        return message + " expected:\"" + expected + "\" but was:\"" + actual + "\"";
    }
}
