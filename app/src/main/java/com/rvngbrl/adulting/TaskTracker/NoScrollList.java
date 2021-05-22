package com.rvngbrl.adulting.TaskTracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

// creates a list view with no scrolling

public class NoScrollList extends ListView {
    public NoScrollList(Context context) {
        super(context);
    }
    public NoScrollList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
