package me.benh.overwatchplayerlog.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.benh.overwatchplayerlog.R;

/**
 * Created by Benjamin Huang on 4/12/2016.
 */

public class AdHelper {
    private static String TAG = AdHelper.class.getSimpleName();

    private static List<String> sWebAdHostNames;

    private AdHelper() {}

    public static boolean isAd(@NonNull String urlText) {
        URL url;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL.");
            e.printStackTrace();
            return false;
        }

        return isAdHost(url.getHost());
    }

    public static boolean isAd(@NonNull URL url) {
        return isAdHost(url.getHost());
    }

    public static boolean isAd(@NonNull Uri uri) {
        return isAdHost(uri.getHost() == null ? "" : uri.getHost());
    }

    public static boolean isAd(@NonNull Context context, @NonNull String url) {
        initMaybe(context);
        return isAd(url);
    }

    public static boolean isAdHost(@NonNull String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        int index = host.indexOf(".");
        return index >= 0 && (sWebAdHostNames.contains(host) ||
               index + 1 < host.length() && isAdHost(host.substring(index + 1)));
    }

    public static void initMaybe(@NonNull Context context) {
        if (null != sWebAdHostNames) {
            return;
        }
        sWebAdHostNames = new ArrayList<>();

        InputStream stream = context.getResources().openRawResource(R.raw.web_ad_hosts);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        do {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                Log.e(TAG, "Error reading ad hosts file.");
                e.printStackTrace();
                break;
            }

            if (null == line) {
                break;
            }

            sWebAdHostNames.add(line);
        } while (true);

        try {
            reader.close();
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing resources");
            e.printStackTrace();
        }
    }

}
