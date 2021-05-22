package com.rvngbrl.adulting.MoneyTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.rvngbrl.adulting.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SetBudget extends AppCompatActivity {
    Calendar calendar;
TextView setBudget;
 DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        datePicker = findViewById(R.id.date_picker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
    }
    public void setBudget(View view){
        setBudget=findViewById(R.id.setBdgt);
        datePicker = findViewById(R.id.date_picker);
        if( setBudget.getText().toString().isEmpty()){
            setBudget.setError( "Please input amount!" );
        }else{
            calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
          //  dateText.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(calendar.getTime()));
            String dateset,datestoday,startdate,enddate;
            dateset=new SimpleDateFormat("MMM-dd-yyyy").format(calendar.getTime());
            datestoday= new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault()).format(new Date()).toString();
            enddate=new SimpleDateFormat("MM/dd/yy").format(calendar.getTime());
            startdate= new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date()).toString();

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myBudget = sharedPreferences.edit();
            myBudget.putString("budget", setBudget.getText().toString());
            myBudget.putString("datebudget", dateset);
            myBudget.putString("datesetbdgt", datestoday);
            //for reading datedatabses
            myBudget.putString("startdate", startdate);
            myBudget.putString("enddate", enddate);
            myBudget.commit();

            Toast.makeText(this, "Successfully set your budget until "+dateset,
                    Toast.LENGTH_LONG).show();
//            Toast.makeText(this, datetoday,
//                    Toast.LENGTH_LONG).show();
            finish();
        }

    }
    public void cancelBudget(View view){
finish();

    }


}