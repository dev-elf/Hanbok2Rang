package mountainq.organization.dongguk.hanbokapp;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mountainq.organization.dongguk.hanbokapp.datas.AA_StaticDatas;
import mountainq.organization.dongguk.hanbokapp.interfaces.NetworkConnection;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dnay2 on 2017-01-03.
 */

public class AppController extends Application {

    /**
     * 싱글톤의 형식으로 어플리케이션의 레트로핏을 관리함
     */
    private static AppController instance;
    public static AppController getInstance(){
        return instance;
    }
    static {
        instance = new AppController();
    }

    private NetworkConnection networkConnection;

    public NetworkConnection getNetworkConnection() {
        return networkConnection;
    }


    /**
     * 데이터 필드
     */
    private AA_StaticDatas mData = AA_StaticDatas.getInstance();
    private static final String baseUrl = "http://SERVER.URL";
    private String pushToken;   //Token for push;
    private String pushId;     //deviceId;

    /**
     * 데이터 입출력 메소드
     */
    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    /**
     * 메인 메소드
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ggg", "application controller creted");
        mData.setDisplayPixels(getApplicationContext());
        buildService();
    }

    private void buildService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        GsonConverterFactory factory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build();

        networkConnection = retrofit.create(NetworkConnection.class);
    }

}
