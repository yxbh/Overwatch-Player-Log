package me.benh.overwatchplayerlog.helpers;

import me.benh.overwatchplayerlog.common.OwPlatforms;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Created by Benjamin Huang on 1/12/2016.
 */

public class OwPlayerStatsSiteUrlHelper {
    public static String getUrlPlayOverwatch(OwPlayerRecord record) {
        if (record.getPlatform().equals(OwPlatforms.PC)) {
            return "https://playoverwatch.com/en-us/career/" + record.getPlatform().toLowerCase() + "/" + record.getRegion().toLowerCase() + "/" + record.getBattleTag().replace("#", "-");
        }
        return "https://playoverwatch.com/en-us/career/" + record.getPlatform().toLowerCase() + "/" + record.getBattleTag().replace("#", "-");
    }

    public static String getUrlMasterOverwatch(OwPlayerRecord record) {
        return "http://masteroverwatch.com/profile/" + record.getPlatform() + "/" + record.getRegion() + "/" + record.getBattleTag().replace("#", "-");
    }

    public static String getUrlOverbuff(OwPlayerRecord record) {
        return "https://www.overbuff.com/players/" + record.getPlatform() + "/" + record.getBattleTag().replace("#", "-");
    }
}
