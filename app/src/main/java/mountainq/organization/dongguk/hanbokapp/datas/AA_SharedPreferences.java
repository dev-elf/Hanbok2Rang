package mountainq.organization.dongguk.hanbokapp.datas;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mountainq.organization.dongguk.hanbokapp.AppController;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class AA_SharedPreferences {
    private static AA_SharedPreferences instance;
    public static AA_SharedPreferences getInstance(){
        if(instance == null){
            instance = new AA_SharedPreferences();
        }
        return instance;
    }
    static {
        instance = new AA_SharedPreferences();
    }

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private AA_SharedPreferences(){
        mPrefs =
                PreferenceManager.getDefaultSharedPreferences(AppController.getInstance().getApplicationContext());
        mEditor = mPrefs.edit();
        mEditor.apply();
    }

    private static final String BOOK_MARK = "book_mark";
        public int getBookMark(){
          return 0;
        }

    public void clear(){
        mEditor.clear();
        mEditor.commit();
    }

}
