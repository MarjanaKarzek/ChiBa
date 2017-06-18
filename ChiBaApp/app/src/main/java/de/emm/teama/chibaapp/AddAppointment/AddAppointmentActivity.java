package de.emm.teama.chibaapp.AddAppointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.emm.teama.chibaapp.R;

/**
 * Created by marja on 18.06.2017.
 */

public class AddAppointmentActivity extends AppCompatActivity{
    private static final String TAG = "AddAppointmentActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");
    }


}
