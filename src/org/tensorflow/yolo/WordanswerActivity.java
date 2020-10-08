package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordanswerActivity extends AppCompatActivity {
    RelativeLayout answerlayout;
    TextView tvans;
    ImageButton btn;
    NetworkService networkService;
    List<Myword> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordanswer);
        answerlayout = (RelativeLayout) findViewById(R.id.answerlayout);

        networkService = RetrofitSender.getNetworkService();

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        tvans = (TextView) findViewById(R.id.tvans);
        btn = (ImageButton) findViewById(R.id.btn);
        Intent intent = getIntent();
        int correct = intent.getExtras().getInt("correct");
        String type = intent.getExtras().getString("type");
        int mB_id = intent.getIntExtra("b_id", 0);
        if (type.equals("word")) {
            if (correct == 1) {
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));
            } else {
                String answer = intent.getExtras().getString("answer");
                String answer2 = intent.getExtras().getString("answer2");
                tvans.setText(answer2 + "의 뜻은 " + answer);
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordwrong));
                Call<List<Myword>> call = networkService.get_myword();
                call.enqueue(new Callback<List<Myword>>() {
                    @Override
                    public void onResponse(Call<List<Myword>> call, Response<List<Myword>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<Myword> list2=new ArrayList<Myword>();
                            int count=0;
                            list=response.body();
                            int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                            for (int index = 0; index < totalElements; index++) {
                                if (list.get(index).getUid().equals(AppSetting.uid)) {
                                    if (list.get(index).getM_word_e().equals(answer2))
                                        count++;
                                }
                            }
                            if (count == 0) {
                                Call<Myword> call_word = networkService.put_myword(AppSetting.uid, mB_id, answer2, answer, 0);
                                call_word.enqueue(new Callback<Myword>() {
                                    @Override
                                    public void onResponse(Call<Myword> call, Response<Myword> response) {
                                        try {
                                            if (response.isSuccessful()) {

                                            } else {
                                                Log.e("########response isnt", "실패1");

                                            }
                                        } catch (Exception e) {
                                            Log.e("Error2", e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Myword> call, Throwable t) {
                                        Log.e("########response isnt", t.getMessage());
                                    }
                                });
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Myword>> call, Throwable t) {
                        Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
                    }
                });


            }
        } else if (type.equals("sentence")) {
            if (correct == 1) {
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));

            } else {
                String sen = intent.getExtras().getString("sen");
                String senk = intent.getExtras().getString("senk");
                tvans.setText(sen);
                answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wordwrong));
                Call<List<Myword>> call = networkService.get_myword();
                call.enqueue(new Callback<List<Myword>>() {
                    @Override
                    public void onResponse(Call<List<Myword>> call, Response<List<Myword>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<Myword> list2=new ArrayList<Myword>();
                            int count=0;
                            list=response.body();
                            int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                            for (int index = 0; index < totalElements; index++) {
                                if (list.get(index).getUid().equals(AppSetting.uid)) {
                                    if (list.get(index).getM_word_e().equals(sen))
                                        count++;
                                }
                            }
                            if (count == 0) {
                                Call<Myword> call_mysen = networkService.put_myword(AppSetting.uid, mB_id, sen, senk, 1);
                                call_mysen.enqueue(new Callback<Myword>() {
                                    @Override
                                    public void onResponse(Call<Myword> call, Response<Myword> response) {
                                        try {
                                            if (response.isSuccessful()) {

                                            } else {
                                                Log.e("########response isnt", "실패1");

                                            }
                                        } catch (Exception e) {
                                            Log.e("Error2", e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Myword> call, Throwable t) {
                                        Log.e("########response isnt", t.getMessage());
                                    }
                                });
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Myword>> call, Throwable t) {
                        Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
                    }
                });


            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                AppSetting.quizcount++;
                Log.v("quizCount=======", AppSetting.quizcount + "");
                if (AppSetting.quizcount < 3) {
                    Log.v("quizCount=======", AppSetting.quizcount + "");
                    intent = new Intent(getApplicationContext(), WordquizActivity.class);
                } else if (AppSetting.quizcount == 3) {
                    Log.v("Sentence Quiz=======", "OK");
                    intent = new Intent(getApplicationContext(), SenquizActivity.class);
                } else if (AppSetting.quizcount < 5) {
                    AppSetting.quizsen++;
                    intent = new Intent(getApplicationContext(), SenquizActivity.class);
                } else {
                    if (mB_id == 1) {
                        intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                    } else if (mB_id == 2) {
                        intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                    } else if (mB_id == 3) {
                        intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                    }

                    if ((AppSetting.progress / 10 == mB_id) && (AppSetting.progress % 10 == 1)) {
                        AppSetting.progress = mB_id * 10 + 2;
                        AppSetting.dp_bool = true;
                        // db에 값 반영
                        updateProgressDB();
                    } else {
                        // db 반영이 필요 없는 경우

                    }
                }


                Log.d("널체크", "문법점수엔 " + AppSetting.uid);
                finish();
                AppSetting.dp_bool = true;
                intent.putExtra("b_id", mB_id);
                startActivity(intent);


            }
        });
    }

    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB() {
        NetworkService networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        int progress = (AppSetting.big_progress + 1) * 10 + 2;
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid, progress);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null) {
                    Log.d("ㅋㅋㅋ4#", "수정하고싶다");

                } else {
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
