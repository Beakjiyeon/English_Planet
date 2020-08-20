package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordanswerActivity extends AppCompatActivity {
    RelativeLayout answerlayout;
    TextView tvans;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordanswer);
        answerlayout = (RelativeLayout)findViewById(R.id.answerlayout);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        tvans = (TextView)findViewById(R.id.tvans);
        btn = (ImageButton)findViewById(R.id.btn);
        Intent intent = getIntent();
        int correct = intent.getExtras().getInt("correct");
        String type = intent.getExtras().getString("type");
        int mB_id = intent.getIntExtra("b_id", 0);
        if(type.equals("word")) {
            if (correct == 1) {
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));
            } else {
                String answer = intent.getExtras().getString("answer");
                String answer2 = intent.getExtras().getString("answer2");
                tvans.setText(answer2 + "의 뜻은 " + answer);
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordwrong));
            }
        }
        else if(type.equals("sentence")){
            if (correct == 1) {
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));

            } else {
                String sen = intent.getExtras().getString("sen");
                tvans.setText(sen);
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordwrong));

            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                AppSetting.quizcount++;
                Log.v("quizCount=======",AppSetting.quizcount+"");
                if(AppSetting.quizcount<3) {
                    Log.v("quizCount=======",AppSetting.quizcount+"");
                    intent = new Intent(getApplicationContext(), WordquizActivity.class);
                }
                else if(AppSetting.quizcount==3){
                    Log.v("Sentence Quiz=======","OK");
                    intent=new Intent(getApplicationContext(), SenquizActivity.class);
                }
                else if(AppSetting.quizcount<5){
                    AppSetting.quizsen++;
                    intent=new Intent(getApplicationContext(), SenquizActivity.class);
                }
                else{
                    intent=new Intent(getApplicationContext(), PlanetActivity1.class);
                }
                AppSetting.progress=2;
                // 쳅터 1의 시작=0, 동화=1
                // db에 값 반영
                updateProgressDB();

                Log.d("널체크","문법점수엔 "+AppSetting.uid);
                finish();
                AppSetting.dp_bool=true;
                intent.putExtra("b_id",  mB_id);
                startActivity(intent);
            }
        });
    }
    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB(){
        NetworkService networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid,2);
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
