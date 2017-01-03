package mountainq.organization.dongguk.hanbokapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class SplashActivity extends AppCompatActivity {

    private AA_StaticDatas mData = AA_StaticDatas.getInstance();

    ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView) findViewById(R.id.logo);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(mData.getWidth()*5/7, mData.getWidth()*5/7);
        logo.setLayoutParams(llp);
        Picasso.with(this).load(R.drawable.hanbok_logo).fit().into(logo);
        new SplashTask().execute();
    }

    private class SplashTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(2400);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }
}
