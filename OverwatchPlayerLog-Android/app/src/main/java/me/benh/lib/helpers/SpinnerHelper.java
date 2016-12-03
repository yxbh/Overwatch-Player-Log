package me.benh.lib.helpers;

import android.support.annotation.NonNull;
import android.widget.Spinner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benhuang on 26/11/16.
 */

public final class SpinnerHelper {
    private SpinnerHelper() {}

    public static void selectSpinnerValue(@NonNull final Spinner spinner, String text)
    {
        for(int i = 0; i < spinner.getCount(); ++i){
            if (spinner.getItemAtPosition(i).toString().equals(text)){
                spinner.setSelection(i, true);
//                final int pos = i;
//                spinner.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        spinner.setSelection(pos, true);
//                    }
//                });
                return;
            }
        }

        throw new InvalidParameterException("Invalid spinner text [" + text + "], spinner has " + getAllSpinnerItemsAsStrings(spinner).toString() + "");
    }

    public static List<String> getAllSpinnerItemsAsStrings(@NonNull Spinner spinner) {
        ArrayList<String> items = new ArrayList<>();
        for(int i = 0; i < spinner.getCount(); ++i){
            items.add(spinner.getItemAtPosition(i).toString());
        }

        return items;
    }
}
