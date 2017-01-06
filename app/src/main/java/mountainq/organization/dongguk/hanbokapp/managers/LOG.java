package mountainq.organization.dongguk.hanbokapp.managers;

import android.util.Log;

/**
 * Created by dnay2 on 2017-01-06.
 */

public class LOG {
    private static final String PUBLISH = "hanbok";
    private static final String DEBUG = "test";

    private static final String D = "debug";
    private static final String E = "error";

    public static void D(String MESSAGE){
        Log.d(DEBUG, MESSAGE);
    }

    public static void E(String MESSAGE){
        Log.e(DEBUG, MESSAGE);
    }
}
