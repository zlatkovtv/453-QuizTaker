package com.example.quiztaker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Adapter for the GridView in the Results Fragment
 */
public class QuizResultsAdapter extends BaseAdapter {
    private Context context;
    private boolean[] results;

    public QuizResultsAdapter(Context context, boolean[] results) {
        this.context = context;
        this.results = results;
    }

    public int getCount() {
        return results.length;
    }

    public Object getItem(int position) {
        return results[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * Sets ImageViews in the GridView corresponding to the results array
     * @param position Index of the result
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        if(results[position]) {
            imageView.setImageResource(R.drawable.ic_check_black_24dp);
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            imageView.setImageResource(R.drawable.ic_close_black_24dp);
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
        }

        return imageView;
    }
}
