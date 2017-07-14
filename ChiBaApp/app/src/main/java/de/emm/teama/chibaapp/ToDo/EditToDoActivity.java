package de.emm.teama.chibaapp.ToDo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

public class EditToDoActivity extends AppCompatActivity {
    private static final String TAG = "EditToDoActivity";
    private Context context = EditToDoActivity.this;
    private int todoId;

    //Current Event Information
    private String currentToDoTitle;
    private String currentDuration;
    private String currentStartDate;
    private String currentStartTime;
    private String currentLocation;
    private ArrayList<String> currentAssignedHashtags = new ArrayList<String>();

    //Hashtag List Fields
    private ArrayList<String> hashtags = new ArrayList<String>();
    private ArrayList<String> assignedHashtags = new ArrayList<String>();
    private ListView hashtagListView;
    private ListView assignedHashtagListView;
    private EditText searchfield;
    private AddHashtagListAdapter adapter;
    private RemoveHashtagListAdapter adapter2;

    private EditText title;
    private EditText duration;
    private EditText location;

    private Drawable errorIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        Log.d(TAG, "onCreate: started");

        errorIcon = getDrawable(R.drawable.ic_error_message);
        errorIcon.setBounds(0, 0,errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        todoId = getIntent().getIntExtra("EXTRA_TODO_ID", -1);

        //Get Event Information
        Cursor data = database.showToDoByToDoId(todoId);
        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                currentToDoTitle = data.getString(1);
                currentDuration = data.getString(2);
                currentStartDate = data.getString(3);
                currentStartTime = data.getString(4);
                currentLocation = data.getString(5);
                Log.d(TAG, "onCreate: got data from database ");
            }
        }
        data.close();
        currentAssignedHashtags = database.showHashtagsByToDoId(todoId);
        assignedHashtags = currentAssignedHashtags;

        hashtagListView = (ListView) findViewById(R.id.editToDoListViewHashtag);
        assignedHashtagListView = (ListView) findViewById(R.id.editToDoListViewAssignedHashtag);
        searchfield = (EditText) findViewById(R.id.editToDoEditTextSearchHashtag);

        setupToolbar();
        setupListViews();
        setupSearchField();

        title = (EditText) findViewById(R.id.editToDoEditTextTitle);
        title.setText(currentToDoTitle);
        duration = (EditText) findViewById(R.id.editToDoEditTextDuration);
        duration.setText(currentDuration);
        location = (EditText) findViewById(R.id.editToDoEditTextLocation);
        location.setText(currentLocation);

        title.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        duration.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchfield.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        location.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        Button deleteButton = (Button) findViewById(R.id.editToDoDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(context);
                deleteDialogBuilder.setCancelable(true);
                deleteDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.deleteToDoByToDoId(todoId);
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                            }
                        });
                deleteDialogBuilder.setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing, just close the Dialog
                            }
                        });
                deleteDialogBuilder.setTitle("ToDo löschen");
                deleteDialogBuilder.setMessage("Möchten Sie wirklich das ToDo " + title.getText() + " löschen?");
                AlertDialog cancleEditDialog = deleteDialogBuilder.create();
                cancleEditDialog.show();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.editToDoToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.editToDoCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel edit todo");
                final AlertDialog.Builder cancleEditDialogBuilder = new AlertDialog.Builder(context);
                cancleEditDialogBuilder.setCancelable(true);
                cancleEditDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                cancleEditDialogBuilder.setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing, just close the Dialog
                            }
                        });
                cancleEditDialogBuilder.setTitle("Änderungen verwerfen");
                cancleEditDialogBuilder.setMessage("Möchten Sie wirklich die Änderungen verwerfen?");
                AlertDialog cancleEditDialog = cancleEditDialogBuilder.create();
                cancleEditDialog.show();
            }
        });

        ImageView check = (ImageView) findViewById(R.id.editToDoOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: save edits of todo option selected");

                if (title.getText().toString().length() == 0 || duration.getText().toString().length() == 0 || location.getText().toString().length() == 0) {
                    if (title.getText().toString().length() == 0)
                        title.setError("Eingabe eines Titels ist erforderlich", errorIcon);
                    if (duration.getText().toString().length() == 0)
                        duration.setError("Eingabe einer Dauer ist erforderlich", errorIcon);
                    if (location.getText().toString().length() == 0)
                        location.setError("Eingabe eines Orts ist erforderlich", errorIcon);
                } else {

                    String titleString = title.getText().toString();
                    String durationString = duration.getText().toString();
                    String locationString = location.getText().toString();
                    Log.d(TAG, "onClick: assignedHashtags " + assignedHashtags.toString());

                    boolean insertData = database.updateToDo(todoId, titleString, durationString, locationString, assignedHashtags);
                    int successState = 0;
                    if (insertData)
                        successState = 1;

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("EXTRA_SUCCESS_STATE_EDIT_TODO", successState);
                    context.startActivity(intent);
                }
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
                (EditToDoActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                (EditToDoActivity.this).adapter.getFilter().filter(s);
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
                Log.d(TAG, "onItemClick: clicked " + hashtagListView.getAdapter().getItem(position).toString());
                String currentItem = hashtagListView.getAdapter().getItem(position).toString();
                if(!assignedHashtags.contains(currentItem)) {
                    assignedHashtags.add(currentItem);
                    Collections.sort(assignedHashtags);
                }
                else
                    Toast.makeText(EditToDoActivity.this,"Hashtag bereits hinzugefügt",Toast.LENGTH_LONG).show();
                adapter2.notifyDataSetChanged();
            }
        });
        hashtagListView.setEmptyView(findViewById(R.id.editToDoListViewHashtagEmpty));
        assignedHashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: clicked " + assignedHashtagListView.getAdapter().getItem(position).toString());
                String currentItem = assignedHashtagListView.getAdapter().getItem(position).toString();
                assignedHashtags.remove(assignedHashtags.indexOf(currentItem));
                adapter2.notifyDataSetChanged();
            }
        });
        assignedHashtagListView.setEmptyView(findViewById(R.id.editToDoListViewAssignedHashtagEmpty));
    }

    private void initializeHashtags() {
        Cursor data = database.showHashtags();
        if (data.getCount() != 0) {
            String hashtag = "";
            while (data.moveToNext()) {
                hashtag += data.getString(1);
                hashtags.add(hashtag);
                hashtag = "";
            }
        }
        Collections.sort(hashtags);
    }
}
