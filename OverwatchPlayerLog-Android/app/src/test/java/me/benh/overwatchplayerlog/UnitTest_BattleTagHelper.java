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
    public void test_isBattleTag() throws Exception {
        assertTrue(BattleTagHelper.isBattleTag("AbCd"));

        assertFalse(BattleTagHelper.isBattleTag("A1b2"));
        assertFalse(BattleTagHelper.isBattleTag("1234"));
        assertFalse(BattleTagHelper.isBattleTag("AbCasdf#"));
    }

    @Test
    public void test_isBattleTagWithId() throws Exception {
        assertTrue(BattleTagHelper.isBattleTagWithId("AbCd#1231"));

        assertFalse(BattleTagHelper.isBattleTagWithId("AbCd"));
        assertFalse(BattleTagHelper.isBattleTagWithId("A1b2"));
        assertFalse(BattleTagHelper.isBattleTagWithId("1234"));
        assertFalse(BattleTagHelper.isBattleTagWithId("AbCasdf#"));
        assertFalse(BattleTagHelper.isBattleTagWithId("AbCasdf#12dwe"));
        assertFalse(BattleTagHelper.isBattleTagWithId("#12"));
    }
}