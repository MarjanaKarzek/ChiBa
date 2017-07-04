package de.emm.teama.chibaapp.Main;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.emm.teama.chibaapp.Utils.BottomNavigationViewHelper;
import de.emm.teama.chibaapp.R;
import de.emm.teama.chibaapp.Utils.DatabaseHelper;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate: starting.");


        //Success Toast for add Event
        int successState_addAppointment = getIntent().getIntExtra("EXTRA_SUCCESS_STATE_ADD_APPOINTMENT",3);
        if(successState_addAppointment == 1)
            Toast.makeText(MainActivity.this,"Termin angelegt",Toast.LENGTH_LONG).show();
        else if(successState_addAppointment == 0)
            Toast.makeText(MainActivity.this,"Fehler beim Anlegen des Termins",Toast.LENGTH_LONG).show();

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
}
