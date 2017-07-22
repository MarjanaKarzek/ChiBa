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
 * <h1>RemoveHashtagListAdapter Class</h1>
 * This class handles the list used to remove hashtags to appointments and todos.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-06-22
 */

public class RemoveHashtagListAdapter extends ArrayAdapter<String>{
    private static final String TAG = "RemoveHashtagListAdapter";
    private Context context;
    private int resource;

    /**
     * This constructor is used to receive context, resource and objects from its parent.
     *
     * @param context This parameter is used to receive the context from its parent.
     * @param resource This parameter is used to receive the resources from its parent.
     * @param objects This parameter is used to receive the objects to be handled by the adapter.
     */
    public RemoveHashtagListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    /**
     * This method creates the view for every element in the list.
     * It just displays the hastags name.
     *
     * @param position This parameter is used to receive the position of the item in the list.
     * @param convertView This parameter is used to receive the convertView object.
     * @param parent This parameter is used to receive the parent ViewGroup.
     * @return The method returns the convertView object after it was modified.
     */
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
