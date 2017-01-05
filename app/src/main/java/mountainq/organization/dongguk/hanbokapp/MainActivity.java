package mountainq.organization.dongguk.hanbokapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
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
import java.util.Iterator;
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
 * 이곳은 메인 화면인데 보여줄게 없습니다ㅜㅜ
 * 지도만 보여줍시다
 */
public class MainActivity extends NavigationDrawerActivity implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, MapView.POIItemEventListener {
    /**
     * 종원 커스텀
     */
    private double rand = Math.random();
    private static final String LOG_TAG = "MainActivity";
    private MapView mapView;
    private MapLayout mapLayout;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<>();
    private boolean isUsingCustomLocationMarker = false;

    private static final String KEYWORD = "keyword";
    private static final String LOCATION = "location";
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();

    private static final String DB_INITIAL = "initial";
    private static final String DB_INSERT = "insert";
    private static final String DB_DELETE = "delete";
    public MapPoint t_map, c_map;
    public AlertDialog dialog;
    private static final String DB_FAIL = "fail";
    private static final String DB_SUCC = "success";
    private int intval = -1;
    private int intval2 = 100;
    private int h_status = 1;
    /**
     * 맵뷰를 가져오기
     */
    RelativeLayout container;


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
    private ArrayList<LocationItem> locationItems = new ArrayList<>();
    LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        mContext = this;
        container = (RelativeLayout) findViewById(R.id.container);
        searchListView = (ListView) findViewById(R.id.searchListView);
        searchEditText = (EditText) findViewById(R.id.searchet);
        searchLayout = (LinearLayout) findViewById(R.id.searchLL);

        bookMarkListView = (ListView) findViewById(R.id.bookMarkListView);
        bookMarkListView.setOnItemClickListener(bookMarkClickListener);
        bookMarkListView.setOnItemLongClickListener(bookMarkLongClickListener);
        getBookMarkList();
        if (mapLayout == null) mapLayout = new MapLayout(this);
        if (mapView == null) mapView = mapLayout.getMapView();
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


    private AdapterView.OnItemClickListener bookMarkClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mapView.removeAllPOIItems();
            addPin(bookMarks.get(position));
            MapPOIItem[] poiItems = mapView.getPOIItems();
            if (poiItems.length > 0) {
                mapView.selectPOIItem(poiItems[0], false);
            }
            closeDrawer();
        }
    };

    private AdapterView.OnItemLongClickListener bookMarkLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final BookMark item = bookMarks.get(position);
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(item.getTitle() + "를 즐겨찾기에서 지우시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteBookMark(item);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
            return false;
        }
    };

    public void onMenuClickListener(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.g1_menu1:
                intent = new Intent(MainActivity.this, A1_CompanyInfo.class);
                break;
            case R.id.g1_menu2:
                Uri uri = Uri.parse("mailto:chriss0313@naver.com");
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                break;
            case R.id.g1_menu3:
                intent = new Intent(MainActivity.this, A2_AppInformation.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    public void onFloatingButtonClickListener(View v) {
        ImageView imgview = (ImageView) findViewById(R.id.mapFunctionMyLocation);
        switch (v.getId()) {
            case R.id.mapFunctionMyLocation:
                if (mapView.getCurrentLocationTrackingMode().toString().equals("TrackingModeOff")) {
                    imgview.setImageResource(R.drawable.my_location_1);
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                } else {
                    imgview.setImageResource(R.drawable.my_location_2);
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    mapView.setShowCurrentLocationMarker(false);
                }
                break;
            case R.id.mapFunctionNear:
                findHanbok(250);
                break;
            case R.id.mapFunctionSetting:
                if (mapView.getMapType().toString().equals("Standard")) {
                    mapView.setMapType(MapView.MapType.Hybrid);
                } else {
                    mapView.setMapType(MapView.MapType.Standard);
                }
                break;
            case R.id.mapFunctionZoomIn:
                mapView.zoomIn(true);
                break;
            case R.id.mapFunctionZoomOut:
                mapView.zoomOut(true);
                break;
            case R.id.searchbtn:
                searchByKeyword(searchEditText.getText().toString());
                searchEditText.setText("");
                break;
        }
    }

    private void insertBookMark(BookMark item) {
        new DBTask().execute(DB_INSERT, String.valueOf(item.getPrimeKey()), item.getTitle(), String.valueOf(item.getLongitude()), String.valueOf(item.getLatitude()), item.getAddress(), item.getPhone());
    }

    private void deleteBookMark(BookMark item) {
        deleteBookMark(item.getPrimeKey());
    }

    private void deleteBookMark(int primeKey) {
        new DBTask().execute(DB_DELETE, String.valueOf(primeKey));
    }

    private void getBookMarkList() {
        new DBTask().execute(DB_INITIAL);

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    class DBTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            DataBaseManager dbm = new DataBaseManager(mContext);
            switch (params[0]) {
                case DB_INITIAL:
                    bookMarks = dbm.printList();
                    return DB_INITIAL;
                case DB_INSERT:
                    if (dbm.insert(params[1], params[2], params[3], params[4], params[5], params[6])) {
                        bookMarks = dbm.printList();
                        return DB_INSERT + DB_SUCC;
                    } else return DB_INSERT + DB_FAIL;
                case DB_DELETE:
                    dbm.delete(params[1]);
                    bookMarks = dbm.printList();
                    return DB_DELETE;
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (bookMarkAdapter != null) bookMarkAdapter.notifyDataSetChanged();
            bookMarkAdapter = new BookMarkAdapter(mContext, bookMarks);
            bookMarkListView.setAdapter(bookMarkAdapter);
            switch (s) {
                case DB_INITIAL:
                    break;
                case DB_INSERT + DB_FAIL:
                    Toast.makeText(MainActivity.this, "이미 추가되어 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case DB_INSERT + DB_SUCC:
                    Toast.makeText(MainActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case DB_DELETE:
                    Toast.makeText(MainActivity.this, "즐겨찾기에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    private void searchByKeyword(String keyword) {
        new SearchTask().execute(KEYWORD, keyword);
    }

    private void searchByLocation(double lon, double lat) {
        new SearchTask().execute(LOCATION, String.valueOf(lon), String.valueOf(lat));
    }


    /**
     * 데이터 통신 관광 API로부터 관광지 정보를 받아와 리스트로 출력한다.
     */
    class SearchTask extends AsyncTask<String, Integer, String> implements AdapterView.OnItemClickListener {

        private URL url = null;
        HttpURLConnection connection = null;

        @Override
        protected String doInBackground(String... params) {
            String keyword = "", lon = "", lat = "";

            if (params[0].equals(KEYWORD)) {
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
                if (params[0].equals(KEYWORD)) {
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
                        locationItems = parser.parse(connection.getInputStream());
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
            if (params[0].equals(KEYWORD)) return KEYWORD;
            else return LOCATION;
        }

        @Override
        protected void onPostExecute(String param) {
            locationAdapter = new LocationAdapter(locationItems, mContext);
            Log.d("Test", "parsed item : " + locationItems.toString());
            if (param.equals(KEYWORD)) {
                searchListView.setAdapter(locationAdapter);
                searchListView.setOnItemClickListener(searchListener);
                showLIst();
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                //알림창에서 확인후 추천
                dialog = new AlertDialog.Builder(MainActivity.this)
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

        private void showLocationBasedList(LocationAdapter locationAdapter) {
            View tempView = View.inflate(MainActivity.this, R.layout.zz_dialogue, null);
            ListView newListView = (ListView) tempView.findViewById(R.id.searchListView);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(mData.getWidth() * 4 / 5, mData.getWidth() * 4 / 5);
            newListView.setLayoutParams(llp);
            newListView.setAdapter(locationAdapter);
            //custom
            newListView.setOnItemClickListener(this);
            dialog = new AlertDialog.Builder(MainActivity.this)
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

        private AdapterView.OnItemClickListener searchListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addPin(locationItems.get(position));
                MapPOIItem[] poiItems = mapView.getPOIItems();
                if (poiItems.length > 0) {
                    mapView.selectPOIItem(poiItems[0], false);
                }
                hideList();
            }
        };

        @Override
        //아이템 클릭
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            addPin(locationItems.get(position));
            addPolyLine(locationItems.get(position).getLatitude(), locationItems.get(position).getLongitude());
            dialog.dismiss();
        }
    }


    /*
        2017-01-05  업데이트 부분
        핀꽂기, 폴리라인 그리기 메소드를 각각 만들어서
        코드를 알아보기 쉽고 간결하게 정리하면 좋겠다
     */

    /**
     * 핀 꽂기 관광지
     *
     * @param item 관광지 객체
     */
    private void addPin(LocationItem item) {
        Item search_item = new Item();
        search_item.setTitle(item.getTitle());
        search_item.address = item.getAddress();
        search_item.phone = item.getPhoneNumber();
        search_item.imageUrl = item.getFirstImgUrl();
        search_item.setLatitude(item.getLatitude());
        search_item.setLongitude(item.getLongitude());
        if (search_item.category == null) search_item.category = "관광";
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");

        marker.setTag(intval--);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude()));
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setCustomImageResourceId(R.drawable.picker_blue); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomSelectedImageResourceId(R.drawable.picker_green);
        marker.setCustomImageAutoscale(false);
        mapView.addPOIItem(marker);
        mTagItemMap.put(marker.getTag(), search_item);
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude())));
        //Toast.makeText(getApplicationContext(), marker.getTag() + "이동하였습니다.", Toast.LENGTH_SHORT).show();
    }

    /**
     * 핀 꽂기 북마크
     *
     * @param item 북마크 객체
     */
    private void addPin(BookMark item) {
        Item search_item = new Item();
        search_item.setTitle(item.getTitle());
        search_item.address = item.getAddress();
        search_item.phone = item.getPhone();
        search_item.setLatitude(item.getLatitude());
        search_item.setLongitude(item.getLongitude());
        if (search_item.category == null) search_item.category = "북마크";
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(intval2++);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude()));
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.picker_yellow);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomSelectedImageResourceId(R.drawable.picker_pink);
        marker.setCustomImageAutoscale(false);
        mapView.addPOIItem(marker);
        mTagItemMap.put(marker.getTag(), search_item);
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude())));
    }

    /**
     * 폴리라인 그리기 검색지-관광지
     *
     * @param lat 위도
     * @param lon 경도
     */

    private void addPolyLine(double lat, double lon) {
        mapView.removeAllPolylines();
        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000 + (int) (rand * 100) + 1);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
        polyline.addPoint(c_map);
        mapView.addPolyline(polyline);
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 200; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    /**
     * 폴리라인 그리기 북마크-관광지
     *
     * @param startItem 시작점
     * @param endItem   끝점
     */
    private void addPolyLine(BookMark startItem, LocationItem endItem) {

    }

    /**
     * 폴리라인 그리기 관광지-북마크
     *
     * @param startItem 시작점
     * @param endItem   끝점
     */
    private void addPolyLine(LocationItem startItem, BookMark endItem) {

    }

    /**
     * 폴리라인 그리기 북마크-북마크
     *
     * @param startItem 시작점
     * @param endItem   끝점
     */
    private void addPolyLine(BookMark startItem, BookMark endItem) {

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

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();


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

        //말풍선
        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            Item item = mTagItemMap.get(poiItem.getTag());
            if (item == null) return null;
            DataBaseManager dbm = new DataBaseManager(mContext);
            bookMarks = dbm.printList();
            Iterator<BookMark> iterator = bookMarks.iterator();
            String name = null;
            ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            while (iterator.hasNext()) {
                BookMark b = iterator.next();
                name = b.getTitle();
                if (name.equals(item.getTitle())) {
                    Log.i(LOG_TAG + "즐겨찾기 맞음", name + ":" + item.getTitle());
                    imageViewBadge.setImageResource(R.drawable.star_on);
                    break;
                } else {
                    Log.i(LOG_TAG + "즐겨찾기 아님", name + ":" + item.getTitle());
                    imageViewBadge.setImageResource(R.drawable.star_off);
                }
            }

            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(item.getTitle());
            TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            textViewDesc.setText(item.address);
            if (item.category.equals("관광")) {
                imageViewBadge.setImageDrawable(createDrawableFromUrl(item.imageUrl));
            }
            t_map = poiItem.getMapPoint();

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
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picker_yellow);
//            bitmap.setHeight(mData.getWidth()/10);
//            bitmap.setWidth(mData.getWidth()/10);
            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.getTitle());
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.getLatitude(), item.getLongitude());
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.picker_yellow);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.picker_pink);
            // poiItem.setCustomSelectedImageBitmap(bitmap);
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
        // Toast.makeText(this, "api는" + item.category + "tag는" + mapPOIItem.getTag() + "이름은" + item.getLatitude(), Toast.LENGTH_SHORT).show();
        StringBuilder sb = new StringBuilder();
        sb.append(item.getTitle()).append("\n");
        sb.append(item.address).append("\n");
        sb.append(item.phone).append("\n");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setTitle("한복이랑");
        alertDialog.setMessage(sb.toString());
        if (item.category.equals("관광")) {
            alertDialog.setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent call_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone));
                    startActivity(call_intent);
                }
            });
            alertDialog.setNegativeButton("닫기", null);
        } else {
            alertDialog.setPositiveButton("관광지추천받기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!item.category.equals("관광")) c_map = t_map;
                    //Toast.makeText(MainActivity.this, item.getLongitude() + "" + item.getLatitude() + item.getTitle(), Toast.LENGTH_SHORT).show();
                    searchByLocation(item.getLongitude(), item.getLatitude());

                }
            });
            DataBaseManager dbm = new DataBaseManager(mContext);
            bookMarks = dbm.printList();
            Iterator<BookMark> iterator = bookMarks.iterator();
            String name = null;
            boolean bm = false;
            while (iterator.hasNext()) {
                BookMark b = iterator.next();
                name = b.getTitle();
                if (name.equals(item.getTitle())) {
                    bm = true;
                    Log.i(LOG_TAG + "즐찾 없애야함", name + ":" + item.getTitle() + bm);
                    break;
                } else {
                    bm = false;
                    Log.i(LOG_TAG + "즐찾 업애면안됨", name + ":" + item.getTitle());
                }
            }
            Log.i(LOG_TAG + "마지막 bm", name + ":" + item.getTitle() + "bm:" + bm);
            if (bm == false) {
                alertDialog.setNegativeButton("즐겨찾기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        View mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
//                        ImageView imageViewBadge = (ImageView)mCalloutBalloon.findViewById(R.id.badge);
//                        imageViewBadge.setImageResource(R.drawable.star_on);

                        BookMark bookMark = new BookMark(item.id, item.getTitle(), Double.toString(item.getLongitude()), Double.toString(item.getLatitude()), item.address, item.phone);
                        insertBookMark(bookMark);
                        findHanbok(250);
                    }
                });
            }
            alertDialog.setNeutralButton("전화 걸기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent call_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone));
                    startActivity(call_intent);
                }
            });
        }
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

    private void showLIst() {
        searchListView.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        visibleBackBtn(true);
    }

    private void hideList() {
        searchListView.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
        visibleBackBtn(false);
    }

    @Override
    public void onBackPressed() {
        mapView.removeAllPolylines();
        mapView.removeAllCircles();
        mapView.removeAllPOIItems();
        if (searchListView.getVisibility() == View.VISIBLE) {
            hideList();
        } else super.onBackPressed();
    }


    /**
     * 드로워가 열려있으면 true, 닫혀있으면 false
     *
     * @param activated
     */
    @Override
    protected void searchEditTextActivated(Boolean activated) {
        super.searchEditTextActivated(activated);
        if (activated) {
            searchLayout.setVisibility(View.INVISIBLE);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openDrawer() {
        super.openDrawer();

    }

    @Override
    public void closeDrawer() {
        super.closeDrawer();

    }

    public void findHanbok(int rad) {
        final int aa = rad;
        ImageView imgview = (ImageView) findViewById(R.id.mapFunctionMyLocation);
        imgview.setImageResource(R.drawable.my_location_2);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
        mapView.removeAllCircles();
        String query = "한복대여점";
        MapPoint.GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도
        int radius = rad; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
        int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
        String apikey = DAUM_MAPS_ANDROID_APP_API_KEY;

        Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
        searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                mapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                mapView.removeAllPolylines();
                showResult(itemList); // 검색 결과 보여줌
                MapPOIItem[] poiItems = mapView.getPOIItems();
                if (poiItems.length < 1) {
                    h_status = 0;
                    if (aa == 500) h_status = 1;
                } else h_status = 1;
            }

            @Override
            public void onFail() {
                showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
            }
        });


        MapCircle circle1 = new MapCircle(
                MapPoint.mapPointWithGeoCoord(latitude, longitude), // center
                rad + 100, // radius
                Color.argb(77, 255, 165, 0), // strokeColor
                Color.argb(77, 255, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mapView.addCircle(circle1);


    }
}
