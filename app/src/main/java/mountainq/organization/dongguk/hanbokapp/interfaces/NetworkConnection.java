package mountainq.organization.dongguk.hanbokapp.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by dnay2 on 2017-01-03.
 */

public interface NetworkConnection {

    /**
     * 회원가입 addUser
     * @param name 포스트형태로 회원정보를 보낸다.
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("/contents")
    Call<String> addUser(@Body String name);
}
