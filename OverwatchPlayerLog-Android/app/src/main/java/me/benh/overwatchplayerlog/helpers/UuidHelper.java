package me.benh.overwatchplayerlog.helpers;

import java.util.UUID;

/**
 * Created by Benjamin Huang on 22/11/2016.
 */

public final class UuidHelper {
    private UuidHelper() {}

    public static String NewUuidString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
