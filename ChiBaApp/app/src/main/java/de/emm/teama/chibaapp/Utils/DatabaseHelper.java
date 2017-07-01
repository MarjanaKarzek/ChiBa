package de.emm.teama.chibaapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Marjana Karzek on 22.06.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "events.db";
    private static final String TABLE_NAME_EVENTS = "events_table";
    private static final String COLUMN_EVENTS_ID = "ID";
    private static final String COLUMN_EVENTS_TITLE = "TITLE";
    private static final String COLUMN_EVENTS_FULLDAY = "FULLDAY";
    private static final String COLUMN_EVENTS_STARTDATE = "STARTDATE";
    private static final String COLUMN_EVENTS_ENDDATE = "ENDDATE";
    private static final String COLUMN_EVENTS_STARTTIME = "STARTTIME";
    private static final String COLUMN_EVENTS_ENDTIME = "ENDTIME";
    private static final String COLUMN_EVENTS_LOCATION = "LOCATION";

    private static final String TABLE_NAME_TODOS = "todos_table";
    private static final String COLUMN_TODO_ID = "ID";
    private static final String COLUMN_TODO_TITLE = "TITLE";
    private static final String COLUMN_TODO_DURATION = "DURATION";
    private static final String COLUMN_TODO_STARTDATE = "STARTDATE";
    private static final String COLUMN_TODO_STARTTIME = "STARTTIME";
    private static final String COLUMN_TODO_LOCATION = "LOCATION";

    private static final String TABLE_NAME_HASHTAGS = "hashtags_table";
    private static final String COLUMN_HASHTAGS_ID = "ID";
    private static final String COLUMN_HASHTAGS_NAME = "NAME";

    private static final String TABLE_NAME_EVENTMATCHING = "eventmatching_table";
    private static final String COLUMN_EVENTMATCHING_ID = "ID";
    private static final String COLUMN_EVENTMATCHING_EVENT_ID = "EVENT_ID";
    private static final String COLUMN_EVENTMATCHING_HASHTAG_ID = "HASHTAG_ID";

    private static final String TABLE_NAME_TODOMATCHING = "todomatching_table";
    private static final String COLUMN_TODOMATCHING_ID = "ID";
    private static final String COLUMN_TODOMATCHING_TODO_ID = "TODO_ID";
    private static final String COLUMN_TODOMATCHING_HASHTAG_ID = "HASHTAG_ID";

    private ArrayList<String> hashtags = new ArrayList<>(Arrays.asList("Ballsport", "Fitness", "Schwimmen",
                                                                        "Restaurant", "Brunch", "Business Launch",
                                                                        "Geburtstag", "Jahrestag", "Muttertag", "Vatertag", "Valentinstag",
                                                                        "Weihnachten", "Halloween", "Silvester", "Chanukka", "Chinesisches Neujahr", "Ostern",
                                                                        "Sommersonnenwende", "Kino",
                                                                        "Einkaufen", "Wäsche waschen", "Geschirrspühlen", "Bügeln", "Staub wischen", "Staub saugen",
                                                                        "Prüfungsanmeldung", "Kursbelegung", "Klausur", "Lerngruppe", "Lernen",
                                                                        "Laptop", "Unterlagen", "Hausaufgaben",
                                                                        "Arbeit", "Party"));

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
                + COLUMN_TODO_STARTDATE + " DATE, "
                + COLUMN_TODO_STARTTIME + " DATETIME,"
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

    public boolean addEvent(String title, boolean fullday, String startdate, String enddate, String starttime, String endtime, String location, ArrayList<String> hashtags) {
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

    public Cursor showEventByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS + " WHERE " + COLUMN_EVENTS_ID + " = " + eventId, null);
        return data;
    }

    public boolean updateEvent(int eventId, String title, boolean fullday, String startdate, String enddate, String starttime, String endtime, String location, ArrayList<String> hashtags){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_EVENTS +
                " SET " + COLUMN_EVENTS_TITLE + " = '" + title + "', " +
                COLUMN_EVENTS_FULLDAY + " = '" + fullday + "', " +
                COLUMN_EVENTS_STARTDATE + " = '" + startdate + "', " +
                COLUMN_EVENTS_ENDDATE + " = '" + enddate + "', " +
                COLUMN_EVENTS_STARTTIME + " = '" + starttime + "', " +
                COLUMN_EVENTS_ENDTIME + " = '" + endtime + "', " +
                COLUMN_EVENTS_LOCATION + " = '" + location + "' " +
                " WHERE " + COLUMN_EVENTS_ID + " = " + eventId;
        db.execSQL(query);
        deleteAssignedHashtagsByEventId(eventId);
        insertAssignedHashtags(eventId,hashtags);
        return true;
    }

    public void checkHashtagTable() {
        Log.d(TAG, "checkHashtagTable: started");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_HASHTAGS, null);
        int amountEntries = 0;
        if(data != null && data.moveToNext()) {
            amountEntries = Integer.valueOf(data.getString(0));
            data.close();
        }

        if(amountEntries != hashtags.size()){
            Log.d(TAG, "checkHashtagTable: entries must be added");
            db.execSQL("DROP TABLE " + TABLE_NAME_HASHTAGS);
            String createTableHashtags = "CREATE TABLE " + TABLE_NAME_HASHTAGS + "("
                    + COLUMN_HASHTAGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + COLUMN_HASHTAGS_NAME + " TEXT)";
            db.execSQL(createTableHashtags);
            for(String hashtag: hashtags) {
                ContentValues contentEventValues = new ContentValues();
                contentEventValues.put(COLUMN_HASHTAGS_NAME, hashtag);
                db.insert(TABLE_NAME_HASHTAGS, null, contentEventValues);
            }
        }
    }

    public Cursor showHashtags(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_HASHTAGS, null);
        return data;
    }

    public ArrayList<String> showHashtagsByEventId(int eventId){
        ArrayList<String> hashtagIds = new ArrayList<String>();
        ArrayList<String> hashtags = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_EVENTMATCHING_HASHTAG_ID + " FROM " + TABLE_NAME_EVENTMATCHING + " WHERE " + COLUMN_EVENTMATCHING_EVENT_ID + " = " + eventId, null);

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                hashtagIds.add(data.getString(0));
            }
        }
        data.close();

        for(String id: hashtagIds){
            hashtags.add(showHashtagNameById(id));
        }

        return hashtags;
    }

    public String showHashtagNameById(String hashtagId){
        SQLiteDatabase db = this.getWritableDatabase();
        String hashtag = "";
        Cursor data = db.rawQuery("SELECT " + COLUMN_HASHTAGS_NAME + " FROM " + TABLE_NAME_HASHTAGS + " WHERE " + COLUMN_HASHTAGS_ID + " = " + hashtagId, null);

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                hashtag = data.getString(0);
            }
        }
        data.close();

        return hashtag;
    }

    public boolean insertAssignedHashtags(int eventId, ArrayList<String> hashtags){
        SQLiteDatabase db = this.getWritableDatabase();
        if(hashtags.size() > 0) {
            ContentValues contentEventHashtagValues = new ContentValues();
            for (String hashtag : hashtags) {
                Log.d(TAG, "insertAssignedHashtags: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_EVENT_ID, eventId);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_HASHTAG_ID, hashtagId);
            }

            long result = db.insert(TABLE_NAME_EVENTMATCHING, null, contentEventHashtagValues);
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    public void deleteAssignedHashtagsByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_EVENTMATCHING +
                " WHERE " + COLUMN_EVENTMATCHING_EVENT_ID + " = " + eventId;
        db.execSQL(query);
    }

    public void deleteEventByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_EVENTS +
                " WHERE " + COLUMN_EVENTS_ID + " = " + eventId;
        db.execSQL(query);
    }
}
