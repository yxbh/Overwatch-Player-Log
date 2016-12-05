package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.Requests;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.lib.helpers.LogHelper;
import me.benh.overwatchplayerlog.helpers.AdHelper;
import me.benh.overwatchplayerlog.helpers.OwPlayerStatsSiteUrlHelper;
import me.benh.overwatchplayerlog.helpers.PlayerTagHelper;
import me.benh.overwatchplayerlog.web.WebChromeClient;
import me.benh.overwatchplayerlog.web.WebViewClient;

/**
 * An activity representing a single OwPlayerItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OwPlayerItemListActivity}.
 */
public class OwPlayerItemDetailActivity extends AppCompatActivity {

    public static final String TAG = OwPlayerItemDetailActivity.class.getSimpleName();

    private OwPlayerRecord playerRecord;

    private Toolbar appBar;
    private CollapsingToolbarLayout appBarLayout;
    private NestedScrollView nestedScrollView;

    private TextView playerPlatform;
    private TextView playerRegion;
    private TextView playerNote;
    private ImageView playerFavorite;
    private ImageView playerRatingLike;
    private ImageView playerRatingDislike;

    private WebView webViewPlayerStats;
    private ProgressBar webViewProgressPlayerStats;
    private WebChromeClient webChromeClient;
    private WebViewClient webViewClient;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owplayeritem_detail);

        // check for launch intent. finish activity with error if there isn't one.
        Intent launchIntent = getIntent();
        if (null == launchIntent) {
            Log.e(TAG, "null == launchIntent");
            ActivityHelper.finishWithError(this);
            return;
        }

        // get the player record item
        if (!launchIntent.hasExtra(Arguements.OWPLAYERRECORD)) {
            Log.e(TAG, "!launchIntent.hasExtra(Arguements.OWPLAYERRECORD)");
            ActivityHelper.finishWithError(this);
            return;
        }
        playerRecord = ((OwPlayerRecordWrapper) getIntent().getParcelableExtra(Arguements.OWPLAYERRECORD)).getRecord();
        Log.v(TAG, "Received " + playerRecord.toString());

        // setup view connections.
        nestedScrollView = (NestedScrollView) findViewById(R.id.owplayeritem_detail_container);
        appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        playerRegion = (TextView) findViewById(R.id.player_region);
        playerPlatform = (TextView) findViewById(R.id.player_platform);
        playerFavorite = (ImageView) findViewById(R.id.player_favorite);
        playerRatingLike = (ImageView) findViewById(R.id.player_rating_like);
        playerRatingDislike = (ImageView) findViewById(R.id.player_rating_dislike);
        playerNote = (TextView) findViewById(R.id.player_note);

        // setup webview for player stats
        webViewPlayerStats = (WebView) findViewById(R.id.webview_player_stats);
        webViewProgressPlayerStats = (ProgressBar) findViewById(R.id.webview_progress_player_stats);
        AdHelper.initMaybe(this);
        if (null != webViewPlayerStats && null != webViewProgressPlayerStats) {
            webViewPlayerStats.setBackgroundColor(Color.argb(1, 0, 0, 0));
            webViewPlayerStats.getSettings().setJavaScriptEnabled(true);

            webViewClient = new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    webViewProgressPlayerStats.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    webViewProgressPlayerStats.setVisibility(View.GONE);

                    if (!url.contains("javascript:")) {
                        Log.v(TAG, "Removing ads.");
                        String jsRemoveAds = "javascript:" +
                                "var adContainers = document.getElementsByClassName('ad-unit');" +
                                "for (var i =  0; i < adContainers.length; ++i) {" +
                                "   var adContainerParent = adContainers[i].parentNode;" +
                                "   adContainerParent.removeChild(adContainers[i]);" +
                                "   var elementParent = adContainerParent.parent;" +
                                "   var element = adContainerParent;" +
                                "   while (element !== undefined && !element.hasChildNodes()) { " +
                                "      elementParent.removeChild(element);" +
                                "      element = elementParent;" +
                                "      elementParent = elementParent.parent;" +
                                "   }" +
                                "}";
                        webViewPlayerStats.loadUrl(jsRemoveAds);
                    }

                    super.onPageFinished(view, url);
                }
            };
            webViewPlayerStats.setWebViewClient(webViewClient);

            webChromeClient = new WebChromeClient(new WebChromeClient.ProgressListener() {
                @Override
                public void onUpdateProgress(int progressValue) {
                    webViewProgressPlayerStats.setProgress(progressValue);
                }
            });
            webViewPlayerStats.setWebChromeClient(webChromeClient);
        }

        // setup fab for stats sites
        FabSpeedDial fabPlayerStatsSites = (FabSpeedDial) findViewById(R.id.fab_open_stats_sites);
        if (null != fabPlayerStatsSites) {
            fabPlayerStatsSites.setMenuListener(new SimpleMenuListenerAdapter() {
                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    return OwPlayerItemDetailActivity.this.onFabOptionsItemSelected(menuItem);
                }
            });
        }

        // setup tool bar
        appBar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(appBar);

        // setup view content
        setupViewContent(playerRecord);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owplayer_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);

        // hide stats links if the battle tag is not well formed.
        menu.setGroupVisible(R.id.menu_group_stats, PlayerTagHelper.isValidTag(playerRecord));
        menu.setGroupEnabled(R.id.menu_group_stats, PlayerTagHelper.isValidTag(playerRecord));

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(this.getLocalClassName(), "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                //navigateUpTo(new Intent(this, OwPlayerItemListActivity.class));
                ActivityHelper.finishWithCanceled(this);
                return true;
            }

            case R.id.menu_remove_owplayer_record: {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.alertdialog_remove_record_title)
                        .setMessage(getString(R.string.alertdialog_remove_record_message))
                        .setPositiveButton(R.string.alertdialog_remove_record_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSource ds = new DataSource(OwPlayerItemDetailActivity.this);
                                ds.removeOwPlayerRecord(playerRecord);
                                ds.close();
                                ActivityHelper.finishWithSuccess(OwPlayerItemDetailActivity.this);
                            }
                        })
                        .setNegativeButton(R.string.alertdialog_remove_record_negative_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .create();
                alertDialog.show();
                return true;
            }

            case R.id.menu_edit_owplayer_record: {
                ActivityHelper.startEditActivity(OwPlayerItemDetailActivity.this, playerRecord);
                return true;
            }

            case R.id.menu_stats_playeroverwatch: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlPlayOverwatch(playerRecord));
                return true;
            }

            case R.id.menu_stats_masteroverwatch: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlMasterOverwatch(playerRecord));
                return true;
            }

            case R.id.menu_stats_overbuff: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlOverbuff(playerRecord));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        LogHelper.d_resultCode(TAG, resultCode);

        switch (requestCode) {
            case Requests.EDIT_RECORD: {
                Log.v(TAG, "REQUEST_EDIT_RECORD");
                if (resultCode == RESULT_OK) {
                    playerRecord = ((OwPlayerRecordWrapper) data.getParcelableExtra(Arguements.OWPLAYERRECORD)).getRecord();
                    Log.v(TAG, "Received " + playerRecord.toString());

                    setupViewContent(playerRecord);
                }
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onFabOptionsItemSelected(MenuItem item) {
        Log.v(this.getLocalClassName(), "onFabOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_edit_owplayer_record: {
                ActivityHelper.startEditActivity(this, playerRecord);
                return true;
            }

            case R.id.menu_stats_playeroverwatch: {
                loadUrl(OwPlayerStatsSiteUrlHelper.getUrlPlayOverwatch(this.playerRecord));
                return true;
            }

            case R.id.menu_stats_masteroverwatch: {
                loadUrl(OwPlayerStatsSiteUrlHelper.getUrlMasterOverwatch(this.playerRecord));
                return true;
            }

            case R.id.menu_stats_overbuff: {
                loadUrl(OwPlayerStatsSiteUrlHelper.getUrlOverbuff(this.playerRecord));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUrl(String url) {
        nestedScrollView.fullScroll(View.FOCUS_UP);
        webViewPlayerStats.loadUrl("about:blank");
        webViewPlayerStats.loadUrl(url);
    }

    private void setupViewContent(@NonNull OwPlayerRecord record) {
        Log.v(TAG, "setupViewContent(@NonNull OwPlayerRecord record)");

        if (null != appBarLayout) {
            appBarLayout.setTitle(playerRecord.getBattleTag());
        }

        if (null != playerRegion) {
            playerRegion.setText(playerRecord.getRegion());
        }

        if (null != playerPlatform) {
            playerPlatform.setText(playerRecord.getPlatform());
        }

        if (null != playerFavorite) {
            playerFavorite.setVisibility(playerRecord.isFavorite() ? View.VISIBLE : View.GONE);
        }

        if (null != playerRatingLike) {
            playerRatingLike.setVisibility(playerRecord.getRating() == OwPlayerRecord.Rating.Like ? View.VISIBLE : View.GONE);
        }

        if (null != playerRatingDislike) {
            playerRatingDislike.setVisibility(playerRecord.getRating() == OwPlayerRecord.Rating.Dislike ? View.VISIBLE : View.GONE);
        }

        if (record.getRating() == OwPlayerRecord.Rating.Neutral) {
            findViewById(R.id.horizontal_spacer_1).setVisibility(View.GONE);
        }

        if (null != playerNote) {
            playerNote.setText(playerRecord.getNote().replace("\n", "; "));
            if (playerRecord.getNote().isEmpty()) {
                findViewById(R.id.collapsing_layoutput_content_row2).setVisibility(View.GONE);
            }
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                loadUrl(OwPlayerStatsSiteUrlHelper.getUrlPlayOverwatch(playerRecord));
            }
        });
    }
}
