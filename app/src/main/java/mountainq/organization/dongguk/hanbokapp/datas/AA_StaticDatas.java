package mountainq.organization.dongguk.hanbokapp.datas;

import android.content.Context;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class AA_StaticDatas {

    /**
     * 어플의 데이터를 전반적으로 관리한다
     */

    private static AA_StaticDatas instance;
    public static AA_StaticDatas getInstance(){
        return instance;
    }
    static {
        instance = new AA_StaticDatas();
    }

    /**
     * public static final 데이터 위치
     */
    //관광 API
    private static final String SERVICE_KEY = "kKxwGFgF3Nl%2FYmNPy7fLIoEbzMhhtrH1THy1IvcHm6yxia1I%2BhtyivenbGNWHGYanSNrtSXioCZpBdUlZkNW4Q%3D%3D";
    private static final String MOBILE_OS = "AND";
    private static final String CONTENT_TYPE_ID = "12";
    private static final String APP_NAME = "Hanbok";
    public static final String TOUR_API_LOCATION_BASED_LIST = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword?ServiceKey="+SERVICE_KEY+"&MobileOS="+MOBILE_OS+"&MobileApp="+APP_NAME+"&ContentTypeId="+CONTENT_TYPE_ID;


    public static final String DAUM_MAPS_ANDROID_APP_API_KEY = "5f2c3efefb03e28627cbf90863bfd053";
    /**
     * 데이터 필드 영역
     */
    private int width, height; // 디바이스의 화면 크기

    private int userId; // 유저정보 저장 지금은 안씀


    /**
     * 화면 크기 저장 메소드 한번만 쓰도록하자
     * @param context
     */
    public void setDisplayPixels(Context context){
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }



    /**
     * 데이터 입출력 메소드
     */


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
