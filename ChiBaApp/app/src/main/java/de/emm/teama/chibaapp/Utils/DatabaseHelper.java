package de.emm.teama.chibaapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
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
    private static final String COLUMN_TODO_STATE = "STATE";

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

    private static final String TABLE_NAME_USER = "user_table";
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USER_NAME = "NAME";
    private static final String COLUMN_USER_BIRTHDATE = "BIRTHDATE";
    private static final String COLUMN_USER_HOMEADDRESS = "HOMEADDRESS";
    private static final String COLUMN_USER_AVATAR_USE = "AVATAR_USE";
    private static final String COLUMN_USER_NOTDISTURB = "DO_NOT_DISTURB";
    private static final String COLUMN_USER_NOTDISTURB_STARTTIME = "DO_NOT_DISTURB_STARTTIME";
    private static final String COLUMN_USER_NOTDISTURB_ENDTIME = "DO_NOT_DISTURB_ENDTIME";

    private static final String TABLE_NAME_FULLDAYMATCHING = "fulldaymatching_table";
    private static final String COLUMN_FULLDAYMATCHING_ID = "ID";
    private static final String COLUMN_FULLDAYMATCHING_DATE = "DATE";
    private static final String COLUMN_FULLDAYMATCHING_EVENT_ID = "EVENT_ID";

    private static final String TABLE_NAME_SYSTEMINFO = "systeminfo_table";
    private static final String COLUMN_SYSTEMINFO_ID = "ID";
    private static final String COLUMN_SYSTEMINFO_CITY = "CITY";
    private static final String COLUMN_SYSTEMINFO_TEMPERATURE = "TEMPERATURE";
    private static final String COLUMN_SYSTEMINFO_WEATHER_ID = "WEATHER_ID";
    private static final String COLUMN_SYSTEMINFO_SUNRISE = "SUNRISE";
    private static final String COLUMN_SYSTEMINFO_SUNSET = "SUNSET";
    private static final String COLUMN_SYSTEMINFO_TIMESTAMP = "TIMESTAMP";


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
                + COLUMN_TODO_TITLE + " TEXT, "
                + COLUMN_TODO_DURATION + " INTEGER, "
                + COLUMN_TODO_STARTDATE + " DATE, "
                + COLUMN_TODO_STARTTIME + " DATETIME, "
                + COLUMN_TODO_LOCATION + " TEXT, "
                + COLUMN_TODO_STATE + " BOOLEAN)";
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
        String createTableUser = "CREATE TABLE " + TABLE_NAME_USER + "( "
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_BIRTHDATE + " DATE, "
                + COLUMN_USER_HOMEADDRESS + " TEXT, "
                + COLUMN_USER_AVATAR_USE + " BOOLEAN, "
                + COLUMN_USER_NOTDISTURB + " BOOLEAN, "
                + COLUMN_USER_NOTDISTURB_STARTTIME + " DATE, "
                + COLUMN_USER_NOTDISTURB_ENDTIME + " DATE)";
        db.execSQL(createTableUser);
        String createTableFullDayMatching = "CREATE TABLE " + TABLE_NAME_FULLDAYMATCHING + "( "
                + COLUMN_FULLDAYMATCHING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_FULLDAYMATCHING_DATE + " DATE, "
                + COLUMN_FULLDAYMATCHING_EVENT_ID + " INTEGER)";
        db.execSQL(createTableFullDayMatching);
        String createTableSystemInfo = "CREATE TABLE " + TABLE_NAME_SYSTEMINFO + "( "
                + COLUMN_SYSTEMINFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_SYSTEMINFO_CITY + " TEXT, "
                + COLUMN_SYSTEMINFO_TEMPERATURE + " TEXT, "
                + COLUMN_SYSTEMINFO_WEATHER_ID + " TEXT, "
                + COLUMN_SYSTEMINFO_SUNRISE + " TEXT, "
                + COLUMN_SYSTEMINFO_SUNSET + " TEXT, "
                + COLUMN_SYSTEMINFO_TIMESTAMP + " TEXT)";
        db.execSQL(createTableSystemInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUserAndSystemIfNotExist(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Create Table SystemInfo if it not exists
        String createTableSystemInfo = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SYSTEMINFO + "( "
                + COLUMN_SYSTEMINFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_SYSTEMINFO_CITY + " TEXT, "
                + COLUMN_SYSTEMINFO_TEMPERATURE + " TEXT, "
                + COLUMN_SYSTEMINFO_WEATHER_ID + " TEXT, "
                + COLUMN_SYSTEMINFO_SUNRISE + " TEXT, "
                + COLUMN_SYSTEMINFO_SUNSET + " TEXT, "
                + COLUMN_SYSTEMINFO_TIMESTAMP + " TEXT)";
        db.execSQL(createTableSystemInfo);
        //Create Table User if it not exists
        String createTableUser = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + "( "
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_BIRTHDATE + " DATE, "
                + COLUMN_USER_HOMEADDRESS + " TEXT, "
                + COLUMN_USER_AVATAR_USE + " BOOLEAN, "
                + COLUMN_USER_NOTDISTURB + " BOOLEAN, "
                + COLUMN_USER_NOTDISTURB_STARTTIME + " DATE, "
                + COLUMN_USER_NOTDISTURB_ENDTIME + " DATE)";
        db.execSQL(createTableUser);
        //check if user exists
        Cursor data = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_USER, null);
        //if not, create user
        if(data != null && data.moveToNext()) {
            if (data.getString(0).contains("0")) {
                //create User
                ContentValues contentUserValues = new ContentValues();
                contentUserValues.put(COLUMN_USER_NAME, "ChiBa");
                contentUserValues.put(COLUMN_USER_BIRTHDATE, "01. Januar 1990");
                contentUserValues.put(COLUMN_USER_HOMEADDRESS, "Beispieladresse 1, 10000 Beispielstadt");
                contentUserValues.put(COLUMN_USER_AVATAR_USE, true);
                contentUserValues.put(COLUMN_USER_NOTDISTURB, false);
                contentUserValues.put(COLUMN_USER_NOTDISTURB_STARTTIME, "22:00");
                contentUserValues.put(COLUMN_USER_NOTDISTURB_ENDTIME, "06:00");
                db.insert(TABLE_NAME_USER, null, contentUserValues);

                Log.d(TAG, "addUserAndSystemIfNotExist: user had to be created");
            } else
                Log.d(TAG, "addUserAndSystemIfNotExist: user exist");
        }
        //check if system info exists
        Cursor dataSystem = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_SYSTEMINFO, null);
        //if not, create system info
        if(dataSystem != null && dataSystem.moveToNext()) {
            if (dataSystem.getString(0).contains("0")) {
                //create system info
                ContentValues contentSystemInfoValues = new ContentValues();
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_CITY, "BERLIN,DE");
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_TEMPERATURE, "0 *C");
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_WEATHER_ID, "80000");
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_SUNRISE, "1499313136");
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_SUNSET, "1499372285");
                contentSystemInfoValues.put(COLUMN_SYSTEMINFO_TIMESTAMP, "0");
                db.insert(TABLE_NAME_SYSTEMINFO, null, contentSystemInfoValues);

                Log.d(TAG, "addUserAndSystemIfNotExist: system info had to be created");
            } else
                Log.d(TAG, "addUserAndSystemIfNotExist: system info exist");
        }
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

    public int getEventIdByLastEvent() {
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

    public Cursor showEventsOfFulldayMatching(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_FULLDAYMATCHING, null);
        return data;
    }

    public Cursor showEventsByStartDate(String startDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS + " WHERE " + COLUMN_EVENTS_STARTDATE + " = '" + startDate + "' "
                                    + "ORDER BY " + COLUMN_EVENTS_STARTTIME, null);
        return data;
    }

    public Cursor showEventsByStartDateWithoutFullDay(String startDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS + " WHERE " + COLUMN_EVENTS_STARTDATE + " = '" + startDate + "' "
                + "AND (" + COLUMN_EVENTS_FULLDAY + " != '1' AND "+ COLUMN_EVENTS_FULLDAY + " != 'true') "
                + "ORDER BY " + COLUMN_EVENTS_STARTTIME, null);
        return data;
    }

    public Cursor showEventIdsByStartDateThatAreFullDay(String startDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_FULLDAYMATCHING_EVENT_ID + " FROM " + TABLE_NAME_FULLDAYMATCHING + " WHERE " + COLUMN_FULLDAYMATCHING_DATE + " = '" + startDate + "'", null);
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

    public ArrayList<String> showHashtagsByToDoId(int todoId){
        ArrayList<String> hashtagIds = new ArrayList<String>();
        ArrayList<String> hashtags = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODOMATCHING_HASHTAG_ID + " FROM " + TABLE_NAME_TODOMATCHING + " WHERE " + COLUMN_TODOMATCHING_TODO_ID + " = " + todoId, null);

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

    public Cursor showToDoByToDoId(int todoId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS + " WHERE " + COLUMN_TODO_ID + " = " + todoId, null);
        return data;
    }

    public void deleteToDoByToDoId(int todoId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    public boolean updateToDo(int todoId, String title, String duration, String location, ArrayList<String> hashtags) {
        Log.d(TAG, "updateToDo: hashtags to assign: " + hashtags.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TODOS +
                " SET " + COLUMN_TODO_TITLE + " = '" + title + "', " +
                COLUMN_TODO_DURATION + " = '" + duration + "', " +
                COLUMN_TODO_LOCATION + " = '" + location + "' " +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
        deleteAssignedHashtagsByToDoId(todoId);
        insertAssignedHashtagsOfToDo(todoId,hashtags);
        return true;
    }

    private boolean insertAssignedHashtagsOfToDo(int todoId, ArrayList<String> hashtags) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(hashtags.size() > 0) {
            ContentValues contentToDoHashtagValues = new ContentValues();
            for (String hashtag : hashtags) {
                Log.d(TAG, "insertAssignedHashtags: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_TODO_ID, todoId);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_HASHTAG_ID, hashtagId);
            }

            long result = db.insert(TABLE_NAME_TODOMATCHING, null, contentToDoHashtagValues);
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    private void deleteAssignedHashtagsByToDoId(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_TODOMATCHING +
                " WHERE " + COLUMN_TODOMATCHING_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    public String getToDoTitleById(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODO_TITLE + " FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_ID+ " = " + todoId, null);
        String toDoTitle = "";
        data.moveToNext();
        if (data.getCount() != 0) {
            toDoTitle = data.getString(0);
            data.close();
        }
        return toDoTitle;
    }

    public String getToDoDurationById(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODO_DURATION + " FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_ID+ " = " + todoId, null);
        String toDoDuration = "";
        data.moveToNext();
        if (data.getCount() != 0) {
            toDoDuration = data.getString(0);
            data.close();
        }
        return toDoDuration;
    }

    public String getToDoStateById(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODO_STATE + " FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_ID+ " = " + todoId, null);
        String toDoState = "";
        data.moveToNext();
        if (data.getCount() != 0) {
            toDoState = data.getString(0);
            data.close();
        }
        return toDoState;
    }

    public Cursor showToDos() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS, null);
        return data;
    }

    public boolean addToDo(String title, String duration, String location, boolean state, ArrayList<String> hashtags) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentToDoValues = new ContentValues();
        contentToDoValues.put(COLUMN_TODO_TITLE, title);
        contentToDoValues.put(COLUMN_TODO_DURATION, duration);
        contentToDoValues.put(COLUMN_TODO_LOCATION, location);
        contentToDoValues.put(COLUMN_TODO_STATE, state);

        long result = db.insert(TABLE_NAME_TODOS, null, contentToDoValues);
        if (result == -1)
            return false;
        if(hashtags.size() > 0) {
            int todoId = getToDoIdByLastToDo();

            ContentValues contentToDoHashtagValues = new ContentValues();
            for (String hashtag : hashtags) {
                Log.d(TAG, "addToDo: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_TODO_ID, todoId);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_HASHTAG_ID, hashtagId);
            }

            result = db.insert(TABLE_NAME_TODOMATCHING, null, contentToDoHashtagValues);
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    private int getToDoIdByLastToDo() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT MAX(" + COLUMN_TODO_ID + ") FROM " + TABLE_NAME_TODOS, null);
        String id = "";
        if(data != null && data.moveToNext()) {
            id = data.getString(0);
            data.close();
        }
        return Integer.valueOf(id);
    }

    public void setStateOfToDoByToDoId(int todoId, boolean state) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TODOS +
                " SET " + COLUMN_TODO_STATE + " = '" + state + "'" +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    public Cursor showUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_USER, null);
        return data;
    }

    public void updateUserName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NAME + " = '" + name + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserAddress(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_HOMEADDRESS + " = '" + address + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserDoNotDisturb(boolean doNotDisturbValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB+ " = '" + doNotDisturbValue + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserAvatarUse(boolean avatarUse) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_AVATAR_USE + " = '" + avatarUse + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserBirthdate(String birhtdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_BIRTHDATE + " = '" + birhtdate + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserDoNotDisturbStartTime(String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB_STARTTIME+ " = '" + startTime + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void updateUserDoNotDisturbEndTime(String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB_ENDTIME+ " = '" + endTime + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    public void insertFullDayEvent(String currentDateString, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentFullDayMatchingValues = new ContentValues();
        contentFullDayMatchingValues.put(COLUMN_FULLDAYMATCHING_DATE, currentDateString);
        contentFullDayMatchingValues.put(COLUMN_FULLDAYMATCHING_EVENT_ID, eventId);
        db.insert(TABLE_NAME_FULLDAYMATCHING, null, contentFullDayMatchingValues);
    }

    public void deleteFulldayMatchingByEventId(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_FULLDAYMATCHING +
                " WHERE " + COLUMN_FULLDAYMATCHING_EVENT_ID + " = " + eventId;
        db.execSQL(query);
    }

    public Cursor showToDosByMaxDuration(int timeslotlength) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS +
                " WHERE (" + COLUMN_TODO_STATE + " = '0' OR " + COLUMN_TODO_STATE + " = 'false') AND " + COLUMN_TODO_DURATION + " <= " + timeslotlength, null);
        return data;
    }

    public String getUserName() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USER_NAME + " FROM " + TABLE_NAME_USER +
                " WHERE " + COLUMN_USER_ID + " = 1", null);
        String name = "";
        if(data.getCount() != 0 && data.moveToNext()){
            name = data.getString(0);
        }
        return name;
    }

    public boolean getUserDoNotDisturbOptionState() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USER_NOTDISTURB + " FROM " + TABLE_NAME_USER +
                " WHERE " + COLUMN_USER_ID + " = 1", null);
        String option = "";
        if(data.getCount() != 0 && data.moveToNext()){
            option = data.getString(0);
        }
        if(option.contains("0") || option.contains("false"))
            return false;
        else
            return true;
    }

    public String getUserDoNotDisturbStartTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USER_NOTDISTURB_STARTTIME + " FROM " + TABLE_NAME_USER +
                " WHERE " + COLUMN_USER_ID + " = 1", null);
        String starttime = "";
        if(data.getCount() != 0 && data.moveToNext()){
            starttime = data.getString(0);
        }
        return starttime;
    }

    public String getUserDoNotDisturbEndTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USER_NOTDISTURB_ENDTIME + " FROM " + TABLE_NAME_USER +
                " WHERE " + COLUMN_USER_ID + " = 1", null);
        String endtime = "";
        if(data.getCount() != 0 && data.moveToNext()){
            endtime = data.getString(0);
        }
        return endtime;
    }

    public Cursor getSystemInformation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_SYSTEMINFO +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1", null);
        return data;
    }

    public void setSystemInfoData(String city, String temperature) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_CITY+ " = '" + city + "', " +
                COLUMN_SYSTEMINFO_TEMPERATURE + " = '" + temperature + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    public void setSystemInfoWeatherData(int weatherId, long sunrise, long sunset) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_WEATHER_ID+ " = '" + weatherId + "', " +
                COLUMN_SYSTEMINFO_SUNRISE + " = '" + sunrise + "', " +
                COLUMN_SYSTEMINFO_SUNSET + " = '" + sunset + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    public void setSystemInfoTimeStamp(long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_TIMESTAMP + " = '" + timestamp + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    public void scheduleToDoByToDoId(Integer todoId, String date, int hour) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TODOS +
                " SET " + COLUMN_TODO_STARTDATE + " = '" + date + "', " +
                COLUMN_TODO_STARTTIME + " = '" + hour + "' " +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    public Cursor showToDosByStartDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODO_STARTTIME + ", " + COLUMN_TODO_DURATION + " FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_STARTDATE + " = '" + date + "'", null);
        return data;
    }
}
