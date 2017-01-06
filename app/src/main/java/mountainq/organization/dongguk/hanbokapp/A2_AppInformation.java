package mountainq.organization.dongguk.hanbokapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.activities.SActivity;
import mountainq.organization.dongguk.hanbokapp.adapters.APIAdapter;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class A2_AppInformation extends SActivity {
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();
    private ArrayList<String> apis = new ArrayList<>();
    private APIAdapter adapter;
    ListView apiListView;
    LinearLayout intro;
    LinearLayout.LayoutParams llp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_app_info);
        apiListView = (ListView) findViewById(R.id.apiListView);
        intro = (LinearLayout) findViewById(R.id.intro);
        llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mData.getHeight()/4);
        intro.setLayoutParams(llp);
        intro.setGravity(Gravity.CENTER);
        ((TextView) intro.getChildAt(0)).setTextSize(18);
        setAPIInfo();
        changeToolbarText("어플정보");
        visibleBackBtn(true);
    }

    private void setAPIInfo(){
        apis.add("한국관광공사 관광API(국문) : " + "키워드검색");
        apis.add("한국관광공사 관광API(국문) : " + "위치기반검색");
        apis.add("다음지도 API");
        apis.add("다음로컬검색 API");
        adapter = new APIAdapter(this, apis);
        apiListView.setAdapter(adapter);
    }
}
