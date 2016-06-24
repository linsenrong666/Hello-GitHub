package com.linsr.dumpling.gui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.linsr.dumpling.log.Log;
import com.linsr.dumpling.log.LogImpl;

/**
 * description
 *
 * @author Linsr
 */
public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected String TAG = this.getClass().getSimpleName();
    protected Log mLog = LogImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setTitleText(CharSequence titleText) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleText);
        }
    }

    public void setTitleText(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resId);
        }
    }

    protected void displayBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
