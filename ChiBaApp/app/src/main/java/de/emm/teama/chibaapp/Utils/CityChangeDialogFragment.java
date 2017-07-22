package de.emm.teama.chibaapp.Utils;

import android.support.v4.app.DialogFragment;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import de.emm.teama.chibaapp.R;

/**
 * <h1>CityChangeDialogFragment Class</h1>
 * This class sets up a DialogFragment to change the city.
 * This class was not implemented properly in the release version.
 * <p>
 * In the comments find log entries to be used for debugging purposes.
 *
 * @author  Marjana Karzek
 * @version 1.0
 * @since   2017-07-12
 */

public class CityChangeDialogFragment extends DialogFragment{
    private static final String TAG = "CityChangeDialogFragment";

    /**
     * This method creates a new dialog fragment.
     *
     * @return The method returns the dialog
     */
    public static CityChangeDialogFragment newInstance() {
        CityChangeDialogFragment dialog = new CityChangeDialogFragment();
        return dialog;
    }

    public interface CityChangeDialogFragmentListener {
        public void onReturnValue(String city);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_city_change_dialog_fragment, container);
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
