package de.emm.teama.chibaapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Marjana Karzek on 22.06.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "events.db";
    public static final String TABLE_NAME_EVENTS = "events_table";
    public static final String COLUMN_EVENTS_ID = "ID";
    public static final String COLUMN_EVENTS_TITLE = "TITLE";
    public static final String COLUMN_EVENTS_FULLDAY = "FULLDAY";
    public static final String COLUMN_EVENTS_STARTDATE = "STARTDATE";
    public static final String COLUMN_EVENTS_ENDDATE = "ENDDATE";
    public static final String COLUMN_EVENTS_STARTTIME = "STARTTIME";
    public static final String COLUMN_EVENTS_ENDTIME = "ENDTIME";
    public static final String COLUMN_EVENTS_LOCATION = "LOCATION";

    public static final String TABLE_NAME_TODOS = "todos_table";
    public static final String COLUMN_TODO_ID = "ID";
    public static final String COLUMN_TODO_TITLE = "TITLE";
    public static final String COLUMN_TODO_DURATION = "DURATION";
    public static final String COLUMN_TODO_LOCATION = "LOCATION";

    public static final String TABLE_NAME_HASHTAGS = "hashtags_table";
    public static final String COLUMN_HASHTAGS_ID = "ID";
    public static final String COLUMN_HASHTAGS_NAME = "NAME";

    public static final String TABLE_NAME_EVENTMATCHING = "eventmatching_table";
    public static final String COLUMN_EVENTMATCHING_ID = "ID";
    public static final String COLUMN_EVENTMATCHING_EVENT_ID = "EVENT_ID";
    public static final String COLUMN_EVENTMATCHING_HASHTAG_ID = "HASHTAG_ID";

    public static final String TABLE_NAME_TODOMATCHING = "todomatching_table";
    public static final String COLUMN_TODOMATCHING_ID = "ID";
    public static final String COLUMN_TODOMATCHING_TODO_ID = "TODO_ID";
    public static final String COLUMN_TODOMATCHING_HASHTAG_ID = "HASHTAG_ID";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableEvents = "CREATE TABLE " + TABLE_NAME_EVENTS + "("
                + COLUMN_EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_EVENTS_TITLE + " TEXT,"
                + COLUMN_EVENTS_FULLDAY + " BOOLEAN,"
                + COLUMN_EVENTS_STARTDATE + " DATE,"
                + COLUMN_EVENTS_ENDDATE + " DATE,"
                + COLUMN_EVENTS_STARTTIME + " DATETIME,"
                + COLUMN_EVENTS_ENDTIME + " DATETIME,"
                + COLUMN_EVENTS_LOCATION + " TEXT)";
        db.execSQL(createTableEvents);
        String createTableToDos = "CREATE TABLE " + TABLE_NAME_TODOS + "("
                + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_TODO_TITLE + " TEXT,"
                + COLUMN_TODO_DURATION + " INTEGER,"
                + COLUMN_TODO_LOCATION + " TEXT)";
        db.execSQL(createTableToDos);
        String createTableHashtags = "CREATE TABLE " + TABLE_NAME_HASHTAGS + "("
                + COLUMN_HASHTAGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_HASHTAGS_NAME + " TEXT)";
        db.execSQL(createTableHashtags);
        String createTableEventMatching = "CREATE TABLE " + TABLE_NAME_EVENTMATCHING + "("
                + COLUMN_EVENTMATCHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_EVENTMATCHING_EVENT_ID + " INTEGER,"
                + COLUMN_EVENTMATCHING_HASHTAG_ID + " INTEGER)";
        db.execSQL(createTableEventMatching);
        String createTableToDoMatching = "CREATE TABLE " + TABLE_NAME_TODOMATCHING + "("
                + COLUMN_TODOMATCHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_TODOMATCHING_TODO_ID + " INTEGER,"
                + COLUMN_TODOMATCHING_HASHTAG_ID + " INTEGER)";
        db.execSQL(createTableToDoMatching);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addEvent(String title, boolean fullday, long startdate, long enddate, long starttime, long endtime, String location, ArrayList<String> hashtags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentEventValues = new ContentValues();
        contentEventValues.put(COLUMN_EVENTS_TITLE, title);
        contentEventValues.put(COLUMN_EVENTS_FULLDAY, fullday);
        contentEventValues.put(COLUMN_EVENTS_STARTDATE, startdate);
        contentEventValues.put(COLUMN_EVENTS_ENDDATE, enddate);
        contentEventValues.put(COLUMN_EVENTS_STARTTIME, starttime);
        contentEventValues.put(COLUMN_EVENTS_ENDTIME, endtime);
        contentEventValues.put(COLUMN_EVENTS_LOCATION, location);

        long result = db.insert(TABLE_NAME_EVENTS, null, contentEventValues);
        if (result == -1)
            return false;
        if(hashtags.size() > 0) {
            int eventId = getEventIdByLastEvent();

            ContentValues contentEventHashtagValues = new ContentValues();
            for (String hashtag : hashtags) {
                Log.d(TAG, "addEvent: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_EVENT_ID, eventId);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_HASHTAG_ID, hashtagId);
            }

            result = db.insert(TABLE_NAME_EVENTMATCHING, null, contentEventHashtagValues);
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    private int getEventIdByLastEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT MAX(" + COLUMN_EVENTS_ID + ") FROM " + TABLE_NAME_EVENTS, null);
        String id = "";
        if(data != null && data.moveToNext()) {
            id = data.getString(0);
            data.close();
        }
        return Integer.valueOf(id);
    }

    private int getHashtagIdByName(String hashtag) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_HASHTAGS_ID + " FROM " + TABLE_NAME_HASHTAGS + " WHERE " + COLUMN_HASHTAGS_NAME + " = '" + hashtag + "'", null);
        String id = "";
        if(data != null && data.moveToNext()) {
            id = data.getString(0);
            data.close();
        }
        return Integer.valueOf(id);
    }
    
    public Cursor showEvent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS, null);
        return data;
    }
}
