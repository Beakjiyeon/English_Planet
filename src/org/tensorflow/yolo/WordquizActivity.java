package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordquizActivity extends AppCompatActivity {
    TextView wordtv,tv1,tv2,tv3;
    ImageView ivans1, ivans2, ivans3;
    String answer,answer2;
    String[] wordarray;
    String[] wrongarray={"바나나", "동물", "코", "기억", "나무","고양이","악어","시끄러운","사자","부족한","기쁜","이빨","딱딱한","과일","조용한","목","고양이"};
    int mB_id;
    NetworkService networkService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordquiz);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        // back btn
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wordtv = (TextView)findViewById(R.id.word);
        ivans1 = (ImageView)findViewById(R.id.wiv1);
        ivans2 = (ImageView)findViewById(R.id.wiv2);
        ivans3 = (ImageView)findViewById(R.id.wiv3);
        tv1= (TextView)findViewById(R.id.wtv1);
        tv2= (TextView)findViewById(R.id.wtv2);
        tv3= (TextView)findViewById(R.id.wtv3);

        wordarray= new String[3];

            // getIntent
            Intent intent = getIntent();
            mB_id = intent.getIntExtra("b_id", 0);


        // 서버 데이터 가져옴
        networkService = RetrofitSender.getNetworkService();
        //Call<Bookword> call =  retrofitConnection.server.get_bw_data("1");

        Call<List<Bookword>> call =  networkService.get_bw(mB_id);
        //Log.d("흠흠무무무이",AppSetting.quizcount+"");
        call.enqueue(new Callback<List<Bookword>>() {
            @Override
            public void onResponse(Call<List<Bookword>> call, Response<List<Bookword>> response) {
                if (response.isSuccessful()) {
                    Log.d("흠흠흠",String.valueOf(AppSetting.quizcount));
                    Bookword bw= response.body().get(AppSetting.quizcount);
                    String bo=bw.getB_word_e();
                    answer2=bo;
                    answer = bw.getB_word_k();
                    wordtv.setText(bo);
                    wordarray[0] = answer;

                    int i1 = (int)(Math.random()*wrongarray.length);
                    int i2 = (int)(Math.random()*wrongarray.length);
                    if(i1==i2){
                        do{
                            i2=(int)(Math.random()*wrongarray.length);
                            Log.d("값값값",i1+"");
                            Log.d("값값값",i2+"");
                        }while(i1==i2);
                    }
                    wordarray[1] = wrongarray[i1];
                    wordarray[2] = wrongarray[i2];

                    // 단어 랜덤으로 넣기
                    shuffle(wordarray,3);

                    tv1.setText(wordarray[0]);
                    tv2.setText(wordarray[1]);
                    tv3.setText(wordarray[2]);

                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<Bookword>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
            }
        });

        ivans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WordanswerActivity.class);
                if(answer.equals(tv1.getText().toString())){intent.putExtra("correct",1);}
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);
                }
                intent.putExtra("type", "word");
                intent.putExtra("b_id",  mB_id);
                startActivity(intent);
                finish();
            }
        });
        ivans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WordanswerActivity.class);
                Log.d("값",answer);
                if(answer.equals(tv2.getText().toString())){intent.putExtra("correct",1);}
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);}
                intent.putExtra("type", "word");
                intent.putExtra("b_id",  mB_id);
                startActivity(intent);
            }
        });
        ivans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WordanswerActivity.class);
                if(answer.equals(tv3.getText().toString())){intent.putExtra("correct",1);}
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);}
                intent.putExtra("type", "word");
                intent.putExtra("b_id",  mB_id);
                startActivity(intent);
            }
        });

    }

    public static void shuffle(String[] array, int count){
        String temp, temp2;
        int randomNum1, randomNum2;

        for(int i=0; i<count; i++){
            randomNum1 = (int)(Math.random()*array.length);
            temp = array[randomNum1];
            randomNum2 = (int)((Math.random()*array.length));
            temp2 = array[randomNum2];
            array[randomNum1] = temp2;
            array[randomNum2] = temp;
        }
    }

}
