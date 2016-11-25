package me.benh.overwatchplayerlog.helpers;

import android.support.annotation.NonNull;

/**
 * Created by benhuang on 25/11/16.
 */

public final class BattleTagHelper {
    private BattleTagHelper() {}

    public static boolean isBattleTagWithId(@NonNull String battleTag) {
        if (battleTag.isEmpty()) {
            return false;
        }

        String[] battleTagComponents = battleTag.split("#");
        if (battleTagComponents.length != 2) return false;

        String playerTag = battleTagComponents[0];
        if (!isAllAlphabet(playerTag)) return false;

        String playerTagId = battleTagComponents[1];
        return isAllNumeric(playerTagId);
    }

    public static boolean isBattleTag(@NonNull String battleTag) {
        return !battleTag.isEmpty() && isAllAlphabet(battleTag);

    }

    private static boolean isAllAlphabet(@NonNull String text) {
        if (text.isEmpty()) return false;

        for (char c : text.toCharArray()) {
            if (Character.isDigit(c) || Character.isWhitespace(c) || !Character.isLetter(c))
                return false;
        }

        return true;
    }

    private static boolean isAllNumeric(@NonNull String text) {
        if (text.isEmpty()) return false;

        for (char c : text.toCharArray()) if (!Character.isDigit(c)) return false;

        return true;
    }
}
