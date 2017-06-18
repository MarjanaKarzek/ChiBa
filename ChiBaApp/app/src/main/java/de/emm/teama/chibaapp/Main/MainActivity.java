package de.emm.teama.chibaapp.Main;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.emm.teama.chibaapp.Utils.BottomNavigationViewHelper;
import de.emm.teama.chibaapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // private static final int ACTIVITY_NUM = 0;
    private SectionsPagerAdapter adapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate: starting.");

        //Setup View Pager
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_calendar);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_todolist);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_settings);

        setupBottomNavigationView();
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
       // Menu menu = bottomNavigationView.getMenu();
        //MenuItem menuItem = menu.getItem(0);
        //menuItem.setChecked(false);
    }
}
