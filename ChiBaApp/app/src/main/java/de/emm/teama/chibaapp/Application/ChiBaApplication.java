package de.emm.teama.chibaapp.Application;

import android.app.Application;

import de.emm.teama.chibaapp.Utils.DatabaseHelper;

/**
 * Created by marja on 02.07.2017.
 */

public class ChiBaApplication extends Application {
    //Database Initialization
    public static DatabaseHelper database ;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new DatabaseHelper(this);
        database.addUserAndSystemIfNotExist();
    }
}
