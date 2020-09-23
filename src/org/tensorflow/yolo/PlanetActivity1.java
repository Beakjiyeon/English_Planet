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
import android.widget.Toast;

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

        //지연+++++++++++++++++++++++++++++++++++++++++++++
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
            intent.putExtra("b_id",1);
            startActivity(intent);
            //finish();
        });

        ImageView img3 = findViewById(R.id.imageView4);
        img3.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),SentenceActivity.class);
            intent.putExtra("b_id",1);
            startActivity(intent);
            //finish();
        });

        // 사진미션 추가
        ImageView img4 = findViewById(R.id.imageView5);
        img4.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),CameraQuizActivity.class);
            intent.putExtra("b_id",1);
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


        // 지연 : DB에서 사용자의 진행률 p_progress를 받아와 AppSetting에 등록한다.
        AppSetting.progress=getProgressDB();
        // 지연 : p_progress 에 따른 map 이미지 & 깜빡임 & 클릭 시 동작
        // 지연 : map 클릭 가능여부는 추후개선


        if(AppSetting.progress==10) {
            // 지연 : 처음 행성에 들어온 상태
            // map 이미지 = _시작_ *동화* 퀴즈 발음 기타 끝
            // 미션 깜밖임 = 동화 미션 이미지 색깔 gray->color->gray
            handler = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    if (AppSetting.dp_bool) {
                        img.setImageResource(R.drawable.yellow_rocket);
                        AppSetting.dp_bool = false;
                    } else {
                        img.setImageResource(R.drawable.yellow_rocket_g);
                        AppSetting.dp_bool = true;
                    }
                    handler.postDelayed(r, 200);
                }
            };
            handler.post(r);
            // 퀴즈 미션 클릭시 : 클릭 불가+퀴즈 미션 먼저 수행하세요
            img2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "동화 미션 먼저 수행하세요", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            // 발음 미션 클릭시 : 클릭 불가+퀴즈 미션 먼저 수행하세요!
            img3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "동화 미션 먼저 수행하세요", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            // map 이미지
            detailProgBar.setImageResource(R.drawable.dprog1);
        }else if(AppSetting.progress==11) {

            // 지연 : 동화 미션을 수행한 상태
            // map 이미지 : _시작_ _동화_ *퀴즈* 발음 기타 끝
            // 미션 깜빡임 : 퀴즈 미션
            // 동화 미션 클릭시 : BookActivity로 이동
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    MainActivity.btnmp.start();
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.putExtra("b_id", 1);
                    startActivity(intent);
                    return true;
                }
            });
            // 발음 미션 클릭시 : 클릭 불가+퀴즈 미션 먼저 수행하세요!
            img3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(getApplicationContext(), "퀴즈 미션 먼저 수행하세요", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            // 미션 깜빡임 설정
            handler = new Handler();
            img.setImageResource(R.drawable.yellow_rocket);
            r = new Runnable() {
                @Override
                public void run() {
                    if (AppSetting.dp_bool) {
                        img2.setImageResource(R.drawable.satellite);
                        AppSetting.dp_bool = false;
                    } else {
                        img2.setImageResource(R.drawable.satellite_g);
                        AppSetting.dp_bool = true;
                    }
                    handler.postDelayed(r, 200);
                }
            };
            handler.post(r);
            // map 이미지 수정
            detailProgBar.setImageResource(R.drawable.dprog2);
        }else if(AppSetting.progress==12) {

            // 퀴즈 수행을 마친 단계
            // 동화 미션 클릭시 : BookActivity로 이동
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    MainActivity.btnmp.start();
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.putExtra("b_id", 1);
                    startActivity(intent);
                    return true;
                }
            });
            // 퀴즈 미션 클릭시 : 퀴즈 액티비티로 이동
            img2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    MainActivity.btnmp.start();
                    Intent intent = new Intent(getApplicationContext(), WordquizActivity.class);
                    intent.putExtra("b_id", 1);
                    startActivity(intent);
                    return true;
                }
            });
            handler = new Handler();
            img.setImageResource(R.drawable.yellow_rocket);
            img2.setImageResource(R.drawable.satellite);
            r = new Runnable() {
                @Override
                public void run() {
                    if (AppSetting.dp_bool) {
                        img3.setImageResource(R.drawable.battery1_gray);
                        AppSetting.dp_bool = false;
                    } else {
                        img3.setImageResource(R.drawable.battery1);
                        AppSetting.dp_bool = true;
                    }
                    handler.postDelayed(r, 200);
                }
            };
            handler.post(r);
            detailProgBar.setImageResource(R.drawable.dprog3);
        }else if(AppSetting.progress>=20) {

            // 발음 미션을 완료한 단계
            // 모든 이미지 터치 가능
            // 미션 이미지 모두 color
            img.setImageResource(R.drawable.yellow_rocket);
            img2.setImageResource(R.drawable.satellite);
            img3.setImageResource(R.drawable.battery1);

            detailProgBar.setImageResource(R.drawable.dprog4);
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

    // 지연 : db로 부터 p_progress를 받아오는 함수
    int getProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        Call<User> response2 = networkService.get_pk_user(AppSetting.uid);
        response2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response!=null) {
                    AppSetting.progress=response.body().getP_progress();
                    Log.d("#########앱세팅#########", ""+AppSetting.progress);
                }else{
                    Log.d("#########앱세팅#########", "fail");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("#########앱세팅#########", "실패"+t.getMessage());
            }
        });

        return AppSetting.progress;
    }
}
