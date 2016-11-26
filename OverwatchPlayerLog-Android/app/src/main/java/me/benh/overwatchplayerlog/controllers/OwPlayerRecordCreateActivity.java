package me.benh.overwatchplayerlog.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import me.benh.overwatchplayerlog.R;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;
import me.benh.overwatchplayerlog.data.source.DataSource;
import me.benh.overwatchplayerlog.helpers.ActivityHelper;
import me.benh.overwatchplayerlog.helpers.BattleTagHelper;

/**
 * A login screen that offers login via email/password.
 */
public class OwPlayerRecordCreateActivity extends AppCompatActivity {

    public static final String TAG = OwPlayerRecordCreateActivity.class.getSimpleName();

    public static final String ARG_OWPLAYERRECORD = "owplayerrecord";

    // UI references.
    private EditText playerBattleTag;
    private CheckBox playerFavorite;
    private Spinner playerPlatform;
    private Spinner playerRegion;
    private EditText playerNote;

    private Menu menu;
    private View mProgressView;
    private View createFormView;

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
        playerFavorite = (CheckBox) findViewById(R.id.player_favorite);
        playerPlatform = (Spinner) findViewById(R.id.player_platform);
        playerRegion = (Spinner) findViewById(R.id.player_region);
        playerNote = (EditText) findViewById(R.id.player_note);

        createFormView = findViewById(R.id.create_form);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.save: {
                Log.v(TAG, "case R.id.save");

                // create new record object.
                OwPlayerRecord newRecord = new OwPlayerRecord();
                newRecord.setBattleTag(playerBattleTag.getText().toString());
                newRecord.setFavorite(playerFavorite.isChecked());
                newRecord.setPlatform(playerPlatform.getSelectedItem().toString());
                newRecord.setRegion(playerRegion.getSelectedItem().toString());
                newRecord.setNote(playerNote.getText().toString());

                // validate
                if (!newRecord.isValid()) {
                    if (BattleTagHelper.isInvalidBattleTag(newRecord.getBattleTag())) {
                        playerBattleTag.setError(getString(R.string.error_field_invalid_battletag));
                    }
                    return true;
                }

                // save to data source.
                Log.v(TAG, "Saving new record " + newRecord.toString());
                new DataSource(this).saveNewOwPlayerRecord(newRecord);

                // return to calling activity.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(newRecord));
                ActivityHelper.finishWithSuccess(this, returnIntent);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            createFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            createFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    createFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            createFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

