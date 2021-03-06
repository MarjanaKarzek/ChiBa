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
import java.util.ArrayList;
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
 * <h1>ChiBa Application Class</h1>
 * This class sets up the database on application start and checks whether user and system exist.
 * It sets up and maintains timer for notifications for todos and appointments.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 3.0
 * @since   2017-07-02
 */
public class ChiBaApplication extends Application {
    public static DatabaseHelper database;
    private static final String TAG = "ChiBaApplication";
    private static HashMap<Integer, Timer> appointmentTimers = new HashMap<Integer, Timer>();
    private static Context context;

    /**
     * On creation of the application this method gets called.
     * It initalizes database and checks for user and system.
     * Afterwards it initalizes the timers for existing todos and events.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        database = new DatabaseHelper(this);
        database.addUserAndSystemIfNotExist();
        setUpTimerForToDos();
        setUpTimerForAppointments();
    }

    /**
     * This method sets up the timers for existing appointments on create and schedules them.
     */
    private void setUpTimerForAppointments() {
        Calendar today = Calendar.getInstance();
        String dateFormat = "d. MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);

        Cursor data = database.showEventsByStartDateWithoutFullDay(simpleDateFormat.format(today.getTime()));
        if (data.getCount() != 0) {
            while (data.moveToNext()) {
                int eventId = Integer.valueOf(data.getString(0));
                ArrayList<String> hashtags = database.showHashtagsByEventId(eventId);
                String[] starttime = data.getString(5).split(":");
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starttime[0]));
                date.set(Calendar.MINUTE, Integer.valueOf(starttime[1]));
                date.add(Calendar.MINUTE, -30);
                if (today.before(date)) {
                    Date time = date.getTime();
                    Timer timer = new Timer();
                    appointmentTimers.put(eventId, timer);
                    //Log.d(TAG, "setUpTimerForAppointments: event " + eventId + " scheduled for " + simpleDateFormat.format(date.getTime()));
                    appointmentTimers.get(eventId).schedule(new ScheduledAppointmentNotification(eventId, hashtags, this, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), getResources()), time);
                }
            }
        }
    }

    /**
     * This method adds a new timer for a newly added appointment and schedules it.
     * @param eventId This parameter is used to retrieve information from the database.
     * @param startTimeString This parameter is used to schedule the notification a half hour earlier.
     * @param hashtags This parameter is used to remind the user properly.
     */
    public static void addAppointmentTimer(int eventId, String startTimeString, ArrayList<String> hashtags) {
        //Log.d(TAG, "addAppointmentTimer: Adding new scheduled notification");
        String[] starttime = startTimeString.split(":");
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(starttime[0]));
        date.set(Calendar.MINUTE, Integer.valueOf(starttime[1]));
        date.add(Calendar.MINUTE, -30);
        Date time = date.getTime();

        Timer timer = new Timer();
        appointmentTimers.put(eventId, timer);
        //Log.d(TAG, "addAppointmentTimer: event " + eventId + " scheduled for " + simpleDateFormat.format(date.getTime()));
        appointmentTimers.get(eventId).schedule(new ScheduledAppointmentNotification(eventId, hashtags, context, (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE), context.getResources()), time);
    }

    /**
     * This method deletes the timer associated with the appointment that got deleted itself.
     * @param eventId This parameter is used to delete the correct timer.
     */
    public static void deleteApplicationTimer(int eventId) {
        if (appointmentTimers.containsKey(eventId)) {
            appointmentTimers.get(eventId).cancel();
            appointmentTimers.remove(eventId);
            //Log.d(TAG, "deleteApplicationTimer: Timer deleted for " + eventId);
        }
    }

    /**
     * This method edits the timer of an edited appointment to match its new start time.
     * For this it delets the old timer and inserts a new one.
     * @param eventId This parameter is used to find the correct timer.
     * @param startTimeString This parameter is used to schedule the timer to the new start time
     * @param assignedHashtags This parameter is used to remind the user properly.
     */
    public static void editAppointmentTimer(int eventId, String startTimeString, ArrayList<String> assignedHashtags) {
        deleteApplicationTimer(eventId);
        addAppointmentTimer(eventId, startTimeString, assignedHashtags);
    }

    /**
     * This method sets up the timer to check free time slots for todos and schedules them every hour.
     * If the application gets started at the beginning of a new hour, it creats a single notification.
     * It is assumed that todos can still be done when maximum 10 minutes are over by the notifications schedule time.
     */
    private void setUpTimerForToDos() {
        //Log.d(TAG, "setUpTimerForToDos: scheduling notifications");
        Timer timer = new Timer();
        Calendar currentTime = Calendar.getInstance();

        if (currentTime.get(Calendar.MINUTE) <= 10) {
            currentTime.add(Calendar.MINUTE, 2);
            ScheduledToDoNotification singleNotification = new ScheduledToDoNotification(this, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), getResources());
            singleNotification.run();
        }
        currentTime.add(Calendar.HOUR_OF_DAY, 1);
        currentTime.set(Calendar.MINUTE, 0);
        Date date = currentTime.getTime();
        long period = 3600000;

        timer.schedule(new ScheduledToDoNotification(this, (NotificationManager) getSystemService(NOTIFICATION_SERVICE), getResources()), date, period);
    }

    /**
     * <h1>Scheduled To-Do Notification Class</h1>
     * This class checks whether the current time slot is free and finds a matching to-do.
     * On run it initalizes the to-do notification.
     * <p>
     * In the comments find log entries to be used for debugging purposes.
     *
     * @author  Marjana Karzek
     * @version 2.0
     * @since   2017-07-08
     */
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

        private String[] textChoices = {", du hättest jetzt etwas Zeit. Möchtest du dich um folgendes ToDo kümmern: ",
                                        ", was hältst du davon folgendes zu erledigen: ",
                                        ", du könntest in der nächsten Zeit folgendes erledigen: "};
        private String[] yesChoices = {"ja, ok",
                                       "na gut",
                                       "ja, gut"};
        private String[] noChoices = {"nein, danke",
                                      "nee",
                                      "später"};

        /**
         * The constructor for the class ScheduledToDoNotification.
         * Initalizes context, notifyMgr, resources and the pending intent for the cancel option of the to-do.
         *
         * @param context This parameter is used to initalize the field context.
         * @param notifyMgr This parameter is used to initalize the field notifyMgr.
         * @param resources This parameter is used to initalize the field resources.
         */
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

        /**
         * This method calculates which hours are available.
         * Available hours for to-dos are in between 8 and 21 o'clock.
         * The method excludes hours that are in between the do not disturb time slot.
         */
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

        /**
         * This method first figures out which time slots are free and which are blocked.
         * If there are any fullday events, all slots are blocked.
         */
        private void getFreeTimeSlots() {
            currentFreeTimeSlots.clear();
            for (int hour : availableHours) {
                currentFreeTimeSlots.put(hour, true);
            }

            Cursor dataFullDay = database.showEventIdsByStartDateThatAreFullDay(simpleDateFormat.format(currentDate.getTime()));
            if (dataFullDay.getCount() != 0) {
                Set<Integer> keySet = currentFreeTimeSlots.keySet();
                currentFreeTimeSlots.clear();
                for (Integer key : keySet) {
                    currentFreeTimeSlots.put(key, false);
                }
            } else {
                Cursor data = database.showEventsByStartDateWithoutFullDay(simpleDateFormat.format(currentDate.getTime()));
                if (data.getCount() != 0) {
                    while (data.moveToNext()) {
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
                        String[] starttime = toDoData.getString(0).split(":");
                        int starthour = Integer.valueOf(starttime[0]);
                        int duration = Integer.valueOf(toDoData.getString(1));

                        if (currentFreeTimeSlots.get(starthour)) {
                            do {
                                currentFreeTimeSlots.remove(starthour);
                                currentFreeTimeSlots.put(starthour, false);
                                starthour++;
                                duration--;
                            } while (duration != 0);
                        }
                    }
                }
                //Log.d(TAG, "getFreeTimeSlots: currentTimeSlots " + currentFreeTimeSlots.toString());
            }
        }

        /**
         * This method gets called on the scheduled time.
         * According to the free time slots it figures out which to-do could match the current slot.
         * It gets a random one and sends the notification.
         */
        public void run() {
            getCurrentAvailableHours();
            if (availableHours.contains(Calendar.getInstance().HOUR_OF_DAY)) {
                getFreeTimeSlots();
                Calendar currentTime = Calendar.getInstance();
                int timeslotlength = 0;
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int followingHour = currentTime.get(Calendar.HOUR_OF_DAY);
                if (currentTime.get(Calendar.MINUTE) <= 10) {
                    if (currentFreeTimeSlots.containsKey(currentHour)) {
                        do {
                            timeslotlength += 1;
                            followingHour += 1;
                        } while (currentFreeTimeSlots.containsKey(followingHour));
                    }
                    Cursor data = database.showToDosByMaxDuration(timeslotlength);
                    if (data.getCount() != 0) {
                        int randomIndex = 0;
                        if (data.getCount() != 1)
                            randomIndex = random.nextInt(data.getCount() - 1);
                        String selectedToDo = "";
                        data.moveToPosition(randomIndex);
                        selectedToDo = data.getString(1);
                        applicationIntentAction2.putExtra("todoId", data.getString(0));
                        pendingApplicationIntentAction2 = PendingIntent.getBroadcast(context, 0, applicationIntentAction2, PendingIntent.FLAG_UPDATE_CURRENT);
                        createPushNotification(selectedToDo);
                    }
                }
            }
            //else Log.d(TAG, "run: not scheduled yet");
        }

        /**
         * This method generates a random text for a notification.
         * It sets up the notification and publishes it.
         *
         * @param selectedToDo This parameter is used to display a matching text in the notification.
         */
        public void createPushNotification(String selectedToDo) {
            int randomText = random.nextInt(textChoices.length);
            int randomYes = random.nextInt(yesChoices.length);
            int randomNo = random.nextInt(noChoices.length);

            String text = database.getUserName() + textChoices[randomText] + selectedToDo;
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification_todo)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.chiba_profile))
                            .setContentTitle("ToDo Erinnerung")
                            .setContentText(text)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(text))
                            .addAction(R.drawable.ic_home, noChoices[randomNo], pendingApplicationIntentAction1)
                            .addAction(R.drawable.ic_home, yesChoices[randomYes], pendingApplicationIntentAction2);
            notifyMgr.notify(001, builder.build());
        }
    }

    /**
     * <h1>Scheduled Appointment Notification Class</h1>
     * This class sets up a random notification for e scheduled appointment.
     * <p>
     * In the comments find log entries to be used for debugging purposes.
     *
     * @author  Marjana Karzek
     * @version 1.0
     * @since   2017-07-10
     */
    private static class ScheduledAppointmentNotification extends TimerTask {
        private Context context;
        private NotificationManager notifyMgr;
        private Resources resources;
        private String eventId;

        private ArrayList<String> hashtags;
        private HashMap<String, ArrayList<String>> reminders = new HashMap<String, ArrayList<String>>();
        private Random random = new Random();

        /**
         * The constructor for the class ScheduledAppointmentNotification.
         * Initializes its fields and sets up the matching reminders.
         *
         * @param eventId This parameter is used to initalize the field eventId.
         * @param hashtags This parameter is used to initalize the field hashtags.
         * @param context This parameter is used to initalize the field context.
         * @param notifyMgr This parameter is used to initalize the field notifyMgr.
         * @param resources This parameter is used to initalize the field resources.
         */
        public ScheduledAppointmentNotification(int eventId, ArrayList<String> hashtags, Context context, NotificationManager notifyMgr, Resources resources) {
            this.context = context;
            this.notifyMgr = notifyMgr;
            this.resources = resources;
            this.eventId = "" + eventId;

            this.hashtags = hashtags;
            setupReminders();
        }

        /**
         * This method sets up the reminders for this event notification.
         */
        private void setupReminders() {
            for (String hashtag : hashtags) {
                reminders.put(hashtag, database.showRemindersByHashtagString(hashtag));
            }
        }

        @Override
        /**
         * This method gets called on the scheduled time.
         * According to the provided reminders it sets up a notification text.
         */
        public void run() {
            if (reminders.size() > 0) {
                int randomHashtag = random.nextInt(reminders.size());
                int randomReminder = random.nextInt(reminders.get(hashtags.get(randomHashtag)).size());
                String reminderTag = reminders.get(hashtags.get(randomHashtag)).get(randomReminder);
                createPushNotification("Hallo " + database.getUserName() + ", für deinen Termin " + database.getEventTitleByEventId(eventId) + " vergiss nicht folgendes: " + reminderTag);
            }
        }

        /**
         * This method sets up a notification with the given text.
         *
         * @param text This parameter is used for the notification text.
         */
        public void createPushNotification(String text) {
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification_appointment)
                            .setContentTitle("Termin Erinnerung")
                            .setContentText(text)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(text))
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.chiba_profile));
            notifyMgr.notify(002, builder.build());
        }
    }
}
