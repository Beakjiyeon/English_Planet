package org.tensorflow.yolo;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class BookActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener{
    // iterator
    Iterator<String> mItr;
    ArrayList<String> mBookList;
    NetworkService networkService;
    Button mBtn;
    Button mBtn_trans;

    NaverTranslateTask asyncTask;


    // TextView List
     public static TextView[] mTvList;

    TextToSpeech tts;
    String[] b_text;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_book);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        mBtn = findViewById(R.id.btn_next); // 다음페이지 버튼
        mBtn_trans = findViewById(R.id.btn_trans); // 번역 버튼

        // textview
        mTvList = new TextView[3];
        mTvList[0] = findViewById(R.id.book_text1);
        mTvList[1] = findViewById(R.id.book_text2);
        mTvList[2] = findViewById(R.id.book_text3);

        for (int i = 0; i < 3; i++) {
            mTvList[i].setOnClickListener(this);
        }

        tts = new TextToSpeech(this.getApplicationContext(), this);

        // back btn
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = new Intent(this, BookwordActivity.class);
        startActivity(intent);

        networkService = RetrofitSender.getNetworkService();

        // b_id : 1번으로 설정
        Call<Book> response = networkService.get_book(1);
        response.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                Book b = (Book) response.body();
                b_text = b.getB_text().split("\r\n");
                mBookList = new ArrayList<>(Arrays.asList(b_text));
                mItr = mBookList.iterator();

                // book iterator
                BookItr();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });

    }

    public void btnNext(View v) {
        BookItr();

    }

    private void BookItr() {
        try {
            String s = "";
            mTvList[0].setText("");
            mTvList[1].setText("");
            mTvList[2].setText("");

            if (mItr.hasNext()) {
                s = mItr.next();
                mTvList[0].setText(s);

                s = mItr.next();
                mTvList[1].setText(s);

                s = mItr.next();
                mTvList[2].setText(s);
            }

        } catch (NoSuchElementException ne) {
            mBtn.setText("Quit");
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast t = Toast.makeText(getApplicationContext(), "학습 종료", Toast.LENGTH_SHORT);
                    AppSetting.progress=1;
                    AppSetting.dp_bool=true;
                    // 쳅터 1의 시작=0, 동화=1
                    // db에 값 반영
                    updateProgressDB();
                    Log.d("널체크","동화엔 "+AppSetting.uid);
                    Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                    startActivity(intent);
                    t.show();
                    finish();
                }
            });

        }

    }
    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB(){
        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid,1);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("ㅋㅋㅋ#", "수정하고싶다");


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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.book_text1:
                tts.speak(mTvList[0].getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.book_text2:
                tts.speak(mTvList[1].getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.book_text3:
                tts.speak(mTvList[2].getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                break;


        }
    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //en-us-x-sfg#female_2-local
            tts.setLanguage(Locale.ENGLISH);
            Voice v = new Voice("en-us-x-sfg#female_2-local",
                    new Locale("en", "US"),
                    400,
                    400,
                    true,
                    null);
            tts.setVoice(v);
            tts.setSpeechRate(0.8f);
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void btnTrans(View view){
        asyncTask = new NaverTranslateTask();
        String str ="";
        for(int i=0;i<3;i++) {
            if (mTvList==null) break;
            str += mTvList[i].getText().toString()+"/";
        }
        asyncTask.execute(str);
    }


}


