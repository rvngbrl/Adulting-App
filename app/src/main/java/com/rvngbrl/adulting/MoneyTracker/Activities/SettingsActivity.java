package com.rvngbrl.adulting.MoneyTracker.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.rvngbrl.adulting.MoneyTracker.Fragments.SettingsFragment;
import com.rvngbrl.adulting.R;

public class SettingsActivity extends BaseFragment {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   //  insertFragment(new SettingsFragment());

      //  setTitle(R.string.nav_settings);

        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar (toolbar).
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
