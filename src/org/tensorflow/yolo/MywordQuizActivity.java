package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MywordQuizActivity extends AppCompatActivity implements Serializable{
    TextView wordtv, tv1, tv2, tv3;
    ImageView ivans1, ivans2, ivans3;
    String answer, answer2;
    String[] wordarray;
    String[] wrongarray = {"바나나", "동물", "코", "기억", "나무", "고양이", "악어", "시끄러운", "사자", "부족한", "기쁜", "이빨", "딱딱한", "과일", "조용한", "목", "고양이", "빛"};
    List<Myword> list,list2;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myword_quiz);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MywordActivity.class);
                startActivity(i);
                finish();
            }
        });

        wordtv = (TextView) findViewById(R.id.word);
        ivans1 = (ImageView) findViewById(R.id.wiv1);
        ivans2 = (ImageView) findViewById(R.id.wiv2);
        ivans3 = (ImageView) findViewById(R.id.wiv3);
        tv1 = (TextView) findViewById(R.id.wtv1);
        tv2 = (TextView) findViewById(R.id.wtv2);
        tv3 = (TextView) findViewById(R.id.wtv3);

        wordarray = new String[3];
        // myword에 있는 데이터들
        list2 = new ArrayList<Myword>();
        networkService = RetrofitSender.getNetworkService();
        if(AppSetting.myquizcount==0){
            Call<List<Myword>> call = networkService.get_myword();
            call.enqueue(new Callback<List<Myword>>() {
                @Override
                public void onResponse(Call<List<Myword>> call, Response<List<Myword>> response) {
                    if (response.isSuccessful()) {

                        list = response.body();
                        int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                        for (int index = 0; index < totalElements; index++) {
                            if (list.get(index).getUid().equals(AppSetting.uid) && list.get(index).getCheck_ws() == 0) {
                                list2.add(list.get(index));
                            }
                        }
                        Myword mw = list2.get(AppSetting.myquizcount);
                        String bo = mw.getM_word_e();
                        answer2 = bo;
                        answer = mw.getM_word_k();
                        wordtv.setText(bo);
                        wordarray[0] = answer;

                        int i1 = (int) (Math.random() * wrongarray.length);
                        int i2 = (int) (Math.random() * wrongarray.length);
                        if (i1 == i2) {
                            do {
                                i2 = (int) (Math.random() * wrongarray.length);
                                Log.d("값값값", i1 + "");
                                Log.d("값값값", i2 + "");
                            } while (i1 == i2);
                        }
                        wordarray[1] = wrongarray[i1];
                        wordarray[2] = wrongarray[i2];

                        // 단어 랜덤으로 넣기
                        shuffle(wordarray, 3);

                        tv1.setText(wordarray[0]);
                        tv2.setText(wordarray[1]);
                        tv3.setText(wordarray[2]);

                    } else {
                    }
                }
                @Override
                public void onFailure(Call<List<Myword>> call, Throwable t) {
                    Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
                }
            });
        }
        else{
            Intent intent = getIntent();
            list2 = (List<Myword>) intent.getSerializableExtra("list2");
            Myword mw = list2.get(AppSetting.myquizcount);
            String bo = mw.getM_word_e();
            answer2 = bo;
            answer = mw.getM_word_k();
            wordtv.setText(bo);
            wordarray[0] = answer;

            int i1 = (int) (Math.random() * wrongarray.length);
            int i2 = (int) (Math.random() * wrongarray.length);
            if (i1 == i2) {
                do {
                    i2 = (int) (Math.random() * wrongarray.length);
                    Log.d("값값값", i1 + "");
                    Log.d("값값값", i2 + "");
                } while (i1 == i2);
            }
            wordarray[1] = wrongarray[i1];
            wordarray[2] = wrongarray[i2];

            // 단어 랜덤으로 넣기
            shuffle(wordarray, 3);

            tv1.setText(wordarray[0]);
            tv2.setText(wordarray[1]);
            tv3.setText(wordarray[2]);

        }

        ivans1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MywordAnswerActivity.class);
                if(answer.equals(tv1.getText().toString())){intent.putExtra("correct",1);}
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);
                }
                intent.putExtra("type", "word");
                intent.putExtra("list2", (Serializable) list2);
                startActivity(intent);
                finish();
            }
        });
        ivans2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MywordAnswerActivity.class);
                Log.d("값",answer);
                if(answer.equals(tv2.getText().toString())){intent.putExtra("correct",1); }
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);}
                intent.putExtra("type", "word");
                intent.putExtra("list2", (Serializable) list2);
                startActivity(intent);
            }
        });
        ivans3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MywordAnswerActivity.class);
                if(answer.equals(tv3.getText().toString())){intent.putExtra("correct",1); }
                else{
                    intent.putExtra("answer",answer);
                    intent.putExtra("answer2",answer2);
                    intent.putExtra("correct",0);}
                intent.putExtra("type", "word");
                intent.putExtra("list2", (Serializable) list2);
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
