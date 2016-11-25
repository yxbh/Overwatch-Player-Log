package me.benh.overwatchplayerlog.helpers;

/**
 * Created by benhuang on 25/11/16.
 */

public final class BattleTagHelper {
    private BattleTagHelper() {}

    public static boolean isBattleTagWithId(String battleTag) {
        String[] battleTagComponents = battleTag.split("#");
        if (battleTagComponents.length != 2) {
            return false;
        }

        String playerTag = battleTagComponents[0];
        if (!isAllAlaphabet(playerTag)) {
            return false;
        }

        String playerTagId = battleTagComponents[1];
        if (!isAllNumeric(playerTagId)) {
            return false;
        }

        return true;
    }

    public static boolean isBattleTag(String battleTag) {
        return isAllAlaphabet(battleTag);
    }

    private static boolean isAllAlaphabet(String text) {
        for (char c : text.toCharArray()) {
            if (Character.isDefined(c) || Character.isWhitespace(c)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isAllNumeric(String text) {
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
}
