package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 23.06.2017.
 */

public class DisplayEventListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "DisplayEventListAdapter";
    private Context context;
    private int resource;

    public DisplayEventListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String event = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView eventText = (TextView) convertView.findViewById(R.id.textViewEventList);
        eventText.setText(event);

        return convertView;
    }
}
