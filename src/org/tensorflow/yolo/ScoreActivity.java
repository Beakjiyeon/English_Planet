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

        int mB_id = intent.getIntExtra("b_id", 0);
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

                    // 현재 p_progress값과 현재 b_id가 같고, 동화미션을 수행했을경우(즉 아직 수행전인 상태로 db에 남아있는 상태의 경우)에 db반영시킴
                    if((AppSetting.progress/10==mB_id)&&(AppSetting.progress%10==2)){
                        AppSetting.progress = (mB_id+1)*10;
                        updateProgressDB();
                        // 행성 하나에 있는 모든 미션을 완료했으므로 big_progress db 수정
                        updateBigProgressDB();

                    }else{
                        // db 반영이 필요 없는 경우

                    }



                    finish();

                    Log.d("널체크","발음점수엔 "+AppSetting.uid);
                    Intent intent=null;
                    if(mB_id==1) {
                        intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                    }else if(mB_id==2) {
                        intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                    }else if(mB_id==3) {
                        intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                    }


                    startActivity(intent);
                }
                else{

                    AppSetting.quizsen++;
                    Intent intent = new Intent(getApplicationContext(), SentenceActivity.class);
                    intent.putExtra("b_id",  mB_id);
                    startActivity(intent);
                }
            }
        });



    }
    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        //int progress=(AppSetting.big_progress+2)*10+0;
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid,AppSetting.progress);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("#######db수정###########3", "수정");

                }else{
                    Log.d("#######db수정###########3", "실패");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("#######db수정############", t.getMessage());
            }
        });
    }
    // 지연: DB 진행률을 수정하는 함수
    void updateBigProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        int calcBig=(AppSetting.progress-1)/10;
        AppSetting.big_progress=calcBig;
        Call<ResponseBody> response2 = networkService.updateBigProgress(AppSetting.uid,calcBig);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("#######db수정###########5", "수정");

                }else{
                    Log.d("#######db수정###########3", "실패");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("#######db수정############", t.getMessage());
            }
        });
    }
}
