package mountainq.organization.dongguk.hanbokapp;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mountainq.organization.dongguk.hanbokapp.activities.NavigationDrawerActivity;
import mountainq.organization.dongguk.hanbokapp.adapters.BookMarkAdapter;
import mountainq.organization.dongguk.hanbokapp.adapters.LocationAdapter;
import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;
import mountainq.organization.dongguk.hanbokapp.datas.BookMark;
import mountainq.organization.dongguk.hanbokapp.datas.DataBaseManager;
import mountainq.organization.dongguk.hanbokapp.datas.LocationItem;
import mountainq.organization.dongguk.hanbokapp.parsers.TourAPIParser;
import mountainq.organization.dongguk.hanbokapp.search.Item;
import mountainq.organization.dongguk.hanbokapp.search.OnFinishSearchListener;
import mountainq.organization.dongguk.hanbokapp.search.Searcher;

import static mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas.DAUM_MAPS_ANDROID_APP_API_KEY;

/**
 * created by geusan 2017-01-03
 *  이곳은 메인 화면인데 보여줄게 없습니다ㅜㅜ
 *  지도만 보여줍시다
 */
public class MainActivity extends NavigationDrawerActivity implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.POIItemEventListener{
    /**
     * 종원 커스텀
     */
    private static final String LOG_TAG = "MainActivity";
    private MapView mapView;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    private boolean isUsingCustomLocationMarker = false;

    private static final String KEYWORD = "keyword";
    private static final String LOCATION = "location";
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();

    private static final String DB_INITIAL = "initial";
    private static final String DB_INSERT = "insert";
    private static final String DB_DELETE = "delete";

    /**
     * 맵뷰를 가져오기
     */
    FrameLayout mainContainer;


    /**
     * 즐겨찾기 리스트 가져오기
     */
    ListView bookMarkListView;
    BookMarkAdapter bookMarkAdapter;
    private ArrayList<BookMark> bookMarks = new ArrayList<>();


    /**
     * 검색 리스트
     */
    LocationAdapter locationAdapter;
    ListView searchListView;
    EditText searchEditText;
    ImageView searchButton;
    private ArrayList<LocationItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        mContext = this;
        mainContainer = (FrameLayout) findViewById(R.id.mapFrame);
        searchListView = (ListView) findViewById(R.id.searchListView);
        searchEditText = (EditText) findViewById(R.id.searchet);
        bookMarkListView = (ListView) findViewById(R.id.bookMarkListView);
        getBookMarkList();

        ActivityCompat.requestPermissions(this, new String[]{
        Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        MapLayout mapLayout = new MapLayout(this);
        mapView = mapLayout.getMapView();
        mapView.setDaumMapApiKey(DAUM_MAPS_ANDROID_APP_API_KEY);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);
        mapView.setMapType(MapView.MapType.Standard);
        mapView.setPOIItemEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.daumMap);
        mapViewContainer.addView(mapLayout);
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
        }
        if(intent != null) startActivity(intent);
    }

    public void onFloatingButtonClickListener(View v){
        switch (v.getId()){
            case R.id.mapFunctionMyLocation:
                if(mapView.getCurrentLocationTrackingMode().toString()=="TrackingModeOff"){
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }else{
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    mapView.setShowCurrentLocationMarker(false);
                }
                break;
            case R.id.mapFunctionNear:
                mapView.removeAllCircles();
                String query = "한복대여점";
                MapPoint.GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
                double latitude = geoCoordinate.latitude; // 위도
                double longitude = geoCoordinate.longitude; // 경도
                int radius = 250; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                Toast.makeText(getApplicationContext(), String.format("위치는 (%f,%f)", latitude, longitude), Toast.LENGTH_SHORT).show();
                String apikey = DAUM_MAPS_ANDROID_APP_API_KEY;

                Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
                searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
                        mapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        showResult(itemList); // 검색 결과 보여줌
                    }
                    @Override
                    public void onFail() {
                        showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                    }
                });


                MapCircle circle1 = new MapCircle(
                        MapPoint.mapPointWithGeoCoord(latitude, longitude), // center
                        350, // radius
                        Color.argb(77, 255, 165, 0), // strokeColor
                        Color.argb(77, 255, 255, 0) // fillColor
                );
                circle1.setTag(1234);
                mapView.addCircle(circle1);
                break;
            case  R.id.mapFunctionSetting:
                break;
            case R.id.mapFunctionZoomIn:
                mapView.zoomIn(true);
                break;
            case R.id.mapFunctionZoomOut:
                mapView.zoomOut(true);
                break;
            case R.id.searchbtn:
                searchByKeyword(searchEditText.getText().toString());
                break;
        }
    }

    private void insertBookMark(BookMark item){
        new DBTask().execute(DB_INSERT, String.valueOf(item.getPrimeKey()), item.getLocationName(), String.valueOf(item.getLon()), String.valueOf(item.getLat()));
    }

    private void deleteBookMark(BookMark item){
        deleteBookMark(item.getPrimeKey());
    }

    private void deleteBookMark(int primeKey){
        new DBTask().execute(DB_DELETE, String.valueOf(primeKey));
    }

    private void getBookMarkList(){
        new DBTask().execute(DB_INITIAL);

    }

    class DBTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            DataBaseManager dbm = new DataBaseManager(mContext);
            switch (params[0]){
                case DB_INITIAL:
                    bookMarks = dbm.printList();
                    return DB_INITIAL;
                case DB_INSERT:
                    dbm.insert(params[1], params[2], params[3], params[4]);
                    break;
                case DB_DELETE:
                    dbm.delete(params[1]);
                    break;
            }
            bookMarks = dbm.printList();
            bookMarkAdapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals(DB_INITIAL)){
                bookMarkAdapter = new BookMarkAdapter(mContext, bookMarks);
                bookMarkListView.setAdapter(bookMarkAdapter);
            }
        }
    }

    private void searchByKeyword(String keyword){
        new SearchTask().execute(KEYWORD, keyword);
    }

    private void searchByLocation(double lon, double lat){
        new SearchTask().execute(LOCATION, String.valueOf(lon), String.valueOf(lat));
    }


    /**
     * 데이터 통신 관광 API로부터 관광지 정보를 받아와 리스트로 출력한다.
     */
    class SearchTask extends AsyncTask<String, Integer, String> {

        private URL url = null;
        HttpURLConnection connection = null;

        @Override
        protected String  doInBackground(String... params) {
            String keyword = "", lon="", lat="";

            if(params[0].equals(KEYWORD)){
                try {
                    keyword = URLEncoder.encode(params[1], "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                lon = params[1];
                lat = params[2];

            }
            Log.d("Test", "SearchTask is doing");

            try {
                if(params[0].equals(KEYWORD)){
                    url = new URL(AA_StaticDatas.TOUR_API_KEYWORD + keyword);
                } else {
                    url = new URL(AA_StaticDatas.TOUR_API_LOCATION_BASED_LIST +
                            "&mapX=" + lon +
                            "&mapY=" + lat +
                            "&radius=" + 1000
                    );
                    Log.d("test", "lon = " + lon + "   lat = " + lat);
                }

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
            if(params[0].equals(KEYWORD)) return KEYWORD;
            else return LOCATION;
        }

        @Override
        protected void onPostExecute(String param) {
            locationAdapter = new LocationAdapter(items, mContext);
            Log.d("Test", "parsed item : " + items.toString());
            if(param.equals(KEYWORD)){
                searchListView.setAdapter(locationAdapter);
//            showList();
//            searchEditText.setTextColor(StaticData.WHITE_TEXT_COLOR);
//            searchEditText.setBackgroundColor(StaticData.MAIN_COLOR);

                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                //알림창에서 확인후 추천
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("대여점 근처의 관광지 추천 받으시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showLocationBasedList(locationAdapter);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        }

        private void showLocationBasedList(LocationAdapter locationAdapter){
            View tempView = View.inflate(MainActivity.this, R.layout.zz_dialogue, null);
            ListView newListView = (ListView) tempView.findViewById(R.id.searchListView);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(mData.getWidth()*4/5, mData.getWidth()*4/5);
            newListView.setLayoutParams(llp);
            newListView.setAdapter(locationAdapter);

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("대여점 주변 정보")
                    .setView(tempView)
                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

    }
    /**
     * 다음 맵 코드
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();

        //Toast.makeText(getApplicationContext(), String.format("현재 위치는 (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude), Toast.LENGTH_SHORT).show();

        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }
    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
    private void onFinishReverseGeoCoding(String result) {
        Toast.makeText(MainActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
        Log.i(LOG_TAG, String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
    }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage(String.format("Long-Press on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }
    //custom

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }
    //검색기능 추가
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            Item item = mTagItemMap.get(poiItem.getTag());
            if (item == null) return null;
            ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(item.title);
            TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            textViewDesc.setText(item.address);
            imageViewBadge.setImageDrawable(createDrawableFromUrl(item.imageUrl));
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }

    }
    public void onMapViewInitialized(MapView mapView) {
        Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        MapPoint.GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도

    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }

        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mapView.getPOIItems();
        if (poiItems.length > 0) {
            mapView.selectPOIItem(poiItems[0], false);
        }
    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Object fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    //말풍선 커스텀
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        final Item item = mTagItemMap.get(mapPOIItem.getTag());
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(item.id).append("\n");
        sb.append("title=").append(item.title).append("\n");
        sb.append("imageUrl=").append(item.imageUrl).append("\n");
        sb.append("address=").append(item.address).append("\n");
        sb.append("newAddress=").append(item.newAddress).append("\n");
        sb.append("zipcode=").append(item.zipcode).append("\n");
        sb.append("phone=").append(item.phone).append("\n");
        sb.append("category=").append(item.category).append("\n");
        sb.append("longitude=").append(item.longitude).append("\n");
        sb.append("latitude=").append(item.latitude).append("\n");
        sb.append("distance=").append(item.distance).append("\n");
        sb.append("direction=").append(item.direction).append("\n");
//        Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("DaumMapLibrarySample");
        alertDialog.setMessage( sb.toString());
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchByLocation(item.longitude, item.latitude);
            }
        });
        alertDialog.show();
    }

    @Override
    @Deprecated
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }
}
