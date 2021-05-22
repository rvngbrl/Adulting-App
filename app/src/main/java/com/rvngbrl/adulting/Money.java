package com.rvngbrl.adulting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.rvngbrl.adulting.MoneyTracker.Fragments.CategoryFragment;
import com.rvngbrl.adulting.MoneyTracker.Fragments.ReportFragment;
import com.rvngbrl.adulting.MoneyTracker.Fragments.SettingsFragment;
import com.rvngbrl.adulting.MoneyTracker.Fragments.TodayFragment;

public class Money extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragments

    private CategoryFragment categoryFragment;
    private ReportFragment reportsFragment;
    private TodayFragment todayFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String scurrency = sh.getString("currency", "");

        if (scurrency.isEmpty()){
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myCurrency = sharedPreferences.edit();
            myCurrency.putString("currency", "PHP");
            myCurrency.commit();
        }




        bottomNavigationView=findViewById(R.id.bottomNavigationbar);
        frameLayout=findViewById(R.id.main_frame);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        todayFragment = new TodayFragment();
        reportsFragment = new ReportFragment();
        categoryFragment = new CategoryFragment();
        settingsFragment = new SettingsFragment();

        setFragment(todayFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.today:
                        setFragment(todayFragment);
                       // bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                        return true;
                    case R.id.report:
                        setFragment(reportsFragment);
                      //  bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                        return true;
                    case R.id.category:
                        setFragment(categoryFragment);
                      //  bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;
                    case R.id.settings:
                       setFragment(settingsFragment);
                        //bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                        return true;
                    default:
                        return false;


                }
            }
        });
    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();


    }

    public void displaySelectedListener(int itemId){

        Fragment fragment=null;

        switch (itemId){
            case R.id.today:
                fragment=new TodayFragment();
                break;
            case R.id.report:
                fragment=new ReportFragment();
                break;
            case R.id.category:
                fragment=new CategoryFragment();
                break;
            case R.id.settings:
               // fragment=new SettingsFragment();
                break;


        }
        if (fragment!=null){
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,fragment);
            fragmentTransaction.commit();

        }
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener(item.getItemId());
        return false;
    }
}