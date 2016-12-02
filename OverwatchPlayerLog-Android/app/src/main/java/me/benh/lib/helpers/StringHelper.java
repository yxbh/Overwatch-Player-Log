package me.benh.lib.helpers;

import android.support.annotation.NonNull;

/**
 * Created by Benjamin Huang on 2/12/2016.
 */

public final class StringHelper {
    private StringHelper() {}

    public static boolean isAllAlphabet(@NonNull String text) {
        if (text.isEmpty()) return false;

        for (char c : text.toCharArray()) {
            if (Character.isDigit(c) || Character.isWhitespace(c) || !Character.isLetter(c))
                return false;
        }

        return true;
    }

    public static boolean isAllNumeric(@NonNull String text) {
        if (text.isEmpty()) return false;

        for (char c : text.toCharArray()) if (!Character.isDigit(c)) return false;

        return true;
    }
}
