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
 * Created by Marjana Karzek on 22.06.2017.
 */

public class RemoveHashtagListAdapter extends ArrayAdapter<String>{
    private static final String TAG = "RemoveHashtagListAdapter";
    private Context context;
    private int resource;

    public RemoveHashtagListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String hashtag = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView hashtagText = (TextView) convertView.findViewById(R.id.textViewRemoveHashtagList);
        hashtagText.setText(hashtag);

        return convertView;
    }
}
