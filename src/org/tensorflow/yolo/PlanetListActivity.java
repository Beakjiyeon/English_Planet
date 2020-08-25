package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.yolo.setting.AppSetting;
import org.tensorflow.yolo.view.ClassifierActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//
// 주희 : 행성리스트 액티비티
public class PlanetListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planets);
        String msg = AppSetting.unickname+"님 환영합니다.";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        // bigpro값 받아오기
        AppSetting.big_progress=getBig();
        // 지연 : 프로그레스 바
        ImageView progBar=(ImageView)findViewById(R.id.progBar);
        //ImageView progBar_in=(ImageView)findViewById(R.id.progress_in);
        if(AppSetting.big_progress==0){
            // 하나도 학습x
        }else if(AppSetting.big_progress==1) {
            // 행성 1개 클리어
            progBar.setBackgroundResource(R.drawable.bigpro1);
        }else if(AppSetting.big_progress==2) {
            // 행성 2개 클리어
            progBar.setBackgroundResource(R.drawable.bigpro2);
        }else if(AppSetting.big_progress==3) {
            // 행성 3개 클리어
            progBar.setBackgroundResource(R.drawable.bigpro3);
        }


        ImageView planet1 = (ImageView) findViewById(R.id.planet1);
        planet1.setImageResource(R.drawable.planet1);

        planet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                startActivity(intent);
            }
        });

        ImageView planet2 = (ImageView) findViewById(R.id.planet2);
        planet2.setImageResource(R.drawable.planet2);

        planet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                startActivity(intent);
            }
        });

        ImageView planet3 = (ImageView) findViewById(R.id.planet3);
        planet3.setImageResource(R.drawable.planet3);

        planet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                startActivity(intent);
            }
        });

        ImageView dev = (ImageView) findViewById(R.id.dev);
        dev.setImageResource(R.drawable.planet_devs);

        ImageView btnCamera = (ImageView) findViewById(R.id.BtnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnmp.start();
                Toast.makeText(getApplicationContext(),"영어 뜻을 알고싶은 물건을 찍어보세요",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        ImageView btnMypage = (ImageView) findViewById(R.id.BtnMypage);
        btnMypage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });


    }
    int getBig(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        Call<User> response2 = networkService.get_pk_user(AppSetting.uid);
        response2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response!=null) {
                    AppSetting.big_progress=response.body().getBig_progress();
                    Log.d("#########앱세팅#########", ""+AppSetting.big_progress);
                }else{
                    Log.d("#########앱세팅#########", "fail");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("#########앱세팅#########", "실패"+t.getMessage());
            }
        });

        return AppSetting.big_progress;

    }



}
