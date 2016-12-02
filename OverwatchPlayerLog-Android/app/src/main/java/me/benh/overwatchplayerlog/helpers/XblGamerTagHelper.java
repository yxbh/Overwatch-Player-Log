package me.benh.overwatchplayerlog.helpers;

import android.support.annotation.NonNull;

/**
 * Created by benhuang on 25/11/16.
 */

public final class XblGamerTagHelper {
    private XblGamerTagHelper() {}

    public static boolean isValidTag(@NonNull String tag) {
        if (tag.isEmpty()) {
            return false;
        }

        char[] chars = tag.toCharArray();

        // first character must not be a digit, symbol or whitespace.
        if (!Character.isLetter(chars[0])) {
            return false;
        }

        for (int i = 1; i < chars.length; ++i) {
            if (!Character.isLetterOrDigit(chars[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isInvalidTag(@NonNull String tag) {
        return !isValidTag(tag);
    }
}
