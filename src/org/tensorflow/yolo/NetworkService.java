package org.tensorflow.yolo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NetworkService {

    @FormUrlEncoded
    @POST("/api/user/")
    Call<User> register_user(@Field("uid") String uid,
                             @Field("upw") String upw,
                             @Field("nickname") String nickname,
                             @Field("p_progress") int p_progress);


    @PATCH("/api/user/{pk}/")
    Call<User> patch_user(@Path("pk") int pk, @Body User user);

    @DELETE("/api/user/{pk}/")
    Call<User> delete_user(@Path("pk") int pk);

    // all user
    @GET("/api/user/")
    Call<JSONObject> get_user();

    @GET("/api/user/{pk}")
    Call<User> get_pk_user(@Path("pk") String pk);


}
