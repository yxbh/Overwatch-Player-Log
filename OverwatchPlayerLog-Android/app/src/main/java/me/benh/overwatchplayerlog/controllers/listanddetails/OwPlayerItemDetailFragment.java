package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.lib.helpers.LogHelper;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.overwatchplayerlog.helpers.OwPlayerStatsSiteUrlHelper;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single OwPlayerItem detail screen.
 * This fragment is either contained in a {@link OwPlayerItemListActivity}
 * in two-pane mode (on tablets) or a {@link OwPlayerItemDetailActivity}
 * on handsets.
 */
public class OwPlayerItemDetailFragment extends Fragment {
    public static final String TAG = OwPlayerItemDetailFragment.class.getSimpleName();

    /**
     * The fragment argument representing the {@link OwPlayerRecord} that this fragment
     * represents.
     */
    public static final String ARG_OWPLAYERRECORD = "owplayerrecord";

    /**
     * The dummy content this fragment is presenting.
     */
    private OwPlayerRecord item;

    private static final int REQUEST_EDIT_RECORD = 999;

    CollapsingToolbarLayout appBarLayout;
    WebView webViewPlayerStats;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OwPlayerItemDetailFragment() {
    }

    public void setupViewContent(@NonNull OwPlayerRecord record) {
        if (appBarLayout != null) {
            appBarLayout.setTitle(record.getBattleTag());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_OWPLAYERRECORD)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            item = ((OwPlayerRecordWrapper) getArguments().getParcelable(ARG_OWPLAYERRECORD)).getRecord();

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.owplayeritem_detail, container, false);

        webViewPlayerStats = (WebView) rootView.findViewById(R.id.webview_player_stats);
        if (null != webViewPlayerStats) {
            webViewPlayerStats.getSettings().setJavaScriptEnabled(true);
        }

        setupViewContent(item);

        // setup floating action buttons
        // setup fab for stats sites
        FabSpeedDial fabPlayerStatsSites = (FabSpeedDial) rootView.findViewById(R.id.fab_open_stats_sites);
        if (null != fabPlayerStatsSites) {
            fabPlayerStatsSites.setMenuListener(new SimpleMenuListenerAdapter() {
                @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    return OwPlayerItemDetailFragment.this.onOptionsItemSelected(menuItem);
                }
            });
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_edit_owplayer_record: {
                ActivityHelper.startEditActivity(getActivity(), this.item);
                return true;
            }

            case R.id.menu_stats_playeroverwatch: {
                webViewPlayerStats.loadUrl(OwPlayerStatsSiteUrlHelper.getUrlPlayOverwatch(this.item));
                return true;
            }

            case R.id.menu_stats_masteroverwatch: {
                webViewPlayerStats.loadUrl(OwPlayerStatsSiteUrlHelper.getUrlMasterOverwatch(this.item));
                return true;
            }

            case R.id.menu_stats_overbuff: {
                webViewPlayerStats.loadUrl(OwPlayerStatsSiteUrlHelper.getUrlOverbuff(this.item));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        LogHelper.d_resultCode(TAG, resultCode);

        switch (requestCode) {
            case REQUEST_EDIT_RECORD: {
                Log.v(TAG, "REQUEST_EDIT_RECORD");
                if (resultCode == RESULT_OK) {
                    item = ((OwPlayerRecordWrapper) data.getParcelableExtra(Arguements.OWPLAYERRECORD)).getRecord();
                    setupViewContent(item);
                }
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
