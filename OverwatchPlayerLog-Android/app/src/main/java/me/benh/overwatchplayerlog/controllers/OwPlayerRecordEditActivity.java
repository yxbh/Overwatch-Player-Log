package me.benh.overwatchplayerlog.controllers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import me.benh.lib.activities.BaseActivity;
import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.OwRegions;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.lib.helpers.SpinnerHelper;
import me.benh.overwatchplayerlog.helpers.AdapterHelper;
import me.benh.overwatchplayerlog.helpers.DrawableHelper;
import me.benh.overwatchplayerlog.helpers.PlayerTagHelper;

public class OwPlayerRecordEditActivity extends BaseActivity {
    public static final String TAG = OwPlayerRecordEditActivity.class.getSimpleName();

    private OwPlayerRecord record;

    private Menu        menu;

    private EditText    playerBattleTag;
    private CheckBox    playerFavorite;
    private ImageView   playerRatingView;
    private Spinner     playerPlatform;
    private Spinner     playerRegion;
    private EditText    playerNote;

    private Drawable playerRatingLikeDrawable;
    private Drawable playerRatingDislikeDrawable;
    private Drawable playerRatingNeutralDrawable;

    private OwPlayerRecord.Rating playerRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ow_player_record_edit);

        // setup view connections.
        playerBattleTag  = (EditText)  findViewById(R.id.player_battletag);
        playerFavorite   = (CheckBox)  findViewById(R.id.player_favorite);
        playerRatingView = (ImageView) findViewById(R.id.player_rating);
        playerPlatform   = (Spinner)   findViewById(R.id.player_platform);
        playerRegion     = (Spinner)   findViewById(R.id.player_region);
        playerNote       = (EditText)  findViewById(R.id.player_note);

        // get rating drawables.
        playerRatingNeutralDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_thumbs_up_down, null);
        playerRatingLikeDrawable = DrawableHelper.getDrawableWithTint(this, R.drawable.ic_thumb_up, android.R.color.holo_green_dark);
        playerRatingDislikeDrawable = DrawableHelper.getDrawableWithTint(this, R.drawable.ic_thumb_down, android.R.color.holo_red_dark);

        // get starting intent.
        Intent receivedIntent = getIntent();
        if (null == receivedIntent) {
            throw new RuntimeException("Missing start intent for " + getClass().getSimpleName());
        }
        record = ((OwPlayerRecordWrapper)receivedIntent.getParcelableExtra(Arguements.OWPLAYERRECORD)).getRecord();
        playerRating = record.getRating();
        Log.v(TAG, "Received " + record.toString());

        // setup listeners
        playerBattleTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* do nothing */ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMenuStates();
            }

            @Override
            public void afterTextChanged(Editable s) { /* do nothing */ }
        });
        playerRatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRating();
            }
        });
        playerPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "playerPlatform.onItemSelected(AdapterView<?> parent, View view, int position, long id)");
                String selectedPlatform = playerPlatform.getSelectedItem().toString();
                Log.v(TAG, "selected platform [" + selectedPlatform + "]");
                ArrayAdapter<String> adapter = AdapterHelper.createArrayAdapter(getApplicationContext(),OwRegions.getRegionsList(selectedPlatform));
                playerRegion.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                saveViewContentToRecord();
                if (!record.getBattleTag().isEmpty()) {
                    validatePlayerTag(record);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* do nothing. */ }
        });
        playerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = playerRegion.getSelectedItem().toString();
                Log.v(TAG, "selected region [" + selectedRegion + "]");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* do nothing. */ }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupViewContent(record);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!super.onCreateOptionsMenu(menu)) {
            return false;
        }

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owplayerrecord_save, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        updateMenuStates();
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.v(TAG, "case android.R.id.home");
                //NavUtils.navigateUpFromSameTask(this);
                ActivityHelper.finishWithCanceled(this);
                return true;

            case R.id.save: {
                Log.v(TAG, "case R.id.save");
                saveViewContentToRecord();

                // validate
                if (!validateRecord(record)) {
                    Log.v(TAG, "!validateRecord(newRecord)");
                    return true;
                }

                // save to data source.
                DataSource ds =  new DataSource(this);
                ds.updateOwPlayerRecord(record);
                ds.close();

                // return to calling activity.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Arguements.OWPLAYERRECORD, new OwPlayerRecordWrapper(record));
                ActivityHelper.finishWithSuccess(this, returnIntent);
                return true;
            }
        }

        return false;
    }

    private void setupViewContent(@NonNull OwPlayerRecord record) {
        if (playerBattleTag != null) {
            playerBattleTag.setText(record.getBattleTag());
        }

        if (playerFavorite != null) {
            playerFavorite.setChecked(record.isFavorite());
        }

        if (playerPlatform != null) {
            SpinnerHelper.selectSpinnerValue(playerPlatform, record.getPlatform());
        }

        if (playerRegion != null) {
            ArrayAdapter<String> adapter = AdapterHelper.createArrayAdapter(getApplicationContext(),OwRegions.getRegionsList(record.getPlatform()));
            playerRegion.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            SpinnerHelper.selectSpinnerValue(playerRegion, record.getRegion());
        }

        if (playerNote != null) {
            playerNote.setText(record.getNote());
        }

        if (null != playerRatingView) {
            switch (record.getRating()) {
                case Neutral: {
                    playerRatingView.setImageDrawable(playerRatingNeutralDrawable);
                    break;
                }

                case Like: {
                    playerRatingView.setImageDrawable(playerRatingLikeDrawable);
                    break;
                }

                case Dislike: {
                    playerRatingView.setImageDrawable(playerRatingDislikeDrawable);
                    break;
                }
            }
        }
    }

    private void saveViewContentToRecord() {
        record.setBattleTag(playerBattleTag.getText().toString());
        record.setFavorite(playerFavorite.isChecked());
        record.setRating(playerRating);
        record.setPlatform(playerPlatform.getSelectedItem().toString());
        record.setRegion(playerRegion.getSelectedItem().toString());
        record.setNote(playerNote.getText().toString());
    }

    private boolean validateRecord(@NonNull OwPlayerRecord record) {
        if (!validatePlayerTag(record)) {
            Log.v(TAG, "!validatePlayerTag(record)");
            return false;
        }

        if (!record.isValid()) {
            return false;
        }

        return true;
    }

    private boolean validatePlayerTag(@NonNull OwPlayerRecord record) {
        if (PlayerTagHelper.isInvalidTag(record)) {
            playerBattleTag.setError(getString(R.string.error_field_invalid_playertag));
            return false;
        } else {
            playerBattleTag.setError(null);
            return true;
        }
    }

    private void updateMenuStates() {
        Log.v(TAG, "updateMenuStates");
        if (null == menu) {
            Log.w(TAG, "null == menu");
            return;
        }

        MenuItem saveItem = menu.findItem(R.id.save);
        if (null == saveItem) {
            Log.e(TAG, "updateMenuStates: unable to find save menu item.");
            return;
        }

        Drawable saveItemIcon = saveItem.getIcon();
        boolean isReadyToSave = !playerBattleTag.getText().toString().isEmpty();

        if (isReadyToSave) {
            saveItem.setEnabled(true);
            saveItemIcon.mutate().setAlpha(getResources().getInteger(R.integer.menuIconEnabledAlpha));
        } else {
            saveItem.setEnabled(false);
            saveItemIcon.mutate().setAlpha(getResources().getInteger(R.integer.menuIconDisabledAlpha));
        }

        invalidateOptionsMenu();
    }

    private void toggleRating() {
        switch (playerRating) {
            case Neutral: {
                playerRatingView.setImageDrawable(playerRatingLikeDrawable);
                playerRating = OwPlayerRecord.Rating.Like;
                break;
            }

            case Like: {
                playerRatingView.setImageDrawable(playerRatingDislikeDrawable);
                playerRating = OwPlayerRecord.Rating.Dislike;
                break;
            }

            case Dislike: {
                playerRatingView.setImageDrawable(playerRatingNeutralDrawable);
                playerRating = OwPlayerRecord.Rating.Neutral;
                break;
            }
        }
    }
}
