package org.tensorflow.yolo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SenquizActivity extends AppCompatActivity {
    ImageButton checkans;
    int count;
    TextView sen_k;
    NetworkService networkService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senquiz);
        final LinearLayout layout= (LinearLayout)findViewById(R.id.layout);
        final LinearLayout ans_lay= (LinearLayout)findViewById(R.id.ans_lay);
        sen_k=(TextView)findViewById(R.id.sen_k);
        checkans = (ImageButton)findViewById(R.id.checkans);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        networkService = RetrofitSender.getNetworkService();
        Call<List<Quiz>> call =  networkService.get_qb(1);
        call.enqueue(new Callback<List<Quiz>>() {
            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                if (response.isSuccessful()) {
                    Quiz qw= response.body().get(AppSetting.quizsen);
                    String senk=qw.q_sentence_k;
                    String sen=qw.q_sentence_e;
                    sen_k.setText(senk);
                    final String[ ] ans= sen.split(" ");

                    String[] array = new String[ans.length];
                    for(int i=0;i<ans.length;i++){
                        array[i] = ans[i];
                    }

                    shuffle(array,array.length);

                    for(int i=0;i<array.length;i++){
                        final Button btn = new Button(getApplicationContext());
                        btn.setBackgroundResource(R.drawable.senword_btn);
                        btn.setId(i+1);
                        btn.setText(array[i]);
                        btn.setTextColor(Color.BLACK);
                        btn.setTextSize(25);

                        layout.addView(btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(view.getParent()==layout) {
                                    layout.removeView(btn);
                                    ans_lay.addView(btn);
                                }
                                else if(view.getParent()==ans_lay){
                                    ans_lay.removeView(btn);
                                    layout.addView(btn);
                                }
                            }});


                        // 정답확인
                        checkans.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(),WordanswerActivity.class);

                                String[] user_ans=new String[ans_lay.getChildCount()];
                                //ArrayList<String> user_ans = new ArrayList<>();
                                for (int i=0;i<ans_lay.getChildCount();i++) {
                                    Button v = (Button) ans_lay.getChildAt(i);
                                    user_ans[i]=v.getText()+"";
                                }

                                if(user_ans.length!=ans.length) {
                                    Toast.makeText(getApplicationContext(),"문장을 완성시켜주세요",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    count = 0;
                                    for (int i = 0; i < ans.length; i++) {
                                        if (user_ans[i].equals(ans[i]))
                                            count++;

                                    }
                                    if (count==ans.length) {
                                        intent.putExtra("correct",1);
                                    } else {
                                        intent.putExtra("correct", "0");
                                    }
                                    intent.putExtra("sen",sen);
                                    intent.putExtra("type", "sentence");
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
                else {

                }

            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
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

