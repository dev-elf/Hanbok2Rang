package mountainq.organization.dongguk.hanbokapp.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;

/**
 * Created by dnay2 on 2017-01-03.
 *
 * 이거 하나 만들어서 계속 쓸거임
 */

public class CustomDaumMapView extends MapView {

    public double userLon, userLat;
    public double centerLon, centerLat;

    public CustomDaumMapView(Activity activity) {
        super(activity);
        setDaumMapApiKey(AA_StaticDatas.DAUM_MAPS_ANDROID_APP_API_KEY);
        setMapViewEventListener(listener);
        setOpenAPIKeyAuthenticationResultListener(new OpenAPIKeyAuthenticationResultListener() {
            @Override
            public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

            }
        });
        setCurrentLocationEventListener(currentListener);
        setMapType(MapType.Standard);
        setPOIItemEventListener(POIitemListener);


    }

    public CustomDaumMapView(Context context) {
        super(context);
    }

    public CustomDaumMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDaumMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    CurrentLocationEventListener currentListener = new CurrentLocationEventListener() {
        @Override
        public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

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
    };
    /**
     * 리스너를 미리 만들어 사용하자
     */
    net.daum.mf.map.api.MapView.MapViewEventListener listener = new net.daum.mf.map.api.MapView.MapViewEventListener() {
        @Override
        public void onMapViewInitialized(net.daum.mf.map.api.MapView mapView) {

        }

        @Override
        public void onMapViewCenterPointMoved(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewZoomLevelChanged(net.daum.mf.map.api.MapView mapView, int i) {

        }

        @Override
        public void onMapViewSingleTapped(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDoubleTapped(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewLongPressed(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragStarted(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragEnded(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewMoveFinished(net.daum.mf.map.api.MapView mapView, MapPoint mapPoint) {

        }
    };

    /**
     * 검색에 관련된 메소드
     */
    POIItemEventListener POIitemListener = new POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };

}
