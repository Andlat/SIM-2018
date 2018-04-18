import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import daynight.daynnight.R;

/**
 * Created by Antoine Mascolo on 2018-04-17.
 */

public class RecompenseFragment extends Fragment {

    public RecompenseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.layout_recompense, container, false);
    }

}
