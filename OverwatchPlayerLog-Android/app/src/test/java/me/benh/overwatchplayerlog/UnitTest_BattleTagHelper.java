package me.benh.overwatchplayerlog;

import org.junit.Test;

import me.benh.overwatchplayerlog.helpers.BattleTagHelper;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest_BattleTagHelper {

    @Test
    public void test_isBattleTagWithoutId() throws Exception {
        assertTrue(BattleTagHelper.isTagWithoutId("AbCd"));

        assertFalse(BattleTagHelper.isTagWithoutId("WarXyZ#1183"));
        assertFalse(BattleTagHelper.isTagWithoutId("A1b2"));
        assertFalse(BattleTagHelper.isTagWithoutId("1234"));
        assertFalse(BattleTagHelper.isTagWithoutId("AbCasdf#"));
    }

    @Test
    public void test_isBattleTagWithId() throws Exception {
        assertTrue(BattleTagHelper.isTagWithId("AbCd#1231"));
        assertTrue(BattleTagHelper.isTagWithId("WarXyZ#1183"));

        assertFalse(BattleTagHelper.isTagWithId("AbCd"));
        assertFalse(BattleTagHelper.isTagWithId("A1b2"));
        assertFalse(BattleTagHelper.isTagWithId("1234"));
        assertFalse(BattleTagHelper.isTagWithId("AbCasdf#"));
        assertFalse(BattleTagHelper.isTagWithId("AbCasdf#12dwe"));
        assertFalse(BattleTagHelper.isTagWithId("#12"));
    }
}