package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;

import java.util.List;

/**
 * Created by Mitchell on 1/7/2016.
 *
 * Adapter used to transform a list of name/id pairs into a
 * ListView showing only the names
 *
 */
public class NameIdListAdapter extends ArrayAdapter<Pair<String, Long>> {

    public NameIdListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NameIdListAdapter(Context context, List<Pair<String, Long>> objects) {
        super(context, R.layout.name_and_description_row, objects);
    }

    // Not sure if this is implemented correctly
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customView = convertView;

        if (customView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            customView = inflater.inflate(R.layout.name_and_description_row, null);
        }

        String name = getItem(position).first;
        TextView textView = (TextView) customView.findViewById(R.id.rowName);
        textView.setText(name);
        return customView;
    }
}
