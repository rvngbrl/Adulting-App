package com.rvngbrl.adulting.MoneyTracker.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rvngbrl.adulting.MoneyTracker.Activities.ExpenseEditActivity;
import com.rvngbrl.adulting.MoneyTracker.Adapter.SimpleExpenseAdapter;
import com.rvngbrl.adulting.MoneyTracker.Utils;
import com.rvngbrl.adulting.MoneyTracker.providers.ExpensesContract;
import com.rvngbrl.adulting.R;

import java.util.Date;

import com.rvngbrl.adulting.MoneyTracker.providers.ExpensesContract.Categories;
import com.rvngbrl.adulting.MoneyTracker.providers.ExpensesContract.Expenses;



public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int SUM_LOADER_ID = 0;
    private static final int LIST_LOADER_ID = 1;

    private ListView mExpensesView;
    private View mProgressBar;
    private SimpleExpenseAdapter mAdapter;
    private TextView mTotalExpSumTextView;
    private TextView mTotalExpCurrencyTextView;
    FloatingActionButton addExp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Today");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        mExpensesView = (ListView) rootView.findViewById(R.id.expenses_list_view);
        mProgressBar = rootView.findViewById(R.id.expenses_progress_bar);
        mTotalExpSumTextView = (TextView) rootView.findViewById(R.id.total_expense_sum_text_view);
        mTotalExpCurrencyTextView = (TextView) rootView.findViewById(R.id.total_expense_currency_text_view);

        addExp = rootView.findViewById(R.id.new_expense_add);
        addExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareExpenseToCreate();
            }
        });

        mExpensesView.setEmptyView(rootView.findViewById(R.id.expenses_empty_list_view));
        mExpensesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prepareExpenseToEdit(id);
            }
        });

        rootView.findViewById(R.id.add_expense_button_if_empty_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareExpenseToCreate();
            }
        });
        mTotalExpSumTextView.setText(Utils.formatToCurrency(0.0f));

        registerForContextMenu(mExpensesView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set default values for preferences (settings) on startup
      //  PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String scurrency = sh.getString("currency", "");
        if(scurrency.isEmpty()){
            SharedPreferences.Editor myCurrency = sh.edit();
            myCurrency.putString("currency", "USD");
            myCurrency.commit();
        }



        mAdapter = new SimpleExpenseAdapter(getActivity());
        mExpensesView.setAdapter(mAdapter);

        // Initialize the CursorLoaders
        getLoaderManager().initLoader(SUM_LOADER_ID, null, this);
        getLoaderManager().initLoader(LIST_LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadExpenseData();
        reloadSharedPreferences();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.fragment_today, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_expense_menu_item:
                prepareExpenseToCreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.expense_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_expense_menu_item:
                deleteExpense(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        switch (id) {
            case SUM_LOADER_ID:
          uri = ExpensesContract.ExpensesWithCategories.SUM_DATE_CONTENT_URI;
                break;
            case LIST_LOADER_ID:
                uri = ExpensesContract.ExpensesWithCategories.DATE_CONTENT_URI;
                break;
        }

        // Retrieve today's date string
        String today = Utils.getDateString(new Date());
        String[] selectionArgs = { today };

        return new CursorLoader(getActivity(),
                uri,
                null,
                null,
                selectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case SUM_LOADER_ID:
                int valueSumIndex = data.getColumnIndex(Expenses.VALUES_SUM);
                data.moveToFirst();
                float valueSum = data.getFloat(valueSumIndex);
                mTotalExpSumTextView.setText(Utils.formatToCurrency(valueSum));
                break;

            case LIST_LOADER_ID:
                // Hide the progress bar
                mProgressBar.setVisibility(View.GONE);

                mAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case SUM_LOADER_ID:
                mTotalExpSumTextView.setText(Utils.formatToCurrency(0.0f));
                break;
            case LIST_LOADER_ID:
                mAdapter.swapCursor(null);
                break;
        }
    }

    private void reloadSharedPreferences() {
       // SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
     //   String prefCurrency = sharedPref.getString(SettingsFragment.KEY_PREF_CURRENCY, "");
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String scurrency = sh.getString("currency", "");
        mTotalExpCurrencyTextView.setText(scurrency);
        mAdapter.setCurrency(scurrency);
    }

    private void reloadExpenseData() {
        // Show the progress bar
        mProgressBar.setVisibility(View.VISIBLE);
        // Reload data by restarting the cursor loaders
        getLoaderManager().restartLoader(LIST_LOADER_ID, null, this);
        getLoaderManager().restartLoader(SUM_LOADER_ID, null, this);
    }

    private int deleteSingleExpense(long expenseId) {
        Uri uri = ContentUris.withAppendedId(Expenses.CONTENT_URI, expenseId);

        // Defines a variable to contain the number of rows deleted
        int rowsDeleted;

        // Deletes the expense that matches the selection criteria
        rowsDeleted = getActivity().getContentResolver().delete(
                uri,        // the URI of the row to delete
                null,       // where clause
                null        // where args
        );

        showStatusMessage(getResources().getString(R.string.expense_deleted));
        reloadExpenseData();

        return rowsDeleted;
    }

    private void deleteExpense(final long expenseId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_expense)
                .setMessage(R.string.delete_exp_dialog_msg)
                .setNeutralButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.delete_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSingleExpense(expenseId);
                    }
                })
                .show();
    }

    private void showStatusMessage(CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private void prepareExpenseToCreate() {
        startActivity(new Intent(getActivity(), ExpenseEditActivity.class));
    }

    private void prepareExpenseToEdit(long id) {
        Intent intent = new Intent(getActivity(), ExpenseEditActivity.class);
        intent.putExtra(ExpenseEditFragment.EXTRA_EDIT_EXPENSE, id);
        startActivity(intent);
    }

}
