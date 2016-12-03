package me.benh.overwatchplayerlog.helpers;

import android.support.annotation.NonNull;

import java.security.InvalidParameterException;

import me.benh.overwatchplayerlog.common.OwPlatforms;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Created by benhuang on 25/11/16.
 */

public final class PlayerTagHelper {
    private PlayerTagHelper() {}

    public static boolean isValidTag(@NonNull OwPlayerRecord record) {
        if (record.getPlatform().equals(OwPlatforms.PC)) {
            return BattleTagHelper.isValidTag(record.getBattleTag());
        } else if (record.getPlatform().equals(OwPlatforms.XBL)) {
            return XblGamerTagHelper.isValidTag(record.getBattleTag());
        } else if (record.getPlatform().equals(OwPlatforms.PSN)) {
            return PsnGamerTagHelper.isValidTag(record.getBattleTag());
        }

        throw new InvalidParameterException("Unsupported platform [" + record.getPlatform() + "]");
    }

    public static boolean isInvalidTag(@NonNull OwPlayerRecord record) {
        return !isValidTag(record);
    }
}
