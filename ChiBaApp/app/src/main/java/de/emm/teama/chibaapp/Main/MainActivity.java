package de.emm.teama.chibaapp.Main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import de.emm.teama.chibaapp.Utils.BottomNavigationViewHelper;
import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private HashMap<Integer,Boolean> currentFreeTimeSlots = new HashMap<Integer, Boolean>();
    private Calendar currentDate = Calendar.getInstance();
    private String dateFormat = "d. MMMM yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.GERMANY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate: starting.");

        tryToastOnSuccess();
        getFreeTimeSlots();
        setUpTimerForToDos();

        //Setup View Pager
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_orange);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_calendar);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_todolist);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_orange);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_calendar_orange);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_todolist_orange);
                        break;
                    default:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings_orange);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_calendar);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_todolist);
                        break;
                    default:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_calendar);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_todolist);
                        break;
                    default:
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);
                }
            }
        });

        setupBottomNavigationView();
        database.checkHashtagTable();
    }

    private void getFreeTimeSlots() {
        currentFreeTimeSlots.clear();
        currentFreeTimeSlots.put(8, true);
        currentFreeTimeSlots.put(9, true);
        currentFreeTimeSlots.put(10, true);
        currentFreeTimeSlots.put(11, true);
        currentFreeTimeSlots.put(12, true);
        currentFreeTimeSlots.put(13, true);
        currentFreeTimeSlots.put(14, true);
        currentFreeTimeSlots.put(15, true);
        currentFreeTimeSlots.put(16, true);
        currentFreeTimeSlots.put(17, true);
        currentFreeTimeSlots.put(18, true);
        currentFreeTimeSlots.put(19, true);
        currentFreeTimeSlots.put(20, true);
        currentFreeTimeSlots.put(21, true);
        Cursor dataFullDay = database.showEventIdsByStartDateThatAreFullDay(simpleDateFormat.format(currentDate.getTime()));
        //if there is a fullday event, the complete day is blocked
        if(dataFullDay.getCount() != 0){
            Set<Integer> keySet = currentFreeTimeSlots.keySet();
            currentFreeTimeSlots.clear();
            for(Integer key: keySet){
                currentFreeTimeSlots.put(key, false);
            }
        }
        else{
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

                    do{
                        currentFreeTimeSlots.remove(starthour);
                        currentFreeTimeSlots.put(starthour,false);
                        starthour++;
                    }while(starthour < endhour);
                }
            }
            Log.d(TAG, "getFreeTimeSlots: currentTimeSlots " + currentFreeTimeSlots.toString());
        }
    }

    private void tryToastOnSuccess() {
        //Success Toast for add Appointment
        int successState_addAppointment = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_ADD_APPOINTMENT",3);
        if(successState_addAppointment == 1)
            Toast.makeText(MainActivity.this,"Termin angelegt",Toast.LENGTH_LONG).show();
        else if(successState_addAppointment == 0)
            Toast.makeText(MainActivity.this,"Fehler beim Anlegen des Termins",Toast.LENGTH_LONG).show();

        //Success Toast for edit Appointment
        int successState_editAppointment = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_EDIT_APPOINTMENT",3);
        if(successState_editAppointment == 1)
            Toast.makeText(MainActivity.this,"Termin geändert",Toast.LENGTH_LONG).show();
        else if(successState_editAppointment == 0)
            Toast.makeText(MainActivity.this,"Fehler beim Ändern des Termins",Toast.LENGTH_LONG).show();

        //Success Toast for add To Do
        int successState_addToDo = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_ADD_TODO",3);
        if(successState_addToDo == 1)
            Toast.makeText(MainActivity.this,"ToDo angelegt",Toast.LENGTH_LONG).show();
        else if(successState_addToDo == 0)
            Toast.makeText(MainActivity.this,"Fehler beim Anlegen des ToDos",Toast.LENGTH_LONG).show();

        //Success Toast for edit To Do
        int successState_editToDo = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_EDIT_TODO",3);
        if(successState_editToDo == 1)
            Toast.makeText(MainActivity.this,"ToDo geändert",Toast.LENGTH_LONG).show();
        else if(successState_editToDo == 0)
            Toast.makeText(MainActivity.this,"Fehler beim Ändern des ToDos",Toast.LENGTH_LONG).show();
    }

    /**
     * ViewPager setup
     * Adds the fragments: Main, Calendar, ToDoList and Settings
     */
    private void setupViewPager(ViewPager viewPager){adapter.addFragment(new MainFragment());
        adapter.addFragment(new CalendarFragment());
        adapter.addFragment(new ToDoListFragment());
        adapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(MainActivity.this, bottomNavigationView);
    }

    private void setUpTimerForToDos() {
        HashMap<Integer,Boolean> freeTimeSlots = currentFreeTimeSlots;
        Timer timer = new Timer();
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.MINUTE,0);
        Date date = currentTime.getTime();
        long period = 3600000;

        timer.schedule(new ScheduledToDoNotification(freeTimeSlots, this, (NotificationManager)getSystemService(NOTIFICATION_SERVICE), getResources()), date, period);
    }

    private static class ScheduledToDoNotification extends TimerTask {
        private HashMap<Integer,Boolean> currentFreeTimeSlots;
        private Random random = new Random();
        private Context context;
        private NotificationManager notifyMgr;
        private Resources resources;
        private Intent applicationIntent;
        private PendingIntent pendingApplicationIntent;

        public ScheduledToDoNotification(HashMap<Integer,Boolean> currentFreeTimeSlots, Context context, NotificationManager notifyMgr, Resources resources){
            this.currentFreeTimeSlots = currentFreeTimeSlots;
            this.context = context;
            this.notifyMgr = notifyMgr;
            this.resources = resources;
            applicationIntent = new Intent(context, MainActivity.class);
            pendingApplicationIntent = PendingIntent.getBroadcast(context, 0, applicationIntent, 0);
        }

        public void run()
        {
            //write your code here
            Log.d(TAG, "run: scheduled notification");
            Calendar currentTime = Calendar.getInstance();
            //get current timeslot length
            int timeslotlength = 0;
            int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
            int followingHour = currentTime.get(Calendar.HOUR_OF_DAY);
            Log.d(TAG, "run: curentHour: " + currentHour + " following Hour " + followingHour);

            if(currentTime.get(Calendar.MINUTE) < 10){
                if(currentFreeTimeSlots.get(currentHour)){
                    do {
                        timeslotlength++;
                        followingHour++;
                    }while(currentFreeTimeSlots.get(followingHour));
                }
                Log.d(TAG, "run: current hour: " + currentHour);
                Log.d(TAG, "run: hour state " + currentFreeTimeSlots.get(currentHour));
                Log.d(TAG, "run: timeslot: " + timeslotlength);
                Cursor data = database.showToDosByMaxDuration(timeslotlength);
                if(data.getCount() != 0){
                    int randomIndex = random.nextInt(data.getCount()-1);
                    String selectedToDo = "";
                    data.moveToPosition(randomIndex);
                    selectedToDo = data.getString(1);
                    Log.d(TAG, "run: selected ToDo " + selectedToDo);
                    createPushNotification(selectedToDo);
                }
           }
            else

           // if(currentTime.get(Calendar.MINUTE) < 10){
                if(currentFreeTimeSlots.containsKey(currentHour)){
                    do {
                        timeslotlength += 1;
                        followingHour += 1;
                    }while(currentFreeTimeSlots.containsKey(followingHour));
                }
                Log.d(TAG, "run: current hour: " + currentHour);
                Log.d(TAG, "run: hour state " + currentFreeTimeSlots.get(currentHour));
                Log.d(TAG, "run: timeslot: " + timeslotlength);
                Cursor data = database.showToDosByMaxDuration(timeslotlength);
                if(data.getCount() != 0){
                    int randomIndex = random.nextInt(data.getCount()-1);
                    String selectedToDo = "";
                    data.moveToPosition(randomIndex+1);
                    selectedToDo = data.getString(1);
                    Log.d(TAG, "run: selected ToDo " + selectedToDo);
                    //TODO check do not disturb option here
                    createPushNotification(selectedToDo);
                //}
            }
            else
                Log.d(TAG, "run: not scheduled yet");
        }

        public void createPushNotification(String selectedToDo){
            String text = "Du hättest jetzt etwas Zeit. Möchtest du dich um folgendes ToDo kümmern: " + selectedToDo;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification_todo)
                            .setContentTitle("ToDo Erinnerung")
                            .setContentText(text)
                            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_chiba_profile))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(text))
                            .addAction(R.drawable.ic_home,"nein, danke",pendingApplicationIntent)
                            .addAction(R.drawable.ic_home,"ja, ok",pendingApplicationIntent);
            int mNotificationId = 001;
            notifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }
}
