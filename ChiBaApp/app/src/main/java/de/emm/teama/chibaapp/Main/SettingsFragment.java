package de.emm.teama.chibaapp.Main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import de.emm.teama.chibaapp.R;

import static de.emm.teama.chibaapp.Application.ChiBaApplication.database;

/**
 * Created by Marjana Karzek on 18.06.2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    private EditText name;
    private TextView birthdate;
    private EditText address;

    private Switch avatarUse;
    private Switch doNotDisturb;
    private TextView notDisturbStartTime;
    private TextView notDisturbEndTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Cursor data = database.showUser();

        name = (EditText) view.findViewById(R.id.settingsEditTextName);
        birthdate = (TextView) view.findViewById(R.id.settingsTextViewBirthdateSelection);
        address = (EditText) view.findViewById(R.id.settingsEditTextHomeaddress);
        avatarUse = (Switch) view.findViewById(R.id.settingsSwitchAvatarUse);
        doNotDisturb = (Switch) view.findViewById(R.id.settingsSwitchDoNotDisturb);
        notDisturbStartTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewStartTime);
        notDisturbEndTime = (TextView) view.findViewById(R.id.settingsDoNotDisturbTextViewEndTime);


        Log.d(TAG, "onCreateView: data count: " + data.getCount());
        if (data.getCount() != 0 && data.moveToNext()) {
            name.setText(data.getString(1));
            birthdate.setText(data.getString(2));
            address.setText(data.getString(3));
            if(data.getString(4).contains("1") || data.getString(4).contains("true"))
                avatarUse.setChecked(true);
            else
                avatarUse.setChecked(false);
            if(data.getString(5).contains("1") || data.getString(4).contains("true"))
                doNotDisturb.setChecked(true);
            else
                doNotDisturb.setChecked(false);
            notDisturbStartTime.setText(data.getString(6));
            notDisturbEndTime.setText(data.getString(7));

        }
        return view;
    }
}
