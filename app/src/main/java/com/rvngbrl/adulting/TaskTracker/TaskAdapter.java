package com.rvngbrl.adulting.TaskTracker;


import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rvngbrl.adulting.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TaskAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private MyDatabaseHelper database;
    String date;

    public TaskAdapter(Activity a, ArrayList<HashMap<String, String>> d, MyDatabaseHelper mydb) {
        activity = a;
        data = d;
        database = mydb;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewHolder holder = null;
        if (convertView == null) {
            holder = new TaskViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.task_list, parent, false);
            holder.task_name = convertView.findViewById(R.id.task_name);
            holder.task_date = convertView.findViewById(R.id.task_date);
            holder.checkBtn = convertView.findViewById(R.id.checkBtn);
            convertView.setTag(holder);
        } else {
            holder = (TaskViewHolder) convertView.getTag();
        }


        final HashMap<String, String> singleTask = data.get(position);
        final TaskViewHolder tmpHolder = holder;

        holder.task_name.setId(position);
        holder.task_date.setId(position);
        holder.checkBtn.setId(position);

        String strCurrentDate= singleTask.get("date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("dd-MMM-yyyy");
            date = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {


            holder.checkBtn.setOnCheckedChangeListener(null);
            if (singleTask.get("status").contentEquals("1")) {
                holder.task_name.setText(Html.fromHtml("<strike>" + singleTask.get("task") + "</strike>"));
                holder.task_date.setText(Html.fromHtml("<strike>" + singleTask.get("date") + "</strike>"));
                holder.checkBtn.setChecked(true);

            } else {
                holder.task_name.setText(singleTask.get("task"));
                holder.task_date.setText(date);
                holder.checkBtn.setChecked(false);
            }

            holder.checkBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        database.updateTaskStatus(singleTask.get("id"), 1);
                        tmpHolder.task_name.setText(Html.fromHtml("<strike>" + singleTask.get("task") + "</strike>"));
                        tmpHolder.task_date.setText(Html.fromHtml("<strike>" + singleTask.get("date") + "</strike>"));
                    } else {
                        database.updateTaskStatus(singleTask.get("id"), 0);
                        tmpHolder.task_name.setText(singleTask.get("task"));
                        tmpHolder.task_date.setText(singleTask.get("date"));

                    }

                }
            });


        } catch (Exception e) {
        }
        return convertView;
    }
}
class TaskViewHolder {
    TextView task_name,task_date;
    CheckBox checkBtn;
}
