package com.rvngbrl.adulting.MoneyTracker.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.rvngbrl.adulting.MoneyTracker.Utils;
import com.rvngbrl.adulting.MoneyTracker.providers.ExpensesContract;
import com.rvngbrl.adulting.R;

import java.util.HashMap;

public class SimpleExpenseAdapter extends CursorAdapter {
    private String mCurrency;

    public SimpleExpenseAdapter(Context context) {
        super(context, null, 0);
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
        notifyDataSetChanged();
    }

    // The newView method is used to inflate a new view and return it
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expense_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template

        TextView tvExpenseNote = (TextView) view.findViewById(R.id.expense_note_text_view) ;
        TextView tvExpenseValue = (TextView) view.findViewById(R.id.expense_value_text_view);
       // TextView tvExpenseCurrency = (TextView) view.findViewById(R.id.expense_currency_text_view);
        TextView tvExpenseCatName = (TextView) view.findViewById(R.id.expense_category_name_text_view);

      //  final HashMap<String, String> singleTask = data.get(position);
        // Extract values from cursor
        float expValue = cursor.getFloat(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.VALUE));
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Categories.NAME));
     String NoteCat = cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.NOTE));

        // Populate views with extracted values
        String Money = Utils.formatToCurrency(expValue);
        tvExpenseValue.setText(Money+"\n"+mCurrency);
        tvExpenseCatName.setText(categoryName);
   //     tvExpenseCurrency.setText(mCurrency);
       tvExpenseNote.setText(NoteCat);
    }
}
