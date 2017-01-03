package mountainq.organization.dongguk.hanbokapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by dnay2 on 2016-11-17.
 */

public abstract class FChildActivity extends Fragment {

    protected Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }
}
