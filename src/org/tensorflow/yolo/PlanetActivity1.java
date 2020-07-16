package org.tensorflow.yolo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import org.tensorflow.yolo.setting.AppSetting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

public class PlanetActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet1);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        ImageView detailProgBar=(ImageView)findViewById(R.id.detailProgBar);

        // 지연 : DB에서 USER 진행률 가져오기
        AppSetting.progress=getProgressDB();
        // 지연 : 행성별 프로그레스 바
        switch (AppSetting.progress) {	// 조건 %5
            case 0:
                detailProgBar.setImageResource(R.drawable.dprog0);
                break;
            case 1:
                detailProgBar.setImageResource(R.drawable.dprog1);
                break;
            case 2:
                detailProgBar.setImageResource(R.drawable.dprog2);
                break;
            case 3:
                detailProgBar.setImageResource(R.drawable.dprog4);
                break;
            default:
                break;
        }



        ImageView img = findViewById(R.id.imageView2);
        img.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BookActivity.class);
            startActivity(intent);
        });

        ImageView img2 = findViewById(R.id.imageView3);
        img2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WordquizActivity.class);
            startActivity(intent);
            //finish();
        });

        ImageView img3 = findViewById(R.id.imageView4);
        img3.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),SentenceActivity.class);
            startActivity(intent);
            //finish();
        });

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PlanetListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    int getProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        Call<User> response2 = networkService.get_pk_user(AppSetting.uid);
        response2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response!=null) {
                    AppSetting.progress=response.body().getP_progress();
                    Log.d("ㅋㅋㅋ2#", ""+AppSetting.progress);
                }else{
                    Log.d("ㅋㅋㅋ2#", "ㄴㄴㄴxxxxxxx");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ㅋㅋㅋ2#", t.getMessage());
            }
        });

        return AppSetting.progress;
    }
}
