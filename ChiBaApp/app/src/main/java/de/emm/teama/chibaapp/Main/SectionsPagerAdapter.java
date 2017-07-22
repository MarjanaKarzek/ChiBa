package de.emm.teama.chibaapp.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>SectionsPagerAdapter Class</h1>
 * This class enables the switching between fragments
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-06-18
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPagerAdapter";
    private final List<Fragment> fragmentList = new ArrayList<>();

    /**
     * Constructor to set up the adapter using the FragmentManager
     * @param fm This parameter is use to retrieve the FragmentManaager from its parent.
     */
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * This method is used to get the fragment item.
     * @param position This parameter is used to determine the item.
     * @return The method returns the chosen Fragment.
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * @return The method returns the amount of available Fragments.
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * This method adds a fragment to the list.
     * @param fragment This parameter is used to parse the Fragment.
     */
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
