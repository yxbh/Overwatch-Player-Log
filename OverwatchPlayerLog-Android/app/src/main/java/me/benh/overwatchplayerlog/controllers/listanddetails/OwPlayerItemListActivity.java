package me.benh.overwatchplayerlog.controllers.listanddetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.Requests;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordCreateActivity;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.overwatchplayerlog.helpers.LogHelper;

/**
 * An activity representing a list of OwPlayerItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OwPlayerItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OwPlayerItemListActivity extends AppCompatActivity {

    public static final String TAG = OwPlayerItemListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;

    private OwPlayerRecordRecyclerViewAdapter recordsViewAdapter;
    private RecyclerView recordsView;

    private ActionBarDrawerToggle   drawerToggle;
    private DrawerLayout            drawerLayout;
    private SearchView              toolBarSearchView;

    private Spinner         toolBarTitleFilterSpinner;
    private ArrayAdapter    toolBarTitleFilterSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owplayeritem_list);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setup toolbar title spinner
        //   this is needed to set custom dropdown item style
        toolBarTitleFilterSpinner = (Spinner) findViewById(R.id.toolbar_title_spinner);
        toolBarTitleFilterSpinnerAdapter =
                ArrayAdapter.createFromResource(
                        getSupportActionBar().getThemedContext(),
                        R.array.activity_owplayeritem_list_titles,
                        R.layout.toolbar_title_spinner_item);
        toolBarTitleFilterSpinnerAdapter.setDropDownViewResource(R.layout.toolbar_title_spinner_dropdown_item);
        toolBarTitleFilterSpinner.setAdapter(toolBarTitleFilterSpinnerAdapter);
        toolBarTitleFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "selected [" + toolBarTitleFilterSpinner.getSelectedItem().toString() + "]");
                boolean showFavoriteOnly = toolBarTitleFilterSpinner.getSelectedItem().toString().toLowerCase().contains("favorite");
                recordsViewAdapter.setFavoriteOnly(showFavoriteOnly);
                recordsViewAdapter.notifyDataSetChanged();
            }
        });

        // setup drawer toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(drawerToggle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        drawerToggle.syncState();

        // setup floating action buttons
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwPlayerItemListActivity.this, OwPlayerRecordCreateActivity.class);
                startActivityForResult(intent, Requests.CREATE_NEW_RECORD);
            }
        });

        // setup list view
        recordsView = (RecyclerView) findViewById(R.id.owplayeritem_list);
        assert recordsView != null;
        recordsViewAdapter = new OwPlayerRecordRecyclerViewAdapter(this, new ArrayList<OwPlayerRecord>());
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

        refreshRecordsWithUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owplayeritem_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemSearch = menu.findItem(R.id.search_player);
        toolBarSearchView = (SearchView) menuItemSearch.getActionView();
        toolBarSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Log.v(TAG, "onQueryTextSubmit: [" + queryText + "]");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.v(TAG, "onQueryTextChange: [" + queryText + "]");
                recordsViewAdapter.filter(queryText);
                recordsViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
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

            case R.id.search_player: {
                // do nothing here since search is handled by the search view query listener.
                return true;
            }

            case R.id.remove_all_player_records: {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.alertdialog_remove_all_records_title)
                        .setMessage(getString(R.string.alertdialog_remove_all_records_message))
                        .setPositiveButton(R.string.alertdialog_remove_all_records_positive_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeAllRecordsWithUi();
                            }
                        })
                        .setNegativeButton(R.string.alertdialog_remove_all_records_negative_button, new DialogInterface.OnClickListener() {
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

//            case R.id.settings: { // TODO: implement settings.
//                Intent intent = new Intent(OwPlayerItemListActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                return true;
//            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close left side drawer is opened.
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // empty or iconify search view if searching.
        else if (!toolBarSearchView.isIconified()) {
            if (toolBarSearchView.getQuery().toString().isEmpty()) { // empty search query
                toolBarSearchView.setIconified(true); // close the search view.
            } else { // non-empty search query.
                toolBarSearchView.setQuery("", true); // empty the query.
            }
        } else {
            super.onBackPressed();
        }
    }

    public boolean isTwoPane() {
        return isTwoPane;
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
                refreshRecords();
                layout.setRefreshing(false);
            }
        });
    }

    public void refreshRecordsWithUi() {
        final SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.owplayeritem_list_swipeRefreshLayout);
        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(true);
                refreshRecords();
                layout.setRefreshing(false);
            }
        });
    }

    public void removeAllRecordsWithUi() {
        final SwipeRefreshLayout layout = (SwipeRefreshLayout) findViewById(R.id.owplayeritem_list_swipeRefreshLayout);
        layout.post(new Runnable() {
            @Override
            public void run() {
                layout.setRefreshing(true);
                DataSource ds = new DataSource(OwPlayerItemListActivity.this);
                ds.removeAllOwPlayerRecords();
                ds.close();
                recordsViewAdapter.removeAllItems();
                layout.setRefreshing(false);
            }
        });
    }

    private void refreshRecords() {
        DataSource ds = new DataSource(OwPlayerItemListActivity.this);
        List<OwPlayerRecord> allRecords = ds.getAllOwPlayerRecords();
        ds.close();
        recordsViewAdapter.swapData(allRecords);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        LogHelper.d_resultCode(TAG, resultCode);

        switch (requestCode) {
            case Requests.CREATE_NEW_RECORD: {
                Log.v(TAG, "REQUEST_CREATE_NEW_RECORD");
                if (resultCode == RESULT_OK) {
                    ActivityHelper.startDetailActivity(this, (OwPlayerRecordWrapper) data.getExtras().getParcelable(Arguements.OWPLAYERRECORD));
                }
                break;
            }

            case Requests.EDIT_RECORD: {
                Log.v(TAG, "REQUEST_EDIT_RECORD");
                if (resultCode == RESULT_OK) {
                    refreshRecordsWithUi();
                }
                break;
            }

            case Requests.VIEW_RECORD_DETAIL: {
                Log.v(TAG, "REQUEST_VIEW_RECORD_DETAIL");
                refreshRecordsWithUi();
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
