package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordEditActivity;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.dummy.DummyContent;

/**
 * An activity representing a single OwPlayerItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link OwPlayerItemListActivity}.
 */
public class OwPlayerItemDetailActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_RECORD = 999;

    private OwPlayerRecord playerRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owplayeritem_detail);

        // get the player record item
        playerRecord = DummyContent.ITEM_MAP.get(getIntent().getStringExtra(OwPlayerItemDetailFragment.ARG_ITEM_ID));

        // setup tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // set activity title
        setTitle(playerRecord.getBattleTag());
        TextView actionBarPlayerRegion = (TextView) findViewById(R.id.player_region);
        if (null != actionBarPlayerRegion) {
            actionBarPlayerRegion.setText(playerRecord.getRegion());
        }
        TextView actionBarPlayerPlatform = (TextView) findViewById(R.id.player_platform);
        if (null != actionBarPlayerPlatform) {
            actionBarPlayerPlatform.setText(playerRecord.getPlatform());
        }

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
                    Intent intent = new Intent(OwPlayerItemDetailActivity.this, OwPlayerRecordEditActivity.class);
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
            arguments.putString(OwPlayerItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(OwPlayerItemDetailFragment.ARG_ITEM_ID));
            OwPlayerItemDetailFragment fragment = new OwPlayerItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.owplayeritem_detail_container, fragment)
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
                navigateUpTo(new Intent(this, OwPlayerItemListActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
