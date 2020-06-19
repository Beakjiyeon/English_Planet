package org.tensorflow.yolo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @FormUrlEncoded
    @POST("/api/user/")
    Call<User> register_user(@Field("uid") String uid,
                             @Field("upw") String upw,
                             @Field("nickname") String nickname,
                             @Field("p_progress") int p_progress);
    @FormUrlEncoded
    @POST("/api/camera/")
    Call<Camera> register_camera(
                             @Field("c_url") String c_url,
                             @Field("c_word_e") String c_word_e,
                             @Field("c_word_k") String c_word_k,
        @Field("uid") String uid);


    @PATCH("/api/user/{pk}/")
    Call<User> patch_user(@Path("pk") int pk, @Body User user);

    @DELETE("/api/user/{pk}/")
    Call<User> delete_user(@Path("pk") int pk);

    // all user
    @GET("/api/user/")
    Call<JSONObject> get_user();

    @GET("/api/user/{pk}")
    Call<User> get_pk_user(@Path("pk") String pk);

    //@GET("/api/camera")
    //Call<Camera> get_Camera(@Query("uid") String uid);


    @GET("/api/camera?")
    Call<List<Camera>> get_Cameras();

    // book text
    @GET("/api/book/{pk}")
    Call<Book> get_book(@Path("pk") int pk);



}

