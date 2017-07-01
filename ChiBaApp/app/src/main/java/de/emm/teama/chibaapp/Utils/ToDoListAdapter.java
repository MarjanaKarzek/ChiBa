package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        String stateString = database.getToDoStateById(getItem(position));
        boolean state = true;
        if(stateString != "1"){
            state = false;
        }

        String toDoInformation = titleString + " (" + durationString + " Stunden)";

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        todoText = (TextView) convertView.findViewById(R.id.textViewToDoList);
        todoText.setText(toDoInformation);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxToDoList);
        checkBox.setChecked(state);
        if(checkBox.isChecked()){
            todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            todoText.setPaintFlags(todoText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    database.setStateOfToDoByToDoId(getItem(position), true);
                }
                else {
                    todoText.setPaintFlags(todoText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    database.setStateOfToDoByToDoId(getItem(position), false);
                }
            }
        });

        return convertView;
    }
}
