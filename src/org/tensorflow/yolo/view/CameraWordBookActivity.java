package org.tensorflow.yolo.view;

import android.app.Activity;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tensorflow.yolo.Camera;
import org.tensorflow.yolo.NetworkService;
import org.tensorflow.yolo.R;
import org.tensorflow.yolo.RetrofitSender;
import org.tensorflow.yolo.setting.AppSetting;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CameraWordBookActivity extends Activity {

    NetworkService networkService;
    TextView cword_text;
    TextView cword_text2;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_camerawordbook);

        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<List<Camera>> response = networkService.get_Cameras();
        response.enqueue(new Callback<List<Camera>>() {
            @Override
            public void onResponse(Call<List<Camera>> call, Response<List<Camera>> response) {

                //Camera b = (Camera)response.body();
                //String b_text = b.getC_word_e();

                if(response!=null) {
                    List<Camera> list=response.body();
                    int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                    for (int index = 0; index < totalElements; index++) {

                        if(list.get(index).getUid().equals(AppSetting.uid)) {
                            Log.d("########", list.get(index).getC_word_e() + "/" + list.get(index).getC_word_k());
                            FirebaseStorage fs = FirebaseStorage.getInstance();

                            //StorageReference imagesRef = fs.getReference().child("Images/");
                            ImageView profileImgView=findViewById(R.id.cimage);
                            Glide.with(getApplicationContext())
                                    .load(list.get(index).getC_url())
                                    .into(profileImgView);

                            cword_text=findViewById(R.id.cword_text);
                            cword_text.setText(list.get(index).getC_word_e());
                            cword_text2=findViewById(R.id.cword_text2);
                            cword_text2.setText(list.get(index).getC_word_k());
                        }
                    }
                }else{
                    Log.d("########", "xxxxxxx");
                }
                Log.d("########", "xxxㅇㅇxxxx");
            }

            @Override
            public void onFailure(Call<List<Camera>> call, Throwable t) {
                Log.d("########", t.getMessage());
            }

        });









    }
}