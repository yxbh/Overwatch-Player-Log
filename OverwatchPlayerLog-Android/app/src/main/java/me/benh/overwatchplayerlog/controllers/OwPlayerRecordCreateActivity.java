package me.benh.overwatchplayerlog.controllers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import me.benh.lib.activities.BaseActivity;
import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.OwRegions;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.overwatchplayerlog.helpers.AdapterHelper;
import me.benh.lib.helpers.DrawableHelper;
import me.benh.overwatchplayerlog.helpers.PlayerTagHelper;

/**
 * A login screen that offers login via email/password.
 */
public class OwPlayerRecordCreateActivity extends BaseActivity {

    public static final String TAG = OwPlayerRecordCreateActivity.class.getSimpleName();

    // UI references.
    private EditText    playerBattleTag;
    private CheckBox    playerFavorite;
    private ImageView   playerRatingView;
    private Spinner     playerPlatform;
    private Spinner     playerRegion;
    private EditText    playerNote;

    private Menu menu;
    private View mProgressView;
    private View createFormView;

    private Drawable playerRatingLikeDrawable;
    private Drawable playerRatingDislikeDrawable;
    private Drawable playerRatingNeutralDrawable;

    private OwPlayerRecord.Rating playerRating = OwPlayerRecord.Rating.Neutral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ow_player_record_create);

        // Set up the create form.
        playerBattleTag = (EditText) findViewById(R.id.player_battletag);
        playerBattleTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.player_battletag || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
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
        playerFavorite   = (CheckBox)  findViewById(R.id.player_favorite);
        playerRatingView = (ImageView) findViewById(R.id.player_rating);
        playerPlatform   = (Spinner)   findViewById(R.id.player_platform);
        playerRegion     = (Spinner)   findViewById(R.id.player_region);
        playerNote       = (EditText)  findViewById(R.id.player_note);
        createFormView   = findViewById(R.id.create_form);

        // setup listeners
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

                OwPlayerRecord record = createRecordFromForm();
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

        // get rating drawables.
        playerRatingNeutralDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_thumbs_up_down, null);
        playerRatingLikeDrawable = DrawableHelper.getDrawableWithTint(this, R.drawable.ic_thumb_up, android.R.color.holo_green_dark);
        playerRatingDislikeDrawable = DrawableHelper.getDrawableWithTint(this, R.drawable.ic_thumb_down, android.R.color.holo_red_dark);
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
//                NavUtils.navigateUpFromSameTask(this);
                ActivityHelper.finishWithCanceled(this);
                return true;

            case R.id.save: {
                Log.v(TAG, "case R.id.save");
                playerBattleTag.setText(playerBattleTag.getText().toString().trim());
                OwPlayerRecord newRecord = createRecordFromForm();

                // validate
                if (!validateRecord(newRecord)) {
                    Log.v(TAG, "!validateRecord(newRecord)");
                    return true;
                }

                // save to data source.
                Log.v(TAG, "Saving new record " + newRecord.toString());
                DataSource ds = new DataSource(this);
                ds.saveNewOwPlayerRecord(newRecord);
                ds.close();

                // return to calling activity.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Arguements.OWPLAYERRECORD, new OwPlayerRecordWrapper(newRecord));
                ActivityHelper.finishWithSuccess(this, returnIntent);
                return true;
            }
        }

        return false;
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

    private OwPlayerRecord createRecordFromForm() {
        OwPlayerRecord newRecord = new OwPlayerRecord();
        newRecord.setBattleTag(playerBattleTag.getText().toString());
        newRecord.setFavorite(playerFavorite.isChecked());
        newRecord.setRating(playerRating);
        newRecord.setPlatform(playerPlatform.getSelectedItem().toString());
        newRecord.setRegion(playerRegion.getSelectedItem().toString());
        newRecord.setNote(playerNote.getText().toString());

        return newRecord;
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
                playerRatingView.setColorFilter(ResourcesCompat.getColor(getResources(), android.R.color.holo_green_dark, null));
                playerRating = OwPlayerRecord.Rating.Like;
                break;
            }

            case Like: {
                playerRatingView.setImageDrawable(playerRatingDislikeDrawable);
                playerRatingView.setColorFilter(ResourcesCompat.getColor(getResources(), android.R.color.holo_red_dark, null));
                playerRating = OwPlayerRecord.Rating.Dislike;
                break;
            }

            case Dislike: {
                playerRatingView.setImageDrawable(playerRatingNeutralDrawable);
                playerRatingView.setColorFilter(null);
                playerRating = OwPlayerRecord.Rating.Neutral;
                break;
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        playerBattleTag.setError(null);

        // Store values at the time of the login attempt.
        String email = playerBattleTag.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            playerBattleTag.setError(getString(R.string.error_field_required));
            focusView = playerBattleTag;
            cancel = true;
        }
    }


}

