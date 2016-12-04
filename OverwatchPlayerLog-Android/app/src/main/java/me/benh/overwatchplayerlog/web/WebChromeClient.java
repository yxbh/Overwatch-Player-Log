package me.benh.overwatchplayerlog.web;

import android.webkit.WebView;

/**
 * Created by Benjamin Huang on 4/12/2016.
 */

public class WebChromeClient extends android.webkit.WebChromeClient {
    private ProgressListener progressListener;

    public WebChromeClient(ProgressListener listener) {
        progressListener = listener;
    }



    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (null != progressListener) {
            progressListener.onUpdateProgress(newProgress);
        }

        super.onProgressChanged(view, newProgress);
    }

    public interface ProgressListener {

        public void onUpdateProgress(int progressValue);
    }
}
