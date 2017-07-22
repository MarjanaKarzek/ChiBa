package de.emm.teama.chibaapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * <h1>ActionReceiver Class</h1>
 * This class waits for the user to select an action from a to-do reminder notification.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-07-08
 */

public class ActionReceiver extends BroadcastReceiver {
    private static final String TAG = "ActionReceiver";

    /**
     * When the user selects an action this method receives and handles it.
     *
     * @param context This parameter is used to receive the context.
     * @param intent This parameter is used to receive the intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String todoId = intent.getStringExtra("todoId");
        //Log.d(TAG, "onReceive: action is " + action);
        //Log.d(TAG, "onReceive: todoId " +todoId);
        if(action != null) {
            if (action.contains("action1")) {
                // don't do anything just close
            } else if (action.contains("action2")) {
                updateToDoState(todoId);
            }
        }
        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeIntent);
    }

    /**
     * The method handles action 2. The user selected to do the to-do right now.
     * It updates the state and sets the time in the database that will be blocked.
     *
     * @param todoId This parameter is used to determine which to-do was selected.
     */
    public void updateToDoState(String todoId){
        String dateFormat = "d. MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);
        Calendar currentTime = Calendar.getInstance();
        String date = simpleDateFormat.format(currentTime);
        database.scheduleToDoByToDoId(Integer.valueOf(todoId), date , currentTime.get(Calendar.HOUR_OF_DAY));
    }
}
