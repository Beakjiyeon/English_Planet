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
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MywordAnswerActivity extends AppCompatActivity implements Serializable {
    RelativeLayout answerlayout;
    TextView tvans;
    ImageButton btn;
    NetworkService networkService;
    List<Myword> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myword_answer);

        answerlayout = (RelativeLayout) findViewById(R.id.answerlayout);
        networkService = RetrofitSender.getNetworkService();

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        tvans = (TextView) findViewById(R.id.tvans);
        btn = (ImageButton) findViewById(R.id.btn);
        Intent intent = getIntent();
        int correct = intent.getExtras().getInt("correct");
        int m_id = intent.getExtras().getInt("m_id");
        Log.d("흠흠흠m_id",m_id+"");
        String type = intent.getExtras().getString("type");
        list2 = (List<Myword>)intent.getSerializableExtra("list2");

        if (type.equals("word")) {
            if (correct == 1) {
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));
                deleteMword(m_id);

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
                deleteMword(m_id);

            } else {
                String sen = intent.getExtras().getString("sen");
                String senk = intent.getExtras().getString("senk");
                tvans.setText(sen);
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wordwrong));
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                AppSetting.myquizcount++;
                if(AppSetting.myquizcount==list2.size()){
                    intent = new Intent(getApplicationContext(), MywordActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(type.equals("word")){
                    intent = new Intent(getApplicationContext(), MywordQuizActivity.class);
                    intent.putExtra("list2", (Serializable) list2);
                    startActivity(intent);
                    finish();
                }
                else if(type.equals("sentence")){
                    intent = new Intent(getApplicationContext(), MysenQuizActivity.class);
                    intent.putExtra("list2", (Serializable) list2);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // 오답노트 단어 삭제하는 함수
    private void deleteMword(int m_id){
        networkService = RetrofitSender.getNetworkService();
        Call<ResponseBody> response = networkService.delete_myword(m_id);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.d("########", "삭제하고싶다");

                }else{
                    Log.d("########", "ㄴㄴㄴxxxxxxx");
                }
                Log.d("########", "ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇxxxㅇㅇxxxx");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("########", t.getMessage());
            }
        });

    }
}
