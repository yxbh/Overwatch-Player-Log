package me.benh.overwatchplayerlog.web;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.io.ByteArrayInputStream;

import me.benh.overwatchplayerlog.helpers.AdHelper;

/**
 * Created by Benjamin Huang on 4/12/2016.
 */

public class WebViewClient extends android.webkit.WebViewClient {
    private static final String TAG = WebViewClient.class.getSimpleName();

    boolean isBlockingAd = true;

    public boolean isBlockingAd() {
        return isBlockingAd;
    }

    public void setBlockingAd(boolean blockingAd) {
        isBlockingAd = blockingAd;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Log.v(TAG, "shouldOverrideUrlLoading");
        if (isBlockingAd() && AdHelper.isAd(url)) {
            Log.v(TAG, "ad url detected [" + url + "]");
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        Log.v(TAG, "shouldInterceptRequest");
        if (isBlockingAd() && AdHelper.isAd(url)) {
            Log.v(TAG, "ad url detected [" + url + "]");
            return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
        }

        return super.shouldInterceptRequest(view, url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (isBlockingAd() && AdHelper.isAd(request.getUrl())) {
            Log.v(TAG, "ad url detected [" + request.getUrl() + "]");
            return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
        }

        return super.shouldInterceptRequest(view, request);
    }
}
