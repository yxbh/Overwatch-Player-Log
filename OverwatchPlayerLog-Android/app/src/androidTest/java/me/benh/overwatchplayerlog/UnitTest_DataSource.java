package me.benh.overwatchplayerlog;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.benh.overwatchplayerlog.data.source.DataSource;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTest_DataSource {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("me.benh.overwatchplayerlog", appContext.getPackageName());
    }

    @Test
    public void test_methodCalls() throws Exception {
        DataSource ds = new DataSource(InstrumentationRegistry.getTargetContext());

        ds.hasOwPlayerRecordId("dummy");
        ds.getAllOwPlayerRecords();
        ds.getOwPlayerRecordWithId("jhb");
    }
}
