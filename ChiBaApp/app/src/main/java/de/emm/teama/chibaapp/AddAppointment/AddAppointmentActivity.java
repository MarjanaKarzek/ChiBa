package de.emm.teama.chibaapp.AddAppointment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import de.emm.teama.chibaapp.Main.MainActivity;
import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class AddAppointmentActivity extends AppCompatActivity{
    private static final String TAG = "AddAppointmentActivity";
    private Context context = AddAppointmentActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        Log.d(TAG, "onCreate: started");
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.addAppointmentToolbar);
        setSupportActionBar(toolbar);

        ImageView cancel = (ImageView) findViewById(R.id.addAppointmentCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel add appointment");
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        
        ImageView check = (ImageView) findViewById(R.id.addAppointmentOption);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: add appointment option selected");
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
