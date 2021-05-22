package com.rvngbrl.adulting.MoneyTracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rvngbrl.adulting.MainActivity;
import com.rvngbrl.adulting.Money;
import com.rvngbrl.adulting.MoneyTracker.Fragments.SettingsFragment;
import com.rvngbrl.adulting.R;

public class SelectCurrency extends AppCompatActivity {
    ListView lv;
    String[] listCurrency={"US Dollar","Philippine Peso","Euro","Japanese Yen","Canadian Dollar","British Pound","Swiss Franc","Australian Dollar","South African Rand","New Zealand Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_currency);

        lv= (ListView)findViewById(R.id.listCur);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listCurrency);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String crncySlctd = "USD";

                if (position == 0){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="USD";
                }
                else if (position == 1){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="PHP";
                }
                else if (position == 2){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="EUR";
                }

                else if (position == 3){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="JPY";
                }

                else if (position == 4){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="CAD";
                }

                else if (position == 5){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="GBP";
                }
                else if (position == 6){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="CHF";
                }

                else if (position == 7){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="AUD";
                }

                else if (position == 8){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="ZAR";
                }
                else if (position == 9){
                    Toast.makeText(getApplicationContext(),""+lv.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                    crncySlctd ="NZD";
                }

// Storing data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myCurrency = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
                myCurrency.putString("currency", crncySlctd);

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
                myCurrency.commit();

                finish();




            }
        });

    }
    public void cancelBudget(View view){
        finish();

    }



}