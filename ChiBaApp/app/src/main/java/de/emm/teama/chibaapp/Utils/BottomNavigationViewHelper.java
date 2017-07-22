package de.emm.teama.chibaapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import de.emm.teama.chibaapp.Appointment.AddAppointmentActivity;
import de.emm.teama.chibaapp.ToDo.AddToDoActivity;
import de.emm.teama.chibaapp.R;

/**
 * <h1>BottomNavigationViewHelper Class</h1>
 * This class sets up the bottom navigation view and can be reused.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-06-18
 * Created by Marjana Karzek on 18.06.2017.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHelper";

    /**
     * This method enables the navigation.
     *
     * @param context This parameter is used to get the context from its parent.
     * @param view This parameter is used to to get the view from its parent.
     */
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
