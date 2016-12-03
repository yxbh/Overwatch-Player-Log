package me.benh.overwatchplayerlog.helpers;

import me.benh.overwatchplayerlog.common.OwPlatforms;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Created by Benjamin Huang on 1/12/2016.
 */

public class OwPlayerStatsSiteUrlHelper {
    public static String getUrlPlayOverwatch(OwPlayerRecord record) {
        if (record.getPlatform().equals(OwPlatforms.PC)) {
            if (BattleTagHelper.isTagWithoutId(record.getBattleTag())) {
                return "https://playoverwatch.com/en-us/search?q=" + record.getBattleTag().replace("#", "-");
            } else {
                return "https://playoverwatch.com/en-us/career/" + record.getPlatform().toLowerCase() + "/" + record.getRegion().toLowerCase() + "/" + record.getBattleTag().replace("#", "-");
            }
        }
        return "https://playoverwatch.com/en-us/career/" + record.getPlatform().toLowerCase() + "/" + record.getBattleTag().replace("#", "-");
    }

    public static String getUrlMasterOverwatch(OwPlayerRecord record) {
        if (record.getPlatform().equals(OwPlatforms.PC) && BattleTagHelper.isTagWithoutId(record.getBattleTag())) {
            return "http://masteroverwatch.com/search/" + record.getBattleTag().replace("#", "-");
        }
        return "http://masteroverwatch.com/profile/" + record.getPlatform() + "/" + record.getRegion() + "/" + record.getBattleTag().replace("#", "-");
    }

    public static String getUrlOverbuff(OwPlayerRecord record) {
        if (record.getPlatform().equals(OwPlatforms.PC) && BattleTagHelper.isTagWithoutId(record.getBattleTag())) {
            return "https://www.overbuff.com/search?q=" + record.getBattleTag().replace("#", "-");
        }
        return "https://www.overbuff.com/players/" + record.getPlatform() + "/" + record.getBattleTag().replace("#", "-");
    }
}
