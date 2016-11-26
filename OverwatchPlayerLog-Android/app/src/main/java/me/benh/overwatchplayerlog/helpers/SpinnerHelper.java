package me.benh.overwatchplayerlog.helpers;

import android.widget.Spinner;

import java.security.InvalidParameterException;

/**
 * Created by benhuang on 26/11/16.
 */

public final class SpinnerHelper {
    private SpinnerHelper() {}

    public static void selectSpinnerValue(Spinner spinner, String text)
    {
        for(int i = 0; i < spinner.getCount(); ++i){
            if (spinner.getItemAtPosition(i).toString().equals(text)){
                spinner.setSelection(i);
                return;
            }
        }

        throw new InvalidParameterException("Invalid spinner text [" + text + "]");
    }
}
