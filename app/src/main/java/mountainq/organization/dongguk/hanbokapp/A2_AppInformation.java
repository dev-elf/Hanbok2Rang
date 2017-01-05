package mountainq.organization.dongguk.hanbokapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.activities.SActivity;
import mountainq.organization.dongguk.hanbokapp.adapters.APIAdapter;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class A2_AppInformation extends SActivity {

    ArrayList<String> apis = new ArrayList<>();
    APIAdapter adapter;
    ListView apiListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_app_info);
        apiListView = (ListView) findViewById(R.id.apiListView);
        setAPIInfo();
        changeToolbarText("개발정보");
        visibleBackBtn(true);
    }

    private void setAPIInfo(){
        apis.add("한국관광공사 관광API(국문) : \n" + "키워드검색");
        apis.add("한국관광공사 관광API(국문) : \n" + "위치기반검색");
        apis.add("다음지도 API");
        adapter = new APIAdapter(this, apis);
        apiListView.setAdapter(adapter);
    }
}
