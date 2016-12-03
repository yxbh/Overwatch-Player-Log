package me.benh.lib.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import me.benh.lib.helpers.LogHelper;

/**
 * Created by Benjamin Huang on 3/12/2016.
 */

public class BaseActivity extends AppCompatActivity {

    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(getActivityName(), "onCreate(@Nullable Bundle savedInstanceState)");

        super.onCreate(savedInstanceState);

        // setup top level view listener for snackbar dismissal
        View rootView = findViewById(android.R.id.content);
        if (null != rootView) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    BaseActivity.this.dismissSnackbarMaybe();
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(getActivityName(), "onCreateOptionsMenu(Menu menu)");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(getActivityName(), "onPrepareOptionsMenu(Menu menu)");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(getActivityName(), "onOptionsItemSelected(Menu menu)");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.v(getActivityName(), "onOptionsMenuClosed(Menu menu)");
        super.onOptionsMenuClosed(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(getActivityName(), "onActivityResult(int requestCode, int resultCode, Intent data)");
        LogHelper.d_resultCode(getActivityName(), resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getActivityName() {
        return this.getLocalClassName();
    }

    public void setSnackbar(Snackbar snackbar) {
        if (null != this.snackbar) {
            this.clearSnackbar();
        }

        this.snackbar = snackbar;
    }

    public boolean hasSnackbar() {
        return null != this.snackbar;
    }

    public Snackbar getSnackbar() {
        return this.snackbar;
    }

    public void dismissSnackbarMaybe() {
        if (this.hasSnackbar()) {
            this.getSnackbar().dismiss();
        }
    }

    public void clearSnackbar() {
        if (null == this.snackbar) {
            return;
        }

        this.snackbar.dismiss();
        this.snackbar = null;
    }
}
