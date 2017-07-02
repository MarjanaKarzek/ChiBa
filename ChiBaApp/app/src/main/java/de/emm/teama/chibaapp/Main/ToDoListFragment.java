package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.ToDoListAdapter;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class ToDoListFragment extends Fragment {
    private static final String TAG = "ToDoListFragment";

    private ListView todoList;
    private ToDoListAdapter adapter;
    private ArrayList<Integer> currentToDos = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);

        todoList = (ListView) view.findViewById(R.id.listViewToDoList);
        todoList.setEmptyView(view.findViewById(R.id.textViewToDosEmpty));

        displayData();
        adapter = new ToDoListAdapter(inflater.getContext(), R.layout.layout_list_todo_adapter_view, currentToDos);
        todoList.setAdapter(adapter);

        return view;
    }

    private void displayData() {
        Log.d(TAG, "displayData: reading data from database");
        Cursor data = database.showToDos();

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                currentToDos.add(Integer.valueOf(data.getString(0)));
            }
        }
        Log.d(TAG, "displayData: data.getCount()" + data.getCount());
    }
}
