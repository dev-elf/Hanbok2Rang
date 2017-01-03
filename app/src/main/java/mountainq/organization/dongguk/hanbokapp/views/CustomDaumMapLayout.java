package mountainq.organization.dongguk.hanbokapp.views;

import android.app.Activity;

import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapView;

/**
 * Created by dnay2 on 2017-01-04.
 */

public class CustomDaumMapLayout extends MapLayout {
    CustomDaumMapView mapView;

    public CustomDaumMapLayout(Activity activity) {
        super(activity);
        this.mapView = new CustomDaumMapView(activity);
    }

    @Override
    public MapView getMapView() {
        return this.mapView;
    }
}
