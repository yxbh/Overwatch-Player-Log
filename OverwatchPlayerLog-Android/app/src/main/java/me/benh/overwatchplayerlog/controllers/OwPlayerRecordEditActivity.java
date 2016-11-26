package me.benh.overwatchplayerlog.controllers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.overwatchplayerlog.helpers.BattleTagHelper;
import me.benh.overwatchplayerlog.helpers.SpinnerHelper;

public class OwPlayerRecordEditActivity extends AppCompatActivity {
    public static final String TAG = OwPlayerRecordEditActivity.class.getSimpleName();

    public static final String ARG_OWPLAYERRECORD = "owplayerrecord";

    private OwPlayerRecord record;

    Menu menu;
    EditText playerBattleTag;
    Spinner playerPlatform;
    Spinner playerRegion;
    EditText playerNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ow_player_record_edit);

        // setup view connections.
        playerBattleTag = (EditText) findViewById(R.id.player_battletag);
        playerPlatform = (Spinner) findViewById(R.id.player_platform);
        playerRegion = (Spinner) findViewById(R.id.player_region);
        playerNote = (EditText) findViewById(R.id.player_note);

        // get starting intent.
        Intent receivedIntent = getIntent();
        if (null == receivedIntent) {
            throw new RuntimeException("Missing start intent for " + getClass().getSimpleName());
        }
        record = ((OwPlayerRecordWrapper)receivedIntent.getParcelableExtra(ARG_OWPLAYERRECORD)).getRecord();
        Log.v(TAG, "Received " + record.toString());

        setupViewContent(record);

        // setup listeners
        playerBattleTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMenuStates();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owplayerrecord_save, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuStates();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.v(TAG, "case android.R.id.home");
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            case R.id.save: {
                Log.v(TAG, "case R.id.save");

                saveViewContentToRecord();

                // validate
                if (!record.isValid()) {
                    if (BattleTagHelper.isInvalidBattleTag(record.getBattleTag())) {
                        playerBattleTag.setError(getString(R.string.error_field_invalid_battletag));
                    }
                    return true;
                }

                // save to data source.
                new DataSource(this).updateOwPlayerRecord(record);

                // return to calling activity.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(record));
                ActivityHelper.finishWithSuccess(this, returnIntent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewContent(@NonNull OwPlayerRecord record) {
        if (playerBattleTag != null) {
            playerBattleTag.setText(record.getBattleTag());
        }

        if (playerPlatform != null) {
            SpinnerHelper.selectSpinnerValue(playerPlatform, record.getPlatform());
        }

        if (playerRegion != null) {
            SpinnerHelper.selectSpinnerValue(playerRegion, record.getRegion());
        }

        if (playerNote != null) {
            playerNote.setText(record.getNote());
        }
    }

    private void saveViewContentToRecord() {
        record.setBattleTag(playerBattleTag.getText().toString());
        record.setPlatform(playerPlatform.getSelectedItem().toString());
        record.setRegion(playerRegion.getSelectedItem().toString());
        record.setNote(playerNote.getText().toString());
    }

    private void updateMenuStates() {
        MenuItem saveItem = menu.findItem(R.id.save);
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
}
