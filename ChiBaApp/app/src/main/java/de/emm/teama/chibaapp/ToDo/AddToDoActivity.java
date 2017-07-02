package de.emm.teama.chibaapp.ToDo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.AddHashtagListAdapter;
import de.emm.teama.chibaapp.Utils.RemoveHashtagListAdapter;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

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

    //Form fields
    private EditText title;
    private EditText duration;
    private EditText location;

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

        title = (EditText) findViewById(R.id.addToDoEditTextTitle);
        duration = (EditText) findViewById(R.id.addToDoEditTextDuration);
        location = (EditText) findViewById(R.id.addToDoEditTextLocation);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addToDoToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.addToDoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel add todo");
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        ImageView check = (ImageView) findViewById(R.id.addToDoOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add todo option selected");

                String titleString = title.getText().toString();
                String durationString = duration.getText().toString();
                String locationString = location.getText().toString();

                boolean insertData = database.addToDo(titleString,durationString,locationString,false,assignedHashtags);
                int successState = 0;
                if(insertData)
                    successState = 1;

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("EXTRA_SUCCESS_STATE", successState);
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
        hashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked " + hashtags.get(position));
                String currentItem = hashtags.get(position);
                hashtags.remove(position);
                assignedHashtags.add(currentItem);
                Collections.sort(assignedHashtags);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        hashtagListView.setEmptyView( findViewById( R.id.addToDoListViewHashtagEmpty) );
        assignedHashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked " + assignedHashtags.get(position));
                String currentItem = assignedHashtags.get(position);
                assignedHashtags.remove(position);
                hashtags.add(currentItem);
                Collections.sort(hashtags);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        assignedHashtagListView.setEmptyView(findViewById(R.id.addToDoListViewAssignedHashtagEmpty));
    }

    private void initializeHashtags(){
        Cursor data = database.showHashtags();
        if(data.getCount() != 0) {
            String hashtag = "";
            while (data.moveToNext()) {
                hashtag += data.getString(1);
                hashtags.add(hashtag);
                hashtag = "";
            }
        }
        Collections.sort(hashtags);
        Log.d(TAG, "initializeHashtags: hashtags:" + hashtags.toString());
    }
}
