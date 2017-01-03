package mountainq.organization.dongguk.hanbokapp.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import mountainq.organization.dongguk.hanbokapp.R;


/**
 * Created by dnay2 on 2016-11-17.
 * Fragment
 */

public abstract class FActivity extends FragmentActivity {

    protected Toolbar toolbar;
    public TextView toolbarText;
    protected ImageView backBtn;
    protected Typeface fontNanum;
    protected Context mContext;

    protected static final HashMap<String, String> menuHashMap = new HashMap<>();

    public void onCreate(Bundle savedInstanceState, int layout/*, boolean isNav*/) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");


//        fontNanum = Typeface.createFromAsset(getAssets(), "fonts/NanumBarunGothicRegular.ttf");

//        initLayout(getResources().getString(R.string.app_name));
    }

    public void setToolbarText(String text){
        ((TextView) findViewById(R.id.toolbarText)).setText(text);
    }

    protected void initLayout(String text){
        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setTypeface(fontNanum);
        toolbarText.setText(text);
    }


}
