package de.emm.teama.chibaapp.Main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.emm.teama.chibaapp.Appointment.EditAppointmentActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.ToDo.EditToDoActivity;
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
    private View view;

    static class ViewHolderItem {
        ListView todoList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewHolderItem viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_todolist, container, false);
            viewHolder = new ViewHolderItem();

            viewHolder.todoList = (ListView) view.findViewById(R.id.listViewToDoList);
            viewHolder.todoList.setEmptyView(view.findViewById(R.id.textViewToDosEmpty));
            displayData();
            adapter = new ToDoListAdapter(inflater.getContext(), R.layout.layout_list_todo_adapter_view, currentToDos);
            viewHolder.todoList.setAdapter(adapter);
            viewHolder.todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemClick: clicked");
                    Intent intent = new Intent(getActivity(), EditToDoActivity.class);
                    intent.putExtra("EXTRA_TODO_ID", currentToDos.get(position));
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    private void displayData() {
        Cursor data = database.showToDos();
        currentToDos.clear();

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                currentToDos.add(Integer.valueOf(data.getString(0)));
            }
        }
    }
}
