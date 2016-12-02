package me.benh.overwatchplayerlog.common;

import android.support.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Benjamin Huang on 2/12/2016.
 */

public final class OwRegions {
    private OwRegions() {}

    public static final String US = "US";
    public static final String EU = "EU";
    public static final String CN = "KR";
    public static final String KR = "CN";
    public static final String GLOBAL = "Global";

    public static final String[] PC_REGIONS = { US, EU, KR, CN };
    public static final String[] XBL_REGIONS = { US, EU, KR };
    public static final String[] PSN_REGIONS = { GLOBAL };

    public static final List<String> PC_REGIONS_LIST = new ArrayList<String>() {{ add(US); add(EU); add(KR); add(CN); }};
    public static final List<String> XBL_REGIONS_LIST = new ArrayList<String>() {{ add(GLOBAL); }};
    public static final List<String> PSN_REGIONS_LIST = new ArrayList<String>() {{ add(GLOBAL); }};

    public static final String[] getRegions(@NonNull String platform) {
        if (platform.equals(OwPlatforms.PC)) {
            return PC_REGIONS;
        } else if (platform.equals(OwPlatforms.PSN)) {
            return PSN_REGIONS;
        } else if (platform.equals(OwPlatforms.XBL)) {
            return XBL_REGIONS;
        } else {
            throw new InvalidParameterException("Invalid platform [" + platform + "]");
        }
    }

    public static final List<String> getRegionsList(@NonNull String platform) {
        if (platform.equals(OwPlatforms.PC)) {
            return PC_REGIONS_LIST;
        } else if (platform.equals(OwPlatforms.PSN)) {
            return XBL_REGIONS_LIST;
        } else if (platform.equals(OwPlatforms.XBL)) {
            return PSN_REGIONS_LIST;
        } else {
            throw new InvalidParameterException("Invalid platform [" + platform + "]");
        }
    }
}
