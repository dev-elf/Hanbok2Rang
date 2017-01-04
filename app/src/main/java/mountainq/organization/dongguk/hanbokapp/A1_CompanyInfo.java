package mountainq.organization.dongguk.hanbokapp;

import android.os.Bundle;
import android.widget.TextView;

import mountainq.organization.dongguk.hanbokapp.activities.SActivity;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class A1_CompanyInfo extends SActivity {

    TextView title, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_company_info);
        init();
        showBackBtn();
    }

    private void init(){
        title = (TextView) findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);

        title.setText("한복이랑 이란?");
        detail.setText(
                "-한복입고 놀러가고 싶으신가요?\n" +
                "-친구, 커플과의 데이트 코스를 찾으신다구요?\n" +
                "\n" +
                "한복과 한국 전통문화의 만남\n" +
                "나만의 이색 전통문화 데이트 코스를\n" +
                "한복이랑이 알려드립니다.");
    }
}
