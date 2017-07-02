package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import static de.emm.teama.chibaapp.Main.MainActivity.database;

/**
 * Created by Marjana Karzek on 22.06.2017.
 */

public class ToDoListAdapter extends ArrayAdapter<Integer>{
    private static final String TAG = "ToDoListAdapter";
    private Context context;
    private int resource;

    private TextView todoText;
    private String stateString;
    private boolean state;

    public ToDoListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Integer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String titleString = database.getToDoTitleById(getItem(position));
        String durationString = database.getToDoDurationById(getItem(position));
        String toDoInformation = titleString + " (" + durationString + " Stunden)";

        stateString = database.getToDoStateById(getItem(position));
        state = false;
        Log.d(TAG, "getView: stateString is of value " + stateString);
        if(stateString.contains("true")){
            state = true;
        }
        else
            state = false;
        Log.d(TAG, "getView: state is of value " + state);


        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        todoText = (TextView) convertView.findViewById(R.id.textViewToDoList);
        todoText.setText(toDoInformation);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxToDoList);
        checkBox.setChecked(state);
        if(checkBox.isChecked()){
            todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: checking text");
                    todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    database.setStateOfToDoByToDoId(getItem(position), true);
                    stateString = "true";
                    state = true;
                    synchronized (todoText){
                        todoText.notifyAll();
                    }
                }
                else {
                    Log.d(TAG, "onCheckedChanged: unchecking text");
                    todoText.setPaintFlags(todoText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    database.setStateOfToDoByToDoId(getItem(position), false);
                    stateString = "false";
                    state = false;
                    synchronized (todoText){
                        todoText.notifyAll();
                    }
                }
            }
        });

        return convertView;
    }
}
