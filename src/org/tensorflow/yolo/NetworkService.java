package org.tensorflow.yolo;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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


    // book text
    @GET("/api/book/{pk}")
    Call<Book> get_book(@Path("pk") int pk);

    // quiz
    @GET("/api/quiz/{userId}") Call<Quiz> getData(@Path("userId") String id);
    @GET("/api/quiz") Call<List<Quiz>> get_qb (@Query("b_id") int b_id);

    // bookword
    //@GET("/api/bookword/{userId}") Call<Bookword> get_bw_data(@Path("userId") String id);
    @GET("api/bookword") Call<List<Bookword>> get_bw(@Query("b_id") int b_id);

    // camera
    @FormUrlEncoded
    @POST("/api/camera/")
    Call<Camera> register_camera(
            @Field("c_url") String c_url,
            @Field("c_word_e") String c_word_e,
            @Field("c_word_k") String c_word_k,
            @Field("uid") String uid);

    @DELETE("/api/camera/{pk}/")
    Call<ResponseBody> delete_camera(@Path("pk") int pk);

    @GET("/api/camera?")
    Call<List<Camera>> get_Cameras();

    // map
    // 서버에 데이터를 수정해달라고 요청하는 메서드
    // "http://memolease.ipdisk.co.kr:1337/minigrams/id/{like:true}" 이런식으로 보내질거다.
    // 그냥 String으로 보내면 서버에 put을 할수없어서 UTF-8로 바꿔서 보내야하는데 이를 레트로핏에서 FORMURLENCODED로 해준다.
    @FormUrlEncoded
    @PATCH("/api/user/{pk}/")
    Call<ResponseBody> updateProgress(@Path("pk") String uid, @Field("p_progress") int progress);


}
