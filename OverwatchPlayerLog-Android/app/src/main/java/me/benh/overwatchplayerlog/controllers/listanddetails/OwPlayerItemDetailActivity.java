package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.Requests;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.lib.helpers.LogHelper;
import me.benh.overwatchplayerlog.helpers.OwPlayerStatsSiteUrlHelper;
import me.benh.overwatchplayerlog.helpers.PlayerTagHelper;

/**
 * An activity representing a single OwPlayerItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OwPlayerItemListActivity}.
 */
public class OwPlayerItemDetailActivity extends AppCompatActivity {

    public static final String TAG = OwPlayerItemDetailActivity.class.getSimpleName();

    private OwPlayerRecord playerRecord;

    OwPlayerItemDetailFragment detailFragment;

    TextView playerPlatform;
    TextView playerRegion;
    ImageView playerFavorite;
    ImageView playerRatingLike;
    ImageView playerRatingDislike;

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
        if (!launchIntent.hasExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD)) {
            Log.e(TAG, "!launchIntent.hasExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD)");
            ActivityHelper.finishWithError(this);
            return;
        }
        playerRecord = ((OwPlayerRecordWrapper) getIntent().getParcelableExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD)).getRecord();
        Log.v(TAG, "Received " + playerRecord.toString());

        // setup view connections.
        playerRegion = (TextView) findViewById(R.id.player_region);
        playerPlatform = (TextView) findViewById(R.id.player_platform);
        playerFavorite = (ImageView) findViewById(R.id.player_favorite);
        playerRatingLike = (ImageView) findViewById(R.id.player_rating_like);
        playerRatingDislike = (ImageView) findViewById(R.id.player_rating_dislike);

        // setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // setup view content
        setupViewContent(playerRecord);

//        // Setup menu listener.
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                return onOptionsItemSelected(item);
//            }
//        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // setup floating action buttons
        FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        if (null != fabEdit) {
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "onClick");
                    ActivityHelper.startEditActivity(OwPlayerItemDetailActivity.this, playerRecord);
                }
            });
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD,
                    getIntent().getParcelableExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD));
            detailFragment = new OwPlayerItemDetailFragment();
            detailFragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.owplayeritem_detail_container, detailFragment)
                    .commit();
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
                break;
            }

            case R.id.menu_stats_playeroverwatch: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlPlayOverwatch(playerRecord));
                break;
            }

            case R.id.menu_stats_masteroverwatch: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlMasterOverwatch(playerRecord));
                break;
            }

            case R.id.menu_stats_overbuff: {
                ActivityHelper.startUrlActivity(this, OwPlayerStatsSiteUrlHelper.getUrlOverbuff(playerRecord));
                break;
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

    private void setupViewContent(@NonNull OwPlayerRecord record) {
        setTitle(playerRecord.getBattleTag());

        if (null != playerRegion) {
            playerRegion.setText(playerRecord.getRegion());
        }

        if (null != playerPlatform) {
            playerPlatform.setText(playerRecord.getPlatform());
        }

        if (null != detailFragment) {
            detailFragment.setupViewContent(playerRecord);
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
    }
}
