package org.tensorflow.yolo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.tensorflow.yolo.setting.AppSetting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

public class PlanetActivity1 extends AppCompatActivity {

    // 그레이 이미지를 조절하기 위한 Flag

    Handler handler;
    Runnable r;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet1);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);
        // 세영: 퀴즈 숫자
        AppSetting.quizcount=0;
        AppSetting.quizsen=0;
        ImageView detailProgBar=(ImageView)findViewById(R.id.detailProgBar);


        ImageView img = findViewById(R.id.imageView2);
        img.setOnClickListener(view -> {
            MainActivity.btnmp.start();
            Intent intent = new Intent(getApplicationContext(), BookActivity.class);
            intent.putExtra("b_id",1);
            startActivity(intent);
        });

        ImageView img2 = findViewById(R.id.imageView3);
        img2.setOnClickListener(view -> {
            MainActivity.btnmp.start();
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
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(),PlanetListActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // 지연 : DB에서 USER 진행률 가져오기
        AppSetting.progress=getProgressDB();
        // 지연 : 행성별 프로그레스 바



        switch (AppSetting.progress) {	// 조건 %5
            case 0: // 시작, 동화 단계가 노란글씨 + 첫번쨰 컨텐츠 깜빡임
                handler=new Handler();
                r=new Runnable(){//
                    @Override
                    public void run() {
                        if(AppSetting.dp_bool){
                            //img.setImageBitmap(null);
                            //img.setLayoutParams(new LinearLayout.LayoutParams(330, 200));
                            img.setImageResource(R.drawable.yellow_rocket);
                            AppSetting.dp_bool=false;
                        }else{
                            img.setImageResource(R.drawable.yellow_rocket_g);
                            AppSetting.dp_bool=true;
                        }
                        handler.postDelayed(r,500);
                    }
                };

                handler.post(r);
                detailProgBar.setImageResource(R.drawable.dprog1);
                break;
            case 1:// 문법 단계가 노란글씨 + 두번쨰 컨텐츠 깜빡임 + 첫번째 컨텐츠 /
                img.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });//

                img3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });//

                handler=new Handler();
                img.setImageResource(R.drawable.yellow_rocket);
                r=new Runnable(){
                    @Override
                    public void run() {
                        if(AppSetting.dp_bool){
                            //img.setImageBitmap(null);
                            //img.setLayoutParams(new LinearLayout.LayoutParams(330, 200));
                            img2.setImageResource(R.drawable.satellite);
                            AppSetting.dp_bool=false;
                        }else{
                            img2.setImageResource(R.drawable.satellite_g);
                            AppSetting.dp_bool=true;
                        }
                        handler.postDelayed(r,400);
        }
    };
                handler.post(r);
                detailProgBar.setImageResource(R.drawable.dprog2);

                break;
            case 2: // 발음단계
                img.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });//
                img2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });//


                handler=new Handler();
                img.setImageResource(R.drawable.yellow_rocket);
                img2.setImageResource(R.drawable.satellite);
                r=new Runnable(){
                    @Override
                    public void run() {
                        if(AppSetting.dp_bool){
                            img3.setImageResource(R.drawable.battery1_gray);
                            AppSetting.dp_bool=false;
                        }else{
                            img3.setImageResource(R.drawable.battery1);
                            AppSetting.dp_bool=true;
                        }
                        handler.postDelayed(r,400);
                    }
                };
                handler.post(r);
                detailProgBar.setImageResource(R.drawable.dprog3);
                break;
            case 3:

                img.setImageResource(R.drawable.yellow_rocket);
                img2.setImageResource(R.drawable.satellite);
                img3.setImageResource(R.drawable.battery1);

                detailProgBar.setImageResource(R.drawable.dprog4);
                break;
            default:
                break;
        }




    }

    private float pxToDp(int px) {
        float density=getApplicationContext().getResources().getDisplayMetrics().density;
        if(density==1.0){
            density*=4.0;
        }else if(density==1.5){
            density*=(8/3);
        }else if(density==2.0){
            density*=2.0;
        }
        return px/density;
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
