package me.benh.overwatchplayerlog;

import org.junit.Test;

import me.benh.lib.helpers.UuidHelper;

import static org.junit.Assert.assertFalse;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest_UuidHelper {

    @Test
    public void test_generation() throws Exception {
        String uuid = UuidHelper.newUuidString();

        assertFalse(uuid.contains("-"));
        assertFalse(uuid.contains("{"));
        assertFalse(uuid.contains("}"));

        assertFalse(uuid.equals(UuidHelper.newUuidString()));
    }

}