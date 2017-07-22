package de.emm.teama.chibaapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>DatabaseHelper Class</h1>
 * This class sets up the database and all its tables.
 * It provides the functionality of reading and writing from and to it.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 9.0
 * @since   2017-06-22
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

    private static final String TABLE_NAME_HASHTAGREMINDERS = "reminder_table";
    private static final String COLUMN_HASHTAGREMINDERS_ID = "ID";
    private static final String COLUMN_HASHTAGREMINDERS_HASHTAG_NAME = "HASHTAG_NAME";
    private static final String COLUMN_HASHTAGREMINDERS_REMINDER = "REMINDER";

    private ArrayList<String> hashtags = new ArrayList<>(Arrays.asList("Ballsport", "Fitness", "Schwimmen",
                                                                        "Restaurant", "Brunch", "Business Lunch", "Mahlzeit",
                                                                        "Geburtstag", "Jahrestag", "Muttertag", "Vatertag", "Valentinstag",
                                                                        "Weihnachten", "Halloween", "Silvester", "Chanukka", "Chinesisches Neujahr", "Ostern",
                                                                        "Sommersonnenwende", "Kino",
                                                                        "Einkaufen", "Wäsche waschen", "Geschirr spühlen", "Bügeln", "Staub wischen", "Staub saugen",
                                                                        "Prüfungsanmeldung", "Kursbelegung", "Klausur", "Lerngruppe", "Lernen",
                                                                        "Laptop", "Unterlagen", "Hausaufgaben",
                                                                        "Arbeit", "Party"));

    private HashMap<String,ArrayList<String>> reminders = new HashMap<String, ArrayList<String>>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * This method creates the tables.
     * @param db This parameter is used to get the current database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        fillReminders();

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
        String createTableHashtagReminders = "CREATE TABLE " + TABLE_NAME_HASHTAGREMINDERS+ "( "
                + COLUMN_HASHTAGREMINDERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COLUMN_HASHTAGREMINDERS_HASHTAG_NAME + " TEXT, "
                + COLUMN_HASHTAGREMINDERS_REMINDER + " TEXT)";
        db.execSQL(createTableHashtagReminders);
    }

    /**
     * This method can be used to upgrade the database but is not implemented in our version.
     *
     * @param db This parameter is used to get the current database.
     * @param oldVersion This parameter is used to get the old version.
     * @param newVersion This parameter is used to get the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * THis method sets up all reminders.
     */
    private void fillReminders(){
        reminders.put("Ballsport",new ArrayList<String>(Arrays.asList("Ball","Trainingstasche")));
        reminders.put("Fitness",new ArrayList<String>(Arrays.asList("Handtuch","Trinkflasche")));
        reminders.put("Schwimmen",new ArrayList<String>(Arrays.asList("Handtuch","Schwimmbrille")));
        reminders.put("Restaurant",new ArrayList<String>(Arrays.asList("Portmonee")));
        reminders.put("Brunch",new ArrayList<String>(Arrays.asList("Portmonee")));
        reminders.put("Business Lunch",new ArrayList<String>(Arrays.asList("Portmonee")));
        reminders.put("Mahlzeit",new ArrayList<String>(Arrays.asList("Essen kochen")));
        reminders.put("Geburtstag",new ArrayList<String>(Arrays.asList("Geschenk")));
        reminders.put("Jahrestag",new ArrayList<String>(Arrays.asList("Geschenk")));
        reminders.put("Muttertag",new ArrayList<String>(Arrays.asList("Geschenk","Blumen")));
        reminders.put("Vatertag",new ArrayList<String>(Arrays.asList("Geschenk","Bier")));
        reminders.put("Valentinstag",new ArrayList<String>(Arrays.asList("Geschenk","Sekt")));
        reminders.put("Weihnachten",new ArrayList<String>(Arrays.asList("Geschenke")));
        reminders.put("Halloween",new ArrayList<String>(Arrays.asList("Kürbis","Kostüm")));
        reminders.put("Silvester",new ArrayList<String>(Arrays.asList("Partyhut","Sekt","Luftschlangen","Tröte","Feuerzeug","Feuerwerk")));
        reminders.put("Chanukka",new ArrayList<String>(Arrays.asList("Geschenke")));
        reminders.put("Chinesisches Neujahr",new ArrayList<String>(Arrays.asList("Geschenke")));
        reminders.put("Ostern",new ArrayList<String>(Arrays.asList("bunte Eier","Hefezopf")));
        reminders.put("Sommersonnenwende",new ArrayList<String>(Arrays.asList("Grillgut","Sonnencreme")));
        reminders.put("Kino",new ArrayList<String>(Arrays.asList("Karten","Portmonee")));
        reminders.put("Einkaufen",new ArrayList<String>(Arrays.asList("Einkaufsliste","Portmonee","Einkaufstasche")));
        reminders.put("Wäsche waschen",new ArrayList<String>(Arrays.asList("Kleidung sammeln")));
        reminders.put("Geschirr spühlen",new ArrayList<String>(Arrays.asList("Geschirr sammeln")));
        reminders.put("Bügeln",new ArrayList<String>(Arrays.asList("Kleiderstapel")));
        reminders.put("Staub wischen",new ArrayList<String>(Arrays.asList("Staubwedel")));
        reminders.put("Staub saugen",new ArrayList<String>(Arrays.asList("Staubsauger")));
        reminders.put("Prüfungsanmeldung",new ArrayList<String>(Arrays.asList("Liste der Fächer")));
        reminders.put("Kursbelegung",new ArrayList<String>(Arrays.asList("Liste der Fächer")));
        reminders.put("Klausur",new ArrayList<String>(Arrays.asList("Stifte","Taschenrechner","Hilfsmittel","Studierendenausweis","Personalausweis")));
        reminders.put("Lerngruppe",new ArrayList<String>(Arrays.asList("Unterlagen","Kaffee")));
        reminders.put("Lernen",new ArrayList<String>(Arrays.asList("Unterlagen","Kaffee")));
        reminders.put("Laptop",new ArrayList<String>(Arrays.asList("Ladegerät","Maus")));
        reminders.put("Unterlagen",new ArrayList<String>(Arrays.asList("Hefter","Notizblock")));
        reminders.put("Hausaufgaben",new ArrayList<String>(Arrays.asList("Unterlagen")));
        reminders.put("Arbeit",new ArrayList<String>(Arrays.asList("Essen","Getränke")));
        reminders.put("Party",new ArrayList<String>(Arrays.asList("Portmonee","Fahrkarte")));
    }

    /**
     * This method checks on systeminfo and user and creates them if the don't exist.
     */
    public void addUserAndSystemIfNotExist(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Check Hashtag Table
        checkHashtagTable();
        checkReminderTable();
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

                //Log.d(TAG, "addUserAndSystemIfNotExist: user had to be created");
            } //else Log.d(TAG, "addUserAndSystemIfNotExist: user exist");
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
                //Log.d(TAG, "addUserAndSystemIfNotExist: system info had to be created");
            } //else Log.d(TAG, "addUserAndSystemIfNotExist: system info exist");
        }
    }

    /**
     * This method add an appointment.
     *
     * @param title This parameter is used for the title of the appointment.
     * @param fullday This parameter is used for the fullday value of the appointment.
     * @param startdate This parameter is used for the startdate of the appointment.
     * @param enddate This parameter is used for the enddate of the appointment.
     * @param starttime This parameter is used for the starttime of the appointment.
     * @param endtime This parameter is used for the endtime of the appointment.
     * @param location This parameter is used for the location of the appointment.
     * @param hashtags This parameter is used for the hashtags of the appointment.
     * @return This method returns a success value.
     */
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

        //Log.d(TAG, "addEvent: hashtags in database " + hashtags.toString());
        long result = db.insert(TABLE_NAME_EVENTS, null, contentEventValues);
        if (result == -1)
            return false;
        if(hashtags.size() > 0) {
            int eventId = getEventIdByLastEvent();

            ContentValues contentEventHashtagValues = new ContentValues();
            for (String hashtag : hashtags) {
                //Log.d(TAG, "addEvent: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_EVENT_ID, eventId);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_HASHTAG_ID, hashtagId);
                result = db.insert(TABLE_NAME_EVENTMATCHING, null, contentEventHashtagValues);
            }
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    /**
     * @return This method returns the event id that was last used.
     */
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

    /**
     * This method returns the hashtags id by its name.
     * @param hashtag This parameter is used to determine which hashtag is used.
     * @return This method returns the hashtag id.
     */
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

    /**
     * This method shows events by startdate without the fullday events.
     * @param startDate This parameter is used to determine the startdate.
     * @return The method returns a cursor with all the information found.
     */
    public Cursor showEventsByStartDateWithoutFullDay(String startDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS + " WHERE " + COLUMN_EVENTS_STARTDATE + " = '" + startDate + "' "
                + "AND (" + COLUMN_EVENTS_FULLDAY + " != '1' AND "+ COLUMN_EVENTS_FULLDAY + " != 'true') "
                + "ORDER BY " + COLUMN_EVENTS_STARTTIME, null);
        return data;
    }

    /**
     * This method shows the event ids of a startdate that are fullday events.
     * @param startDate This parameter is used to determine the startdate.
     * @return The method returns a cursor with all found information.
     */
    public Cursor showEventIdsByStartDateThatAreFullDay(String startDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_FULLDAYMATCHING_EVENT_ID + " FROM " + TABLE_NAME_FULLDAYMATCHING + " WHERE " + COLUMN_FULLDAYMATCHING_DATE + " = '" + startDate + "'", null);
        return data;
    }

    /**
     * This method shows an event by its id.
     * @param eventId This parameter is used to determine the event.
     * @return The method returns the corresponding event.
     */
    public Cursor showEventByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_EVENTS + " WHERE " + COLUMN_EVENTS_ID + " = " + eventId, null);
        return data;
    }

    /**
     * This method is used to update an event.
     * @param eventId This parameter is used to determine the event that must be updated.
     * @param title This parameter is used to update the title of the event.
     * @param fullday This parameter is used to update the fullday value of the event.
     * @param startdate This parameter is used to update the startdate of the event.
     * @param enddate This parameter is used to update the enddate of the event.
     * @param starttime This parameter is used to update the starttime of the event.
     * @param endtime This parameter is used to update the endtime of the event.
     * @param location This parameter is used to update the location of the event.
     * @param hashtags This parameter is used to update the hashtags of the event.
     * @return The method returns a success value.
     */
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
        //Log.d(TAG, "updateEvent: to be assigned hashtags in database: "+hashtags.toString());

        deleteAssignedHashtagsByEventId(eventId);
        insertAssignedHashtags(eventId,hashtags);
        return true;
    }

    /**
     * This method refills the reminder table.
     */
    private void checkReminderTable() {
        reminders.clear();
        fillReminders();
    }

    /**
     * This method checks the hashtag table and refills it if necessary.
     */
    public void checkHashtagTable() {
        //Log.d(TAG, "checkHashtagTable: started");
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

    /**
     * @return This method returns a cursor with all hashtags.
     */
    public Cursor showHashtags(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_HASHTAGS, null);
        return data;
    }

    /**
     * This method returns all hashtags of an event id.
     * @param eventId This parameter is used to determine the event.
     * @return The method returns a cursor with all found information.
     */
    public ArrayList<String> showHashtagsByEventId(int eventId){
        ArrayList<String> hashtagIds = new ArrayList<String>();
        ArrayList<String> hashtags = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_EVENTMATCHING_HASHTAG_ID + " FROM " + TABLE_NAME_EVENTMATCHING + " WHERE " + COLUMN_EVENTMATCHING_EVENT_ID + " = " + eventId, null);

        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                hashtagIds.add(data.getString(0));
                //Log.d(TAG, "showHashtagsByEventId: retrieved id " + data.getString(0));
            }
        }
        data.close();
        //Log.d(TAG, "showHashtagsByEventId: hashtag ids: " + hashtagIds.toString());

        for(String id: hashtagIds){
            hashtags.add(showHashtagNameById(id));
        }
        //Log.d(TAG, "showHashtagsByEventId: hashtags: " + hashtags.toString());

        return hashtags;
    }

    /**
     * This method returns all hashtags of an to-do id.
     * @param todoId This parameter is used to determine the to-do.
     * @return The method returns a cursor with all found information.
     */
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

    /**
     * This method returns the hashtag name by its id.
     * @param hashtagId This parameter is used to determine the hashtag.
     * @return The method returns the name of the hashtag.
     */
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

    /**
     * This method inserts the assigned hashtags of an event.
     * @param eventId This parameter is used to determine the event.
     * @param hashtags This parameter is used to process the hashtags.
     */
    public void insertAssignedHashtags(int eventId, ArrayList<String> hashtags){
        SQLiteDatabase db = this.getWritableDatabase();
        if(hashtags.size() > 0) {
            for (String hashtag : hashtags) {
                ContentValues contentEventHashtagValues = new ContentValues();
                //Log.d(TAG, "insertAssignedHashtags: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                //Log.d(TAG, "insertAssignedHashtags: hashtag id " + hashtagId);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_EVENT_ID, eventId);
                contentEventHashtagValues.put(COLUMN_EVENTMATCHING_HASHTAG_ID, hashtagId);
                db.insert(TABLE_NAME_EVENTMATCHING, null, contentEventHashtagValues);
            }
        }
    }

    /**
     * This method deletes the assigned hashtags of an event.
     * @param eventId This parameter is used to determine the event.
     */
    public void deleteAssignedHashtagsByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_EVENTMATCHING +
                " WHERE " + COLUMN_EVENTMATCHING_EVENT_ID + " = " + eventId;
        db.execSQL(query);
    }

    /**
     * This method deletes an event by its id.
     * @param eventId This parameter is used to determine the event.
     */
    public void deleteEventByEventId(int eventId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_EVENTS +
                " WHERE " + COLUMN_EVENTS_ID + " = " + eventId;
        db.execSQL(query);
    }

    /**
     * This method shows a to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     * @return The method returns a cursor with all information found.
     */
    public Cursor showToDoByToDoId(int todoId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS + " WHERE " + COLUMN_TODO_ID + " = " + todoId, null);
        return data;
    }

    /**
     * This method deletes an to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     */
    public void deleteToDoByToDoId(int todoId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    /**
     * This method is used to update a to-do.
     * @param todoId This parameter is used to determine the to-do.
     * @param title This parameter is used to update the title of the to-do.
     * @param duration This parameter is used to update the duration of the to-do.
     * @param location This parameter is used to update the location of the to-do.
     * @param hashtags This parameter is used to update the hashtags of the to-do.
     * @return The method returns a success value.
     */
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

    /**
     * This method inserts the assigned hashtags of a to-do.
     * @param todoId This parameter is used to determine the to-do.
     * @param hashtags This parameter is used to process the hashtags.
     */
    private void insertAssignedHashtagsOfToDo(int todoId, ArrayList<String> hashtags) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(hashtags.size() > 0) {
            for (String hashtag : hashtags) {
                //Log.d(TAG, "insertAssignedHashtags: hashtag name: " + hashtag);
                ContentValues contentToDoHashtagValues = new ContentValues();
                int hashtagId = getHashtagIdByName(hashtag);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_TODO_ID, todoId);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_HASHTAG_ID, hashtagId);
                db.insert(TABLE_NAME_TODOMATCHING, null, contentToDoHashtagValues);
            }
        }
    }

    /**
     * This method deletes the assigned hashtags of a to-do.
     * @param todoId This parameter is used to determine the to-do.
     */
    private void deleteAssignedHashtagsByToDoId(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_TODOMATCHING +
                " WHERE " + COLUMN_TODOMATCHING_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    /**
     * This method is used to return the title of a to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     * @return The method returns the title of the to-do.
     */
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

    /**
     * This method is used to return the duration of a to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     * @return The method returns the duration of the to-do.
     */
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

    /**
     * This method is used to return the state of a to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     * @return The method returns the state of the to-do.
     */
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

    /**
     * @return This method returns all to-dos.
     */
    public Cursor showToDos() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS, null);
        return data;
    }

    /**
     * This method is used to add a to-do.
     * @param title This parameter is used to add the title for the to-do.
     * @param duration This parameter is used to add the duration for the to-do.
     * @param location This parameter is used to add the location for the to-do.
     * @param state This parameter is used to add the state for the to-do.
     * @param hashtags This parameter is used to add the hashtags for the to-do.
     * @return The method returns a success value.
     */
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
                //Log.d(TAG, "addToDo: hashtag name: " + hashtag);
                int hashtagId = getHashtagIdByName(hashtag);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_TODO_ID, todoId);
                contentToDoHashtagValues.put(COLUMN_TODOMATCHING_HASHTAG_ID, hashtagId);
                result = db.insert(TABLE_NAME_TODOMATCHING, null, contentToDoHashtagValues);
            }
            if (result == -1)
                return false;
            else
                return true;
        }
        else
            return true;
    }

    /**
     * @return This method returns the last used to-do id.
     */
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

    /**
     * This method sets the state of a to-do by its id.
     * @param todoId This parameter is used to determine the to-do.
     * @param state This parameter is used to set the state.
     */
    public void setStateOfToDoByToDoId(int todoId, boolean state) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TODOS +
                " SET " + COLUMN_TODO_STATE + " = '" + state + "'" +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    /**
     * @return This method returns the information about the user.
     */
    public Cursor showUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_USER, null);
        return data;
    }

    /**
     * This method is used to update the users name.
     * @param name This parameter is used to update the name.
     */
    public void updateUserName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NAME + " = '" + name + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users address.
     * @param address This parameter is used to update the address.
     */
    public void updateUserAddress(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_HOMEADDRESS + " = '" + address + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users do not disturb value.
     * @param doNotDisturbValue This parameter is used to update the value.
     */
    public void updateUserDoNotDisturb(boolean doNotDisturbValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB+ " = '" + doNotDisturbValue + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users avatar use value.
     * @param avatarUse This parameter is used to update the value.
     */
    public void updateUserAvatarUse(boolean avatarUse) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_AVATAR_USE + " = '" + avatarUse + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users birthdate.
     * @param birhtdate This parameter is used to update the value.
     */
    public void updateUserBirthdate(String birhtdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_BIRTHDATE + " = '" + birhtdate + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users do not disturb starttime.
     * @param startTime This parameter is used to update the value.
     */
    public void updateUserDoNotDisturbStartTime(String startTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB_STARTTIME+ " = '" + startTime + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method is used to update the users do not disturb endtime.
     * @param endTime This parameter is used to update the value.
     */
    public void updateUserDoNotDisturbEndTime(String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_USER +
                " SET " + COLUMN_USER_NOTDISTURB_ENDTIME+ " = '" + endTime + "'" +
                " WHERE " + COLUMN_USER_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method inserts a fullday event for all days.
     * @param currentDateString This parameter is used to determine the date.
     * @param eventId This parameter is used to determine the event.
     */
    public void insertFullDayEvent(String currentDateString, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentFullDayMatchingValues = new ContentValues();
        contentFullDayMatchingValues.put(COLUMN_FULLDAYMATCHING_DATE, currentDateString);
        contentFullDayMatchingValues.put(COLUMN_FULLDAYMATCHING_EVENT_ID, eventId);
        db.insert(TABLE_NAME_FULLDAYMATCHING, null, contentFullDayMatchingValues);
    }

    /**
     * This method is used to delete a fullday event.
     * @param eventId This parameter is used to determine the event.
     */
    public void deleteFulldayMatchingByEventId(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_FULLDAYMATCHING +
                " WHERE " + COLUMN_FULLDAYMATCHING_EVENT_ID + " = " + eventId;
        db.execSQL(query);
    }

    /**
     * This method is used to get all to-dos that have a maximum duration of the timeslot length.
     * @param timeslotlength This paramater is used to check the length.
     * @return The method returns a cursor with all information found.
     */
    public Cursor showToDosByMaxDuration(int timeslotlength) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_TODOS +
                " WHERE (" + COLUMN_TODO_STATE + " = '0' OR " + COLUMN_TODO_STATE + " = 'false') AND " + COLUMN_TODO_DURATION + " <= " + timeslotlength, null);
        return data;
    }

    /**
     * @return This method returns the users name.
     */
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

    /**
     * @return This method returns the users do not disturb state.
     */
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

    /**
     * @return This method returns the users do not disturb start time.
     */
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

    /**
     * @return This method returns the users do not disturb end time.
     */
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

    /**
     * @return This method returns all the system information.
     */
    public Cursor getSystemInformation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_SYSTEMINFO +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1", null);
        return data;
    }

    /**
     * This method sets the city and temperature of the system info table.
     * @param city This parameter is used to set the city information.
     * @param temperature This parameter is used to set the temperature information.
     */
    public void setSystemInfoData(String city, String temperature) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_CITY+ " = '" + city + "', " +
                COLUMN_SYSTEMINFO_TEMPERATURE + " = '" + temperature + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method sets the weather id, sunrise and sunset of the system info table.
     * @param weatherId This parameter is used to set the weather id information.
     * @param sunrise This parameter is used to set the sunrise information.
     * @param sunset This parameter is used to set the sunset information.
     */
    public void setSystemInfoWeatherData(int weatherId, long sunrise, long sunset) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_WEATHER_ID+ " = '" + weatherId + "', " +
                COLUMN_SYSTEMINFO_SUNRISE + " = '" + sunrise + "', " +
                COLUMN_SYSTEMINFO_SUNSET + " = '" + sunset + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method sets the timestamp of the system info table.
     * @param timestamp This parameter is used to set the timestamp information.
     */
    public void setSystemInfoTimeStamp(long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SYSTEMINFO +
                " SET " + COLUMN_SYSTEMINFO_TIMESTAMP + " = '" + timestamp + "' " +
                " WHERE " + COLUMN_SYSTEMINFO_ID + " = 1";
        db.execSQL(query);
    }

    /**
     * This method schedules a to-do that was accepted by the user.
     * @param todoId This parameter is used to determine the to-do.
     * @param date This parameter is used to set the date.
     * @param hour This parameter is used to set the time.
     */
    public void scheduleToDoByToDoId(Integer todoId, String date, int hour) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TODOS +
                " SET " + COLUMN_TODO_STARTDATE + " = '" + date + "', " +
                COLUMN_TODO_STARTTIME + " = '" + hour + "' " +
                " WHERE " + COLUMN_TODO_ID + " = " + todoId;
        db.execSQL(query);
    }

    /**
     * This method returns all to-dos of a specific date.
     * @param date This parameter is used to determine the date.
     * @return This method returns a cursor with all found information.
     */
    public Cursor showToDosByStartDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_TODO_STARTTIME + ", " + COLUMN_TODO_DURATION + " FROM " + TABLE_NAME_TODOS +
                " WHERE " + COLUMN_TODO_STARTDATE + " = '" + date + "'", null);
        return data;
    }

    /**
     * This method returns a list of all reminders of a hashtag.
     * @param hashtag This parameter is used to determine the hashtag.
     * @return The method returns a list of reminders of the hashtag.
     */
    public ArrayList<String> showRemindersByHashtagString(String hashtag) {
        return reminders.get(hashtag);
    }

    /**
     * This method returns the title of an event by its id.
     * @param eventId This parameter is used to determine the event.
     * @return The method returns the title.
     */
    public String getEventTitleByEventId(String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_EVENTS_TITLE + " FROM " + TABLE_NAME_EVENTS +
                " WHERE " + COLUMN_EVENTS_ID + " = " + eventId, null);
        String title = "";
        if(data.getCount() != 0 && data.moveToNext()){
            title = data.getString(0);
        }
        return title;
    }

    /**
     * @return This method returns the avatar use state of the user.
     */
    public boolean getUserAvatarOptionState() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COLUMN_USER_AVATAR_USE+ " FROM " + TABLE_NAME_USER +
                " WHERE " + COLUMN_USER_ID + " = 1", null);
        boolean avatarUse = false;
        if(data.getCount() != 0 && data.moveToNext()){
            String value = data.getString(0);
            if(value.equals("1") || value.equals("true"))
                avatarUse = true;
        }
        return avatarUse;
    }

    /**
     * This method returns the hashtags of all current events.
     * @param possibleEvents This parameter is used to determine the hashtags.
     * @return The method returns the hashtags of all current events in a list.
     */
    public ArrayList<String> getHashtagsOfCurrentActiveEventsByPossibleEvents(ArrayList<Integer> possibleEvents) {
        Set<String> hashtagSet = new HashSet<String>();
        Calendar currentDate = Calendar.getInstance();
        for(int eventId: possibleEvents){
            Cursor data = showEventByEventId(eventId);
            if(data.getCount() != 0 && data.moveToNext()){
                String[] startTimeInfo = data.getString(5).split(":");
                String[] endTimeInfo = data.getString(6).split(":");
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,Integer.valueOf(startTimeInfo[0]));
                startTime.set(Calendar.MINUTE,Integer.valueOf(startTimeInfo[1]));
                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY,Integer.valueOf(endTimeInfo[0]));
                endTime.set(Calendar.MINUTE,Integer.valueOf(endTimeInfo[1]));
                if(currentDate.after(startTime) && currentDate.before(endTime)){
                    ArrayList<String> currentHashtags = showHashtagsByEventId(eventId);
                    for(String hashtag: currentHashtags){
                        hashtagSet.add(hashtag);
                    }
                }
            }
        }
        //Log.d(TAG, "getHashtagsOfCurrentActiveEventsByPossibleEvents: " + hashtagSet.toString());
        return new ArrayList<String>(hashtagSet);
    }
}
