package com.kridacreations.diary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<Details> {
    public DetailsAdapter(Activity context, ArrayList<Details> items){
        super(context,0,items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Details currentdetail = getItem(position);

        TextView dateview = (TextView) listItemView.findViewById(R.id.show_date);
        dateview.setText("Date:  " + currentdetail.getDate() + "/"
                         + currentdetail.getMonth() + "/"
                         + currentdetail.getYear());

        TextView feelingview = (TextView) listItemView.findViewById(R.id.show_feeling);
        feelingview.setText(currentdetail.getFeeling());

        return listItemView;
    }

}
