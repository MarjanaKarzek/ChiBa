package de.emm.teama.chibaapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 22.06.2017.
 */

public class ToDoListAdapter extends ArrayAdapter<Integer> {
    private static final String TAG = "ToDoListAdapter";
    private Context context;
    private int resource;

    private String stateString;
    private boolean state;

    static class ViewHolderItem {
        TextView todoText;
        CheckBox checkBox;
    }

    public ToDoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Integer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolderItem viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.todoText = (TextView) convertView.findViewById(R.id.textViewToDoList);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxToDoList);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.checkBox.isChecked()){
                        Log.d(TAG, "onCheckedChanged: checking text");
                        viewHolder.todoText.setPaintFlags(viewHolder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        database.setStateOfToDoByToDoId(getItem(position), true);
                    }else{
                        Log.d(TAG, "onCheckedChanged: unchecking text");
                        viewHolder.todoText.setPaintFlags(viewHolder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        database.setStateOfToDoByToDoId(getItem(position), false);
                    }

                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        String titleString = database.getToDoTitleById(getItem(position));
        String durationString = database.getToDoDurationById(getItem(position));
        String toDoInformation = titleString + " (" + durationString + " Stunden)";

        stateString = database.getToDoStateById(getItem(position));
        if (stateString.contains("true") || stateString.contains("1")) {
            state = true;
        } else
            state = false;

        viewHolder.todoText.setText(toDoInformation);
        viewHolder.checkBox.setChecked(state);
        if (viewHolder.checkBox.isChecked()) {
            viewHolder.todoText.setPaintFlags(viewHolder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }
}
