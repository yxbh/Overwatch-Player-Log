package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordEditActivity;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.helpers.LogHelper;

/**
 * An activity representing a single OwPlayerItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OwPlayerItemListActivity}.
 */
public class OwPlayerItemDetailActivity extends AppCompatActivity {

    public static final String TAG = OwPlayerItemDetailActivity.class.getSimpleName();

    private static final int REQUEST_EDIT_RECORD = 999;

    private OwPlayerRecord playerRecord;

    OwPlayerItemDetailFragment detailFragment;

    TextView playerPlatform;
    TextView playerRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owplayeritem_detail);

        // get the player record item
        playerRecord = ((OwPlayerRecordWrapper) getIntent().getParcelableExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD)).getRecord();
        Log.v(TAG, "Received " + playerRecord.toString());

        // setup view connections.
        playerRegion = (TextView) findViewById(R.id.player_region);
        playerPlatform = (TextView) findViewById(R.id.player_platform);

        // setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // setup view content
        setupViewContent(playerRecord);

        // Setup menu listener.
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onMenuItemClick(item);
            }
        });

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
                    Intent intent = new Intent(OwPlayerItemDetailActivity.this, OwPlayerRecordEditActivity.class);
                    intent.putExtra(OwPlayerRecordEditActivity.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(playerRecord));
                    startActivityForResult(intent, REQUEST_EDIT_RECORD);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(this.getLocalClassName(), "onOptionsItemSelected");
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
                finish();
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
            case REQUEST_EDIT_RECORD: {
                Log.v(TAG, "REQUEST_EDIT_RECORD");
                if (resultCode == RESULT_OK) {
                    playerRecord = ((OwPlayerRecordWrapper) data.getParcelableExtra(OwPlayerRecordEditActivity.ARG_OWPLAYERRECORD)).getRecord();
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
    }
}
