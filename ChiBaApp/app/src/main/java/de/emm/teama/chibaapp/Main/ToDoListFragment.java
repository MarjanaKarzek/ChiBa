package de.emm.teama.chibaapp.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class ToDoListFragment extends Fragment {
    private static final String TAG = "ToDoListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);
        return view;
    }
}
