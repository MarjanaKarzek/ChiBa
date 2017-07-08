package de.emm.teama.chibaapp.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 08.07.2017.
 */

public class ActionReceiver extends BroadcastReceiver {
    private static final String TAG = "ActionReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String todoId = intent.getStringExtra("todoId");
        Log.d(TAG, "onReceive: action is " + action);
        Log.d(TAG, "onReceive: todoId " +todoId);
        if(action != null) {
            if (action.contains("action1")) {
                Log.d(TAG, "performAction1: Just don't do anything just close");
            } else if (action.contains("action2")) {
                updateToDoState(todoId);
            }
        }
        Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeIntent);
    }

    public void updateToDoState(String todoId){
        Log.d(TAG, "performAction2: change state of todo " + todoId);
        database.setStateOfToDoByToDoId(Integer.valueOf(todoId), true);
    }

}
