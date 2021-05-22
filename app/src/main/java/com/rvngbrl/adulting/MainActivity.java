package com.rvngbrl.adulting;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.rvngbrl.adulting.MoneyTracker.Activities.SelectCurrency;
import com.rvngbrl.adulting.MoneyTracker.Activities.SetBudget;
import com.rvngbrl.adulting.MoneyTracker.ExpenseDbHelper;
import com.rvngbrl.adulting.MoneyTracker.Fragments.DatePickerFragment;
import com.rvngbrl.adulting.MoneyTracker.Utils;
import com.rvngbrl.adulting.TaskTracker.MyDatabaseHelper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView bdgtRmndr;
    TextView totalBdgt;
    Double totalbudget;
    TextView nameUser;
    DrawerLayout drawer;
    NavigationView navigationView ;
    Toolbar toolbar;
    AdView mAdView;
    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        remainingBudget();
        final View dialogView = View.inflate(this, R.layout.dialog_getname, null);
        final EditText editText = dialogView.findViewById(R.id.getName);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        String name="Arvin";
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if( editText.getText().toString().isEmpty()){
                    editText.setError( "Please enter your name" );
                }else{

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myName = sharedPreferences.edit();
                    myName.putString("userName", editText.getText().toString());
                    myName.commit();
                }


            }
        });
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String Username= sh.getString("userName", "");
        if(Username.isEmpty()){
            builder.show();
        }
        else{

            nameUser=(TextView)findViewById(R.id.UserName);
            String builderName = "Hi "+Username;
            nameUser.setText(builderName+"!");


        }

        MobileAds.initialize(this, String.valueOf(R.string.AdID)); //input your id
        mAdView=(AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        //For Amazon Remove when update in Google Play
//        SharedPreferences.Editor myPurchase = sharedPreferences.edit();
//        myPurchase.putString("purchaseStat", "Purchased");
//        myPurchase.commit();

        String StatPurchase = sharedPreferences.getString("purchaseStat", "");
        if (StatPurchase.equals("Purchased")){
            mAdView.setVisibility(View.GONE);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) drawerLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        //Navigation Drawer Code
         drawer = findViewById(R.id.drawer_layout);
   navigationView = findViewById(R.id.nav_view);
       toolbar = findViewById(R.id.toolbarmain);

        ///////Toolbar//////
        setSupportActionBar(toolbar);

        /////////////////////////Navigation////////
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setItemIconTintList(null);





    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.isDrawerOpen(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            //For Amazon Remove when update in Google Play
            case R.id.nav_purchase:
                Intent intentBill = new Intent(this, purchase.class);
                startActivity(intentBill);
                break;
            case R.id.nav_friend:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Adulting - Task and Money Manager.");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.rvngbrl.adulting" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.nav_about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.nav_changeName:
                final View dialogView = View.inflate(this, R.layout.dialog_getname, null);
                final EditText editText = dialogView.findViewById(R.id.getName);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                String name="Arvin";
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if( editText.getText().toString().isEmpty()){
                            editText.setError( "Please enter your name" );
                        }else{

                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myName = sharedPreferences.edit();
                            myName.putString("userName", editText.getText().toString());
                            myName.commit();
                        }
                        Toast.makeText(getApplicationContext(),"Please Restart the Application to take effect.",Toast.LENGTH_SHORT).show();


                    }
                });
                builder.show();
                break;
            case R.id.nav_changeCurrency:
                //INTENT
                Intent intent1 = new Intent(this, SelectCurrency.class);
                startActivity(intent1);

                break;
            case R.id.nav_changeBudget:
                SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPref",MODE_PRIVATE);
                String StatPurchase = sharedPreferences.getString("purchaseStat", "");
               if (StatPurchase.equals("Purchased")){
                   Intent intent2 = new Intent(this, SetBudget.class);
                    startActivity(intent2);

                }else{
                    Intent intentBill2 = new Intent(this, purchase.class);
                    startActivity(intentBill2);
                }
                break;

        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        ///use this when reloading the fragments
        super.onResume();
       onRestart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void ToDoClick(View view) {
//        Date currentTime = Calendar.getInstance().getTime();
//
//        DateFormat date = new SimpleDateFormat("HH:mm a");
//        String localTime = date.format(currentTime);
//
//        Toast.makeText(this,localTime,
//                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ToDo.class);
        startActivity(intent);
    }

    public void moneyClick(View view) throws ParseException {
        Intent intent = new Intent(this, Money.class);
        startActivity(intent);

    }

    private void createNotificationChannel(){

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            CharSequence name = "RvnGbrlReminder";
            String description = "Reminder for ToDo Features";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyrvngbrl",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        remainingBudget();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void remainingBudget(){
        bdgtRmndr=findViewById(R.id.budgetReminder);
        totalBdgt=findViewById(R.id.textRBudget);
        SharedPreferences sh = this.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String scurrency = sh.getString("currency", "");
        String budget = sh.getString("budget", "");
        String dateBdgt = sh.getString("datebudget", "");
        String lastSetBdgt = sh.getString("datesetbdgt", "");

        if (budget.isEmpty()){
            bdgtRmndr.setText("Please set your budget first in Money Settings.");
            totalBdgt.setVisibility(View.INVISIBLE);
        }
        else{ bdgtRmndr.setText("Remaining Budget Until: "+dateBdgt);
        //need to update:change it to fload
            totalBdgt.setText(budget);}

        //ALERT TIME
        try{
            SQLiteDatabase tododb = getApplicationContext().openOrCreateDatabase("ToDoDBHelper.db",Context.MODE_PRIVATE,null);
            Cursor cursor = tododb.rawQuery(" Select * from task_mngr where task_at = DATE('now')  and status ='0' ORDER by task_at ASC",null);
            if(cursor.getCount()==0){}
            // StringBuffer buffer = new StringBuffer();
            if (cursor.moveToNext()){
                Intent intent = new Intent(MainActivity.this,ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent,0);

                AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenSecondsInMillis = 1000*10;

                alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClick + tenSecondsInMillis,pendingIntent);

            }
        }catch(Exception error1) {
            Log.e("Error", "The exception caught while executing the process. (error1)");
            error1.printStackTrace();
        }

        //Shared Pref for Getting Date
        String enddate= sh.getString("enddate", "");
        String startdate = sh.getString("startdate", "");
        if (!startdate.isEmpty()){
            SQLiteDatabase expensedb = getApplicationContext().openOrCreateDatabase("expense_tracer.db",Context.MODE_PRIVATE,null);
            Cursor cursor2 = expensedb.rawQuery("Select Sum(value) as 'Budget' from expenses where date between '" + startdate + "' AND  '"+ enddate +"' ORDER BY Date ASC ",null);
            if(cursor2.getCount()==0){}
            // StringBuffer buffer = new StringBuffer();
            if (cursor2.moveToNext()){
                //  buffer.append("Budget"+cursor.getString(0));
                String spend =cursor2.getString(0);

                if (spend == null) {
                    spend="0.00";
                }
                totalbudget=  Double.parseDouble(budget)-Double.parseDouble(spend);
                totalBdgt.setText(scurrency+" "+totalbudget);
                if (  totalbudget<=0.00){
                    totalBdgt.setTextColor(Color.RED);
                }

                try {

                    Calendar calendar = Calendar.getInstance();
                 //   System.out.println("Current Date = " + calendar.getTime());
                    // Decrementing days by 2
                    calendar.add(Calendar.DATE, -1);
                  //  System.out.println("Updated Date = " + calendar.getTime());


                    if (new SimpleDateFormat("MM/dd/yy").parse(enddate).before(calendar.getTime())) {
                        bdgtRmndr.setText("Your Budget Date Expired. \nYou Save:");

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }



        String Username= sh.getString("userName", "");


            nameUser=(TextView)findViewById(R.id.UserName);
            String builderName = "Hi "+Username;
            nameUser.setText(builderName+"!");


    }



}