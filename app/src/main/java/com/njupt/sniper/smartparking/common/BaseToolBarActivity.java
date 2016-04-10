package com.njupt.sniper.smartparking.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.njupt.sniper.smartparking.R;


public abstract class BaseToolBarActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    protected LeftIconTextView activityUpTitle;

    protected TextView activityTitle;

    protected TextView activityAction;

    protected abstract int getLayoutId();

    protected abstract String getToolbarUpTitle();

    protected abstract String getCurrentTitle();

    protected abstract String getMenuText();

    protected abstract void clickMenuSave();

    protected abstract boolean showMenuSave();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        setSupportActionBar(toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (!showMenuSave()) {
            activityAction.setVisibility(View.GONE);
        }

        activityAction.setText(getMenuText());

        activityAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMenuSave();
            }
        });

        activityTitle.setText(getCurrentTitle());


    }
}
