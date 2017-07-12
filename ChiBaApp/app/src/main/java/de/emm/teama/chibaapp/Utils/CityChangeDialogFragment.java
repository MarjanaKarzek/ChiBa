package de.emm.teama.chibaapp.Utils;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import de.emm.teama.chibaapp.R;

/**
 * Created by Marjana Karzek on 12.07.2017.
 */

public class CityChangeDialogFragment extends DialogFragment{
    private static final String TAG = "CityChangeDialogFragment";

    public static AppointmentDetailDialogFragment newInstance() {
        AppointmentDetailDialogFragment dialog = new AppointmentDetailDialogFragment();
        return dialog;
    }

    public interface CityChangeDialogFragmentListener {
        public void onReturnValue(String city);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_appointment_detail_fragment, container);
        final CityChangeDialogFragmentListener activity = (CityChangeDialogFragmentListener) getActivity();

        RadioButton berlin = (RadioButton) view.findViewById(R.id.radioButtonOptionBerlin);
        RadioButton cologne = (RadioButton) view.findViewById(R.id.radioButtonOptionCologne);
        RadioButton frankfurt = (RadioButton) view.findViewById(R.id.radioButtonOptionFrankfurt);
        RadioButton hamburg = (RadioButton) view.findViewById(R.id.radioButtonOptionHamburg);
        RadioButton munich = (RadioButton) view.findViewById(R.id.radioButtonOptionMunich);

        berlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onReturnValue("BERLIN,DE");
            }
        });
        cologne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onReturnValue("KOELN,DE");
            }
        });
        frankfurt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onReturnValue("FRANKFURT MAIN FLUGHAFEN,DE");
            }
        });
        hamburg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onReturnValue("HAMBURG,DE");
            }
        });
        munich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onReturnValue("LANDKREIS MÜNCHEN,DE");
            }
        });

        return view;
    }
}
