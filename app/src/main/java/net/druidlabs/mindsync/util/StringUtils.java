package net.druidlabs.mindsync.util;

/**
 * Utility class for String input validation methods.
 *
 * @author Andrew Jones
 * @version 1.0
 * @since 1.2.0-beta.1
 */

public class StringUtils {

    private StringUtils() {
    }

    /**
     * Check if the {@code input} is blank.
     * This method was created to replace the String {@link String#isBlank() isBlank()} method
     * as Android versions older than API 30 (Android 11) did not have access to this method.
     *
     * @param input the input String to be checked.
     * @return {@code true} if the {@code input} has no text or only contains {@link Character#isWhitespace(int)  whitespace} characters.
     * @see String#isBlank()
     */

    public static boolean isInputBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

}
