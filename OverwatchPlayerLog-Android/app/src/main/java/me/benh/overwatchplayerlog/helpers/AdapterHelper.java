package me.benh.overwatchplayerlog.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import me.benh.overwatchplayerlog.R;

/**
 * Created by Benjamin Huang on 3/12/2016.
 */

public final class AdapterHelper {
    private AdapterHelper() {}

    public static <T> ArrayAdapter<T> createArrayAdapter(Context context, List<T> list) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(context, R.layout.default_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
