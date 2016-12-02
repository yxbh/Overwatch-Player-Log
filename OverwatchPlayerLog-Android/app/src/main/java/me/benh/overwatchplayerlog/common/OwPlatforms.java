package me.benh.overwatchplayerlog.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin Huang on 2/12/2016.
 */

public final class OwPlatforms {
    private OwPlatforms() {}

    public static final String PC = "PC";
    public static final String XBL = "XBL";
    public static final String PSN = "PSN";

    public static final String[] ALL_PLATFORMS = { PC, XBL, PSN };
    public static final List<String> ALL_PLATFORMS_LIST = new ArrayList<String>(){{ add(PC); add(XBL); add(PSN); }};
}
