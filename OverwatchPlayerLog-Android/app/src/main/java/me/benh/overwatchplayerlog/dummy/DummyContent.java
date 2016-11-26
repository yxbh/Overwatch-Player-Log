package me.benh.overwatchplayerlog.dummy;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<OwPlayerRecord> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, OwPlayerRecord> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static final List<OwPlayerRecord> getItems() {
        return ITEMS;
    }

    private static void addItem(OwPlayerRecord item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static OwPlayerRecord createDummyItem(int position) {
        OwPlayerRecord newRecord = new OwPlayerRecord();
        newRecord.setBattleTag("BattleTag#" + String.valueOf(position));
        newRecord.setPlatform("PC");
        newRecord.setRegion("US");
        newRecord.setRecordCreateDatetime(new Date(Calendar.getInstance().getTime().getTime()));
        newRecord.setRecordLastUpdateDatetime(new Date(Calendar.getInstance().getTime().getTime()));

        return newRecord;
    }

}
