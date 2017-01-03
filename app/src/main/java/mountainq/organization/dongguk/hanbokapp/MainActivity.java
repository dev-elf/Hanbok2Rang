package mountainq.organization.dongguk.hanbokapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import mountainq.organization.dongguk.hanbokapp.activities.NavigationDrawerActivity;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;
import mountainq.organization.dongguk.hanbokapp.datas.LocationItem;
import mountainq.organization.dongguk.hanbokapp.parsers.TourAPIParser;
import mountainq.organization.dongguk.hanbokapp.views.CustomDaumMapLayout;
import mountainq.organization.dongguk.hanbokapp.views.CustomDaumMapView;

/**
 * created by geusan 2017-01-03
 *  이곳은 메인 화면인데 보여줄게 없습니다ㅜㅜ
 *  지도만 보여줍시다
 */
public class MainActivity extends NavigationDrawerActivity {

    /**
     * 검색 창
     */
    EditText searchEditText;
    ImageView searchButton;

    /**
     * 맵뷰를 가져오기
     */
    FrameLayout mainContainer;
    RelativeLayout mapViewContainer;
    CustomDaumMapLayout mapLayout;
    CustomDaumMapView mapView;

    private ArrayList<LocationItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        mainContainer = (FrameLayout) findViewById(R.id.mapFrame);
        initMap();
    }

    private void initMap(){
        mapLayout = new CustomDaumMapLayout(this);
        mapView = (CustomDaumMapView) mapLayout.getMapView();

    }


    public void onMenuClickListener(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.group1:
                intent = new Intent(MainActivity.this, A1_SearchActivity.class);
                break;
            case R.id.g1_menu1:
                break;
            case R.id.g1_menu2:
                break;
            case R.id.group2:
                break;
            case R.id.g2_menu1:
                break;
            case R.id.g2_menu2:
                break;
            case R.id.g2_menu3:
                break;
        }
        startActivity(intent);
    }

    public void onFloatingButtonClickListener(View v){
        switch (v.getId()){

        }
    }


    /**
     * 데이터 통신 관광 API로부터 관광지 정보를 받아와 리스트로 출력한다.
     */
    class SearchTask extends AsyncTask<String, Integer, Void> {

        private URL url = null;
        HttpURLConnection connection = null;

        @Override
        protected Void doInBackground(String... params) {
            String keyword = "";
            Log.d("Test", "SearchTask is doing");
            try {
                keyword = URLEncoder.encode(params[0], "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                url = new URL(AA_StaticDatas.TOUR_API_LOCATION_BASED_LIST +
                        "&mapX=" + 126.993263 +
                        "&mapY=" + 37.560268 +
                        "&radius=" + 1000
                        );
                connection = (HttpURLConnection) url.openConnection();
                int code = connection.getResponseCode();
                switch (code) {
                    case 200:
                        TourAPIParser parser = new TourAPIParser();
                        items = parser.parse(connection.getInputStream());
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Log.d("test", connection.getErrorStream().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            searchAdapter = new TourSearchAdapter(items, mContext);
//            searchList.setAdapter(searchAdapter);
//            Log.d("Test", "parsed item : " + items.toString());
//            showList();
//            searchEt.setTextColor(StaticData.WHITE_TEXT_COLOR);
//            searchEt.setBackgroundColor(StaticData.MAIN_COLOR);

//            View v = getCurrentFocus();
//            if (v != null) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
        }
    }
}
