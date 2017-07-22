package de.emm.teama.chibaapp.Main;


import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import de.emm.teama.chibaapp.Utils.BottomNavigationViewHelper;
import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * <h1>MainActivity Class</h1>
 * This class provides the main acitvity for the application.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 4.0
 * @since   2017-06-18
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    /**
     * This method sets up the view.
     * @param savedInstanceState This parameter is used to get the saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate: starting.");
        tryToastOnSuccess();

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
                switch (tab.getPosition()) {
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
                switch (tab.getPosition()) {
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
                switch (tab.getPosition()) {
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

    /**
     * This method display the Toasts if there is a success state available.
     */
    private void tryToastOnSuccess() {
        //Success Toast for add Appointment
        int successState_addAppointment = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_ADD_APPOINTMENT", 3);
        if (successState_addAppointment == 1)
            Toast.makeText(MainActivity.this, "Termin angelegt", Toast.LENGTH_LONG).show();
        else if (successState_addAppointment == 0)
            Toast.makeText(MainActivity.this, "Fehler beim Anlegen des Termins", Toast.LENGTH_LONG).show();

        //Success Toast for edit Appointment
        int successState_editAppointment = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_EDIT_APPOINTMENT", 3);
        if (successState_editAppointment == 1)
            Toast.makeText(MainActivity.this, "Termin geändert", Toast.LENGTH_LONG).show();
        else if (successState_editAppointment == 0)
            Toast.makeText(MainActivity.this, "Fehler beim Ändern des Termins", Toast.LENGTH_LONG).show();

        //Success Toast for add To Do
        int successState_addToDo = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_ADD_TODO", 3);
        if (successState_addToDo == 1)
            Toast.makeText(MainActivity.this, "ToDo angelegt", Toast.LENGTH_LONG).show();
        else if (successState_addToDo == 0)
            Toast.makeText(MainActivity.this, "Fehler beim Anlegen des ToDos", Toast.LENGTH_LONG).show();

        //Success Toast for edit To Do
        int successState_editToDo = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_EDIT_TODO", 3);
        if (successState_editToDo == 1)
            Toast.makeText(MainActivity.this, "ToDo geändert", Toast.LENGTH_LONG).show();
        else if (successState_editToDo == 0)
            Toast.makeText(MainActivity.this, "Fehler beim Ändern des ToDos", Toast.LENGTH_LONG).show();
    }

    /**
     * This method adds the fragments: Main, Calendar, ToDoList and Settings.
     */
    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new MainFragment());
        adapter.addFragment(new CalendarFragment());
        adapter.addFragment(new ToDoListFragment());
        adapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * This method performs the setup of the BottomNavigationView.
     */
    private void setupBottomNavigationView() {
        //Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.enableNavigation(MainActivity.this, bottomNavigationView);
    }
}
