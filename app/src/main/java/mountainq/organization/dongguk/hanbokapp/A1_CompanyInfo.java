package mountainq.organization.dongguk.hanbokapp;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mountainq.organization.dongguk.hanbokapp.activities.SActivity;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class A1_CompanyInfo extends SActivity {

    private AA_StaticDatas mData = AA_StaticDatas.getInstance();
    TextView title, detail;
    ImageView logoImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_company_info);
        changeToolbarText("한복이랑 소개");
        visibleBackBtn(true);
        title = (TextView) findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);
        logoImg = (ImageView) findViewById(R.id.logo);
        init();
    }


    private void init(){

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        title.setLayoutParams(llp);
        title.setPadding(0,0,0,mData.getHeight()/15);
        detail.setLayoutParams(llp);
        detail.setPadding(mData.getWidth()/30,mData.getHeight()/15,mData.getWidth()/30,0);

        llp = new LinearLayout.LayoutParams(mData.getWidth()/2+30, mData.getWidth()/2+30);
        logoImg.setLayoutParams(llp);
        logoImg.setCropToPadding(true);
        logoImg.setPadding(mData.getWidth()/35,mData.getWidth()/35,mData.getWidth()/35,mData.getWidth()/35);
        logoImg.setBackgroundResource(R.drawable.zz_logo_bg);
        logoImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Picasso.with(this).load(R.drawable.hanbok_logo).fit().into(logoImg);

        title.setText("한복이랑 이란?");
        detail.setText(
                "-한복입고 놀러가고 싶으신가요?\n" +
                "-친구, 커플과의 데이트 코스를 찾으신다구요?\n" +
                "\n" +
                "한복과 한국 전통문화의 만남\n" +
                "나만의 이색 전통문화 데이트 코스를\n" +
                "한복이랑이 알려드립니다.\n" +
                "\n\n" +
                "당신의 소중한 추억 한복이랑 함께하세요");
    }
}
