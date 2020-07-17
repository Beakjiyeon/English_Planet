package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.tensorflow.yolo.setting.AppSetting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ImageButton btn = (ImageButton)findViewById(R.id.btn);
        LinearLayout scorelayout = (LinearLayout)findViewById(R.id.scorelayout);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        // 발음 점수 받아오기
       Intent intent=new Intent(this.getIntent());
        Double s=intent.getDoubleExtra("myscore",0);
        int countword = intent.getIntExtra("countword",0);

        count++;

        // 점수에 따라 배경 설정
        if(s>=3) {
            if(countword>=2)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.excellent));
            else if(countword==1)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.good));
            else
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));
        }
        else if (s>=2) {
            if(countword>=2)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.good));
            else
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));
        }
        else
            scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));


        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SentenceActivity.class);
//                startActivity(intent);
                // AppSetting 값 수정 db에서 받아오는데 시간이 걸리기떄문...
                if(AppSetting.quizsen==2) {

                    // 쳅터 1의 시작=0, 동화=1
                    // db에 값 반영
                    updateProgressDB();

                    finish();
                    AppSetting.progress=3;
                    Log.d("널체크","발음점수엔 "+AppSetting.uid);
                    Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                    startActivity(intent);
                }
                else{

                    AppSetting.quizsen++;
                    Intent intent = new Intent(getApplicationContext(), SentenceActivity.class);
                    startActivity(intent);
                }
            }
        });



    }
    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid,3);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("ㅋㅋㅋ4#", "수정하고싶다");

                }else{
                    Log.d("ㅋㅋㅋ#", "ㄴㄴㄴxxxxxxx");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ㅋㅋㅋ#", t.getMessage());
            }
        });
    }
}
