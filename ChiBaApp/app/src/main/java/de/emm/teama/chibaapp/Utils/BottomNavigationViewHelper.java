package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import de.emm.teama.chibaapp.AddAppointment.AddAppointmentActivity;
import de.emm.teama.chibaapp.AddToDo.AddToDoActivity;
import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationView){

    }

    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_add_appointment:
                        Intent intentAddAppointment = new Intent(context, AddAppointmentActivity.class);
                        context.startActivity(intentAddAppointment);
                        break;
                    case R.id.ic_add_todo:
                        Intent intentAddToDo = new Intent(context, AddToDoActivity.class);
                        context.startActivity(intentAddToDo);
                        break;
                }

                return false;
            }
        });
    }
}
