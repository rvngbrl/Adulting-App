package com.rvngbrl.adulting.MoneyTracker.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rvngbrl.adulting.MoneyTracker.Utils;

import com.rvngbrl.adulting.MoneyTracker.providers.ExpensesContract;
import com.rvngbrl.adulting.R;
import com.twotoasters.sectioncursoradapter.SectionCursorAdapter;


public class SectionExpenseAdapter extends SectionCursorAdapter {
    private String mCurrency;
   Context mContext;
    public SectionExpenseAdapter(Context context) {
        super(context, null, 0);
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
        notifyDataSetChanged();
    }

    @Override
    protected Object getSectionFromCursor(Cursor cursor) {
//        String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.DATE));
       Log.d("DATE///////",cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.DATE)));
        return Utils.getSystemFormatDateString(mContext, cursor.getString(cursor.getColumnIndexOrThrow(ExpensesContract.Expenses.DATE)));

    }

    @Override
    protected View newSectionView(Context context, Object item, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.expense_report_section_header, parent, false);
    }

    @Override
    protected void bindSectionView(View convertView, Context context, int position, Object item) {
        ((TextView) convertView).setText((String) item);
    }

    @Override
    protected View newItemView(Context context, Cursor cursor, ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.expense_list_item, parent, false);
    }

    @Override
    protected void bindItemView(View convertView, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvExpenseNote = (TextView) convertView.findViewById(R.id.expense_note_text_view) ;
        TextView tvExpenseValue = (TextView) convertView.findViewById(R.id.expense_value_text_view);
      //  TextView tvExpenseCurrency = (TextView) convertView.findViewById(R.id.expense_currency_text_view);
        TextView tvExpenseCatName = (TextView) convertView.findViewById(R.id.expense_category_name_text_view);


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
