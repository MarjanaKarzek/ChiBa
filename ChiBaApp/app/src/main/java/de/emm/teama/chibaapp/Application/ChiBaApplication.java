package de.emm.teama.chibaapp.Application;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.ActionReceiver;
import de.emm.teama.chibaapp.Utils.DatabaseHelper;

/**
 * Created by marja on 02.07.2017.
 */

public class ChiBaApplication extends Application {
    //Database Initialization
    public static DatabaseHelper database ;
    private static final String TAG = "ChiBaApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        database = new DatabaseHelper(this);
        database.addUserAndSystemIfNotExist();
        setUpTimerForToDos();
    }

    private void setUpTimerForToDos() {
        Log.d(TAG, "setUpTimerForToDos: scheduling notifications");
        Timer timer = new Timer();
        Calendar currentTime = Calendar.getInstance();
        Date date = currentTime.getTime();
        String dateFormat = "d. MMMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);

        if (currentTime.get(Calendar.MINUTE) <= 10) {
            currentTime.add(Calendar.MINUTE,2);
            date = currentTime.getTime();
            //Log.d(TAG, "setUpTimerForToDos: create single notification");
            //Log.d(TAG, "setUpTimerForToDos: scheduled for " + simpleDateFormat.format(date));
            ScheduledToDoNotification singleNotification = new ScheduledToDoNotification(this, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), getResources());
            singleNotification.run();
        }
        currentTime.add(Calendar.HOUR_OF_DAY, 1);
        currentTime.set(Calendar.MINUTE, 0);
        date = currentTime.getTime();
        long period = 3600000;


        //Log.d(TAG, "setUpTimerForToDos: scheduled for " + simpleDateFormat.format(date));

        timer.schedule(new ScheduledToDoNotification(this, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), getResources()), date, period);
    }

    private static class ScheduledToDoNotification extends TimerTask {
        private Random random = new Random();
        private Context context;
        private NotificationManager notifyMgr;
        private Resources resources;

        private Intent applicationIntentAction1;
        private PendingIntent pendingApplicationIntentAction1;
        private Intent applicationIntentAction2;
        private PendingIntent pendingApplicationIntentAction2;

        private HashSet<Integer> availableHours = new HashSet<Integer>();
        private HashMap<Integer, Boolean> currentFreeTimeSlots = new HashMap<Integer, Boolean>();
        private Calendar currentDate = Calendar.getInstance();
        private String dateFormat = "d. MMMM yyyy";
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);

        public ScheduledToDoNotification(Context context, NotificationManager notifyMgr, Resources resources) {
            this.context = context;
            this.notifyMgr = notifyMgr;
            this.resources = resources;

            applicationIntentAction1 = new Intent(context, ActionReceiver.class);
            applicationIntentAction1.setAction("action1");
            pendingApplicationIntentAction1 = PendingIntent.getBroadcast(context, 0, applicationIntentAction1, PendingIntent.FLAG_UPDATE_CURRENT);

            applicationIntentAction2 = new Intent(context, ActionReceiver.class);
            applicationIntentAction2.setAction("action2");
        }

        private void getFreeTimeSlots() {
            currentFreeTimeSlots.clear();
            for (int hour : availableHours) {
                currentFreeTimeSlots.put(hour, true);
            }

            Cursor dataFullDay = database.showEventIdsByStartDateThatAreFullDay(simpleDateFormat.format(currentDate.getTime()));
            //if there is a fullday event, the complete day is blocked
            if (dataFullDay.getCount() != 0) {
                Set<Integer> keySet = currentFreeTimeSlots.keySet();
                currentFreeTimeSlots.clear();
                for (Integer key : keySet) {
                    currentFreeTimeSlots.put(key, false);
                }
            } else {
                //0 COLUMN_EVENTS_ID
                //1 COLUMN_EVENTS_TITLE
                //2 COLUMN_EVENTS_FULLDAY
                //3 COLUMN_EVENTS_STARTDATE
                //4 COLUMN_EVENTS_ENDDATE
                //5 COLUMN_EVENTS_STARTTIME
                //6 COLUMN_EVENTS_ENDTIME
                //7 COLUMN_EVENTS_LOCATION
                Cursor data = database.showEventsByStartDateWithoutFullDay(simpleDateFormat.format(currentDate.getTime()));
                if (data.getCount() != 0) {
                    while (data.moveToNext()) {
                        //set blocks to false if blocked
                        String[] starttime = data.getString(5).split(":");
                        int starthour = Integer.valueOf(starttime[0]);

                        String[] endtime = data.getString(6).split(":");
                        int endhour = Integer.valueOf(endtime[0]);
                        int endminute = Integer.valueOf(endtime[1]);

                        do {
                            currentFreeTimeSlots.remove(starthour);
                            currentFreeTimeSlots.put(starthour, false);
                            starthour++;
                        } while (starthour < endhour);

                        if (endminute > 0) {
                            currentFreeTimeSlots.remove(endhour);
                            currentFreeTimeSlots.put(endhour, false);
                        }
                    }
                }
                Cursor toDoData = database.showToDosByStartDate(simpleDateFormat.format(currentDate.getTime()));
                if (toDoData.getCount() != 0) {
                    while (toDoData.moveToNext()) {
                        //set blocks to false if blocked by scheduled to do
                        String[] starttime = toDoData.getString(0).split(":");
                        int starthour = Integer.valueOf(starttime[0]);
                        int duration = Integer.valueOf(toDoData.getString(1));

                        if(currentFreeTimeSlots.get(starthour)){
                            do {
                                currentFreeTimeSlots.remove(starthour);
                                currentFreeTimeSlots.put(starthour, false);
                                starthour++;
                                duration--;
                            }while(duration != 0);
                        }
                    }
                }
                //Log.d(TAG, "getFreeTimeSlots: currentTimeSlots " + currentFreeTimeSlots.toString());
            }
        }

        private void getCurrentAvailableHours() {
            availableHours.clear();
            availableHours.add(8);
            availableHours.add(9);
            availableHours.add(10);
            availableHours.add(11);
            availableHours.add(12);
            availableHours.add(13);
            availableHours.add(14);
            availableHours.add(15);
            availableHours.add(16);
            availableHours.add(17);
            availableHours.add(18);
            availableHours.add(19);
            availableHours.add(20);
            availableHours.add(21);
            if (database.getUserDoNotDisturbOptionState()) {
                String starttimeString = database.getUserDoNotDisturbStartTime();
                String endtimeString = database.getUserDoNotDisturbEndTime();

                String[] starttime = starttimeString.split(":");
                String[] endtime = endtimeString.split(":");

                int starthour = Integer.valueOf(starttime[0]);
                int endhour = Integer.valueOf(endtime[0]);
                int endminute = Integer.valueOf(endtime[1]);

                do {
                    availableHours.remove(starthour);
                    starthour++;
                    if (starthour == 24)
                        starthour = 0;
                } while (starthour != endhour);
                if (endminute > 0)
                    availableHours.remove(endhour);
            }
        }

        public void run() {
            getCurrentAvailableHours();
            if (availableHours.contains(Calendar.getInstance().HOUR_OF_DAY)) {
                //Log.d(TAG, "run: available");
                //calculate current free timeslots
                getFreeTimeSlots();
                //write your code here
                //Log.d(TAG, "run: scheduled notification");
                Calendar currentTime = Calendar.getInstance();
                //get current timeslot length
                int timeslotlength = 0;
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int followingHour = currentTime.get(Calendar.HOUR_OF_DAY);
                //Log.d(TAG, "run: currentHour: " + currentHour + " following Hour " + followingHour);

                if (currentTime.get(Calendar.MINUTE) <= 10) {
                    if (currentFreeTimeSlots.containsKey(currentHour)) {
                        do {
                            timeslotlength += 1;
                            followingHour += 1;
                        } while (currentFreeTimeSlots.containsKey(followingHour));
                    }
                    //Log.d(TAG, "run: current hour: " + currentHour);
                    //Log.d(TAG, "run: hour state " + currentFreeTimeSlots.get(currentHour));
                    //Log.d(TAG, "run: timeslot: " + timeslotlength);
                    Cursor data = database.showToDosByMaxDuration(timeslotlength);
                    if (data.getCount() != 0) {
                        int randomIndex = 0;
                        if (data.getCount() != 1)
                            randomIndex = random.nextInt(data.getCount() - 1);
                        String selectedToDo = "";
                        data.moveToPosition(randomIndex);
                        selectedToDo = data.getString(1);
                        //Log.d(TAG, "run: selected To Do id " + Integer.valueOf(data.getString(0)));
                        applicationIntentAction2.putExtra("todoId", data.getString(0));
                        pendingApplicationIntentAction2 = PendingIntent.getBroadcast(context, 0, applicationIntentAction2, PendingIntent.FLAG_UPDATE_CURRENT);
                        //Log.d(TAG, "run: selected To Do " + selectedToDo);
                        createPushNotification(selectedToDo);
                    }
                }
            } else
                Log.d(TAG, "run: not scheduled yet");
        }

        public void createPushNotification(String selectedToDo) {
            String text = database.getUserName() + ", du hättest jetzt etwas Zeit. Möchtest du dich um folgendes ToDo kümmern: " + selectedToDo;
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification_todo)
                            .setContentTitle("ToDo Erinnerung")
                            .setContentText(text)
                            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_home))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(text))
                            .addAction(R.drawable.ic_home, "nein, danke", pendingApplicationIntentAction1)
                            .addAction(R.drawable.ic_home, "ja, ok", pendingApplicationIntentAction2);
            notifyMgr.notify(001, builder.build());
        }
    }
}
