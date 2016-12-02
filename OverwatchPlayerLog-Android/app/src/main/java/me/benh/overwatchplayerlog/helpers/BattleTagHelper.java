package me.benh.overwatchplayerlog.helpers;

import android.support.annotation.NonNull;

/**
 * Created by benhuang on 25/11/16.
 */

public final class BattleTagHelper {
    private BattleTagHelper() {}

    public static boolean isTagWithId(@NonNull String battleTag) {
        if (battleTag.isEmpty()) {
            return false;
        }

        String[] battleTagComponents = battleTag.split("#");
        if (battleTagComponents.length != 2) return false;

        String playerTag = battleTagComponents[0];
        if (!StringHelper.isAllAlphabet(playerTag)) return false;

        String playerTagId = battleTagComponents[1];
        return StringHelper.isAllNumeric(playerTagId);
    }

    public static boolean isTagWithoutId(@NonNull String battleTag) {
        return !battleTag.isEmpty() && StringHelper.isAllAlphabet(battleTag);

    }

    public static boolean isValidTag(@NonNull String battleTag) {
        return !isInvalidTag(battleTag);
    }

    public static boolean isInvalidTag(@NonNull String battleTag) {
        return !(isTagWithId(battleTag) || isTagWithoutId(battleTag));
    }
}
