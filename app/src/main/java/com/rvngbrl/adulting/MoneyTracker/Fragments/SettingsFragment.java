package com.rvngbrl.adulting.MoneyTracker.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.rvngbrl.adulting.Money;
import com.rvngbrl.adulting.MoneyTracker.Activities.SelectCurrency;
import com.rvngbrl.adulting.MoneyTracker.Activities.SetBudget;
import com.rvngbrl.adulting.R;
import com.rvngbrl.adulting.purchase;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView currencyTV,budgetTV,remindTV;
    String freminder,sreminder,finalreminder;
    int count = 1;
    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inputFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);


// for Currency
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String scurrency = sh.getString("currency", "");
        currencyTV=inputFragmentView.findViewById(R.id.changeCur);
        currencyTV.setText(scurrency);
// for Budget
        String budget = sh.getString("budget", "");
        String dateBdgt = sh.getString("datebudget", "");
        String lastSetBdgt = sh.getString("datesetbdgt", "");
        budgetTV=inputFragmentView.findViewById(R.id.changeBudget);
        remindTV=inputFragmentView.findViewById(R.id.remindBudget);

        if (budget.isEmpty()){
            budgetTV.setText("Set Budget First");
          finalreminder ="";
            remindTV.setVisibility(View.INVISIBLE);
        }else{
            budgetTV.setText(scurrency+" "+budget);
            freminder ="You set your budget last "+ lastSetBdgt;
            sreminder = " and valid until "+ dateBdgt;
           finalreminder =freminder+sreminder;
            remindTV.setText(finalreminder);

        }
        //for budget reminder



        CardView cardView1 =inputFragmentView.findViewById(R.id.chooseCur);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //INTENT
                Intent intent1 = new Intent(getActivity(), SelectCurrency.class);
                startActivity(intent1);
            }
        });
        CardView cardView2 = inputFragmentView.findViewById(R.id.clickBudget);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INTENT

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
                String StatPurchase = sharedPreferences.getString("purchaseStat", "");
                if (StatPurchase.equals("Purchased")){
                    Intent intent2 = new Intent(getActivity(), SetBudget.class);
                    startActivity(intent2);
                }else{
                    Intent intentBill2 = new Intent(getActivity(), purchase.class);
                    startActivity(intentBill2);
                }

            }
        });

        return inputFragmentView;
    }

    @Override
    public void onResume() {
        ///use this when reloading the fragments
        super.onResume();
        content();
    }
    public void content(){
///use this when reloading the fragments
        // Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String scurrency = sh.getString("currency", "");
        String budget = sh.getString("budget", "");
        String dateBdgt = sh.getString("datebudget", "");
        String lastSetBdgt = sh.getString("datesetbdgt", "");
        currencyTV.setText(scurrency);
        budgetTV.setText(scurrency+" "+budget);
        freminder ="You set your budget last "+ lastSetBdgt;
        sreminder = " and valid until "+ dateBdgt;
        finalreminder =freminder+sreminder;
        remindTV.setText(finalreminder);

        refresh(1000);
    }

    public void refresh(int millisecond){
///use this when reloading the fragments
        final Handler handler= new Handler();
        final Runnable runnable= new Runnable() {
            @Override
            public void run() {
                content();

            }
        };
    }

    public void Reload(){

        getActivity().getSupportFragmentManager().beginTransaction().replace(SettingsFragment.this.getId(), new SettingsFragment()).commit();
    }
}
