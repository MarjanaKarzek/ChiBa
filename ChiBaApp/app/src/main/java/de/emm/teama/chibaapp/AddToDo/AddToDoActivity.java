package de.emm.teama.chibaapp.AddToDo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

import de.emm.teama.chibaapp.AddAppointment.AddAppointmentActivity;
import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.RemoveHashtagListAdapter;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class AddToDoActivity extends AppCompatActivity{
    private static final String TAG = "AddToDoActivity";
    private Context context = AddToDoActivity.this;

    //Hashtag List Fields
    private ArrayList<String> hashtags = new ArrayList<String>();
    private ArrayList<String> assignedHashtags = new ArrayList<String>();
    private ListView hashtagListView;
    private ListView assignedHashtagListView;
    private EditText searchfield;
    private AddHashtagListAdapter adapter;
    private RemoveHashtagListAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        Log.d(TAG, "onCreate: started");

        hashtagListView = (ListView) findViewById(R.id.addToDoListViewHashtag);
        assignedHashtagListView = (ListView) findViewById(R.id.addToDoListViewAssignedHashtag);
        searchfield = (EditText) findViewById(R.id.addToDoEditTextSearchHashtag);

        setupToolbar();
        setupListViews();
        setupSearchField();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addToDoToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.addToDoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel add appointment");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        ImageView check = (ImageView) findViewById(R.id.addToDoOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add appointment option selected");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void setupSearchField() {
        searchfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (AddToDoActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                (AddToDoActivity.this).adapter.getFilter().filter(s);
            }
        });
    }

    private void setupListViews() {
        initializeHashtags();
        adapter = new AddHashtagListAdapter(this, R.layout.layout_add_hashtag_adapter_view, hashtags);
        hashtagListView.setAdapter(adapter);
        adapter2 = new RemoveHashtagListAdapter(this, R.layout.layout_remove_hashtag_adapter_view, assignedHashtags);
        assignedHashtagListView.setAdapter(adapter2);
    }

    private void initializeHashtags(){
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        hashtags.add("Fitness");
        hashtags.add("Kochen");
        hashtags.add("Einkaufen");
        hashtags.add("Arzt");
        hashtags.add("Haushalt");
        assignedHashtags.add("Assigend Hashtag");
    }
}
