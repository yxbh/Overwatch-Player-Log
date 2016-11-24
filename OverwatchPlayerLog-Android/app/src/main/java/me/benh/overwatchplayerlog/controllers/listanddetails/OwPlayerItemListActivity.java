package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordCreateActivity;
import me.benh.overwatchplayerlog.dummy.DummyContent;

/**
 * An activity representing a list of OwPlayerItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OwPlayerItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OwPlayerItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;

    private OwPlayerRecordRecyclerViewAdapter recordsViewAdapter;
    private RecyclerView recordsView;
    private ActionBarDrawerToggle drawerToggle;

    public boolean isTwoPane() {
        return isTwoPane;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owplayeritem_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // setup drawer toggle
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        // setup floating action buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwPlayerItemListActivity.this, OwPlayerRecordCreateActivity.class);
                startActivity(intent);
            }
        });

        // setup list view
        recordsView = (RecyclerView) findViewById(R.id.owplayeritem_list);
        assert recordsView != null;
        recordsViewAdapter = new OwPlayerRecordRecyclerViewAdapter(this, DummyContent.ITEMS);
        assert recordsViewAdapter != null;
        setupRecyclerView(recordsView, recordsViewAdapter);
        setupSwipeRefreshLayout(recordsViewAdapter);

        if (findViewById(R.id.owplayeritem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owplayeritem_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(this.getLocalClassName(), "onOptionsItemSelected");
        View menuView = this.getWindow().getDecorView().findViewById(R.id.content);
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

            case R.id.searchPlayer: {
                Snackbar.make(menuView, "Player search action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

            case R.id.removeAllPlayerRecords: {
                Snackbar.make(menuView, "Remove all player records action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

            case R.id.settings: {
                Snackbar.make(menuView, "Settings action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull OwPlayerRecordRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter); // TODO: replace with actual data source loading of OwPlayerRecords.

        // setup swipe gesture callback.
        new ItemTouchHelper(new OwPlayerRecordRecyclerViewItemGestureCallback(this, adapter)).attachToRecyclerView(recyclerView);
    }

    private void setupSwipeRefreshLayout(@NonNull final OwPlayerRecordRecyclerViewAdapter adapter) {
        final SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.owplayeritem_list_swipeRefreshLayout);
        assert layout != null;

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.swapData(DummyContent.ITEMS);
                layout.setRefreshing(false);
            }
        });
    }

}
