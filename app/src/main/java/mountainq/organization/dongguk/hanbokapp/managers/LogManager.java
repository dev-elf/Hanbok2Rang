package mountainq.organization.dongguk.hanbokapp.managers;

import android.util.Log;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class LogManager {
    public static final String PUBLISH = "hanbok";
    public static final String DEBUG = "test";

    public static final String D = "debug";
    public static final String E = "error";

    public void logMessage(String LEVEL, String MESSAGE){

        switch (LEVEL){
            case D:
                Log.d(DEBUG, MESSAGE);
                break;
            case E:
                Log.d(DEBUG, MESSAGE);
                break;
        }
    }

}
