package org.tensorflow.yolo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookActivity extends Activity implements View.OnClickListener, TextToSpeech.OnInitListener {
    // iterator
    Iterator<String> mItr;
    ArrayList<String> mBookList;
    NetworkService networkService;
    Button mBtn;
    Button mBtn_EngToKor;
    Button mBtn_KorToEng;

    NaverTranslateTask asyncTask;

    // TextView List
    public static TextView[] mTvList;

    // 원문을 보여주기 위한 ArrayList<>
    public ArrayList<String> mOrigin;

    TextToSpeech tts;
    String[] b_text;

    // b_id
    int mB_id;

    //
    TextView mChapter;
    TextView mBooktitle;

    // tap image - 손가락
    ImageView mTap;

    // animation
    Animation mAnimation;

    // 효과음
    private MediaPlayer mp;

    // Queue
    Queue mBookQueue;





    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_book);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        // 효과음
        mp = MediaPlayer.create(this, R.raw.book);

        // ArrayList create
        mOrigin = new ArrayList<>();

        // getIntent
        Intent intent = getIntent();
        mB_id = intent.getIntExtra("b_id", 0);

        mChapter = findViewById(R.id.chapter);
        mBooktitle = findViewById(R.id.book_title);

        // tap imageview findviewbyid
        mTap = findViewById(R.id.tap1);
        // animation
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        //애니메이션 시작
        mTap.startAnimation(mAnimation);

        //stack create
        mBookQueue = new LinkedList();


        // chaper하고 책 제목 변경
        switch (mB_id) {
            case 2:
                mChapter.setText("Chapter2");
                mBooktitle.setText("Friends");
                mTap.setVisibility(View.GONE);
                mAnimation.cancel();
                break;

            case 3:
                mChapter.setText("Chapter3");
                mBooktitle.setText("Tall and Short");
                mTap.setVisibility(View.GONE);
                mAnimation.cancel();
                break;
        }


        mBtn = findViewById(R.id.btn_next); // 다음페이지 버튼
        // EngToKor
        mBtn_EngToKor = findViewById(R.id.btn_EngToKor);
        // KorToEng
        mBtn_KorToEng = findViewById(R.id.btn_KorToEng);
        // 초기값 비활성화 : 처음에 영어문장이므로 영어로 번역하는 버튼 비활성화함
        mBtn_KorToEng.setEnabled(false);
        mBtn_KorToEng.setBackgroundColor(Color.GRAY);

        // textview
        mTvList = new TextView[3];
        mTvList[0] = findViewById(R.id.book_text1);
        mTvList[1] = findViewById(R.id.book_text2);
        mTvList[2] = findViewById(R.id.book_text3);

        for (int i = 0; i < 3; i++) {
            mTvList[i].setOnClickListener(this);
        }

        tts = new TextToSpeech(this.getApplicationContext(), this);

       // Intent inten = new Intent(getApplicationContext(), BookwordActivity.class);
        //startActivity(inten);

        // back btn
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        networkService = RetrofitSender.getNetworkService();

        // 책 가져오는 코드
        Call<Book> response = networkService.get_book(mB_id);
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
        // 효과음
        mp.start();
        mTap.setVisibility(View.GONE);
        mAnimation.cancel();
        // 다음페이지로 넘어가면 초기값 세팅
        SetEngToKor();

        BookItr();

    }

    public void btnPrev(View v){
        mp.start();
        for (int i=0;i<3;i++) {
            mTvList[i].setText("");
        }

        for (int i=0;i<3;i++){
            if(!mBookQueue.isEmpty()){
                mTvList[i].setText(mBookQueue.poll().toString());
            }
        }
    }

    private void BookItr() {
        try {
            String s = "";
            mOrigin.clear();
            mTvList[0].setText("");
            mTvList[1].setText("");
            mTvList[2].setText("");

            if (mItr.hasNext()) {
                s = mItr.next();
                mTvList[0].setText(s);
                mOrigin.add(s);
                mBookQueue.offer(s);


                s = mItr.next();
                mTvList[1].setText(s);
                mOrigin.add(s);
                mBookQueue.offer(s);

                s = mItr.next();
                mTvList[2].setText(s);
                mOrigin.add(s);
                mBookQueue.offer(s);

            }

        } catch (NoSuchElementException ne) {
            mBtn.setText("Quit");
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast t = Toast.makeText(getApplicationContext(), "학습 종료", Toast.LENGTH_SHORT);
                    // 지연 : 동화 학습 종료한 상태로 변경
                    // 1. 만약 처음 동떄
                    // 2. 두번째 학습인데 십의 자리가 b_id와 다른 경우는 db에 입력하지 않음
                    // 즉!!!
                    // 현재 p_progress값과 현재 b_id가 같고, 동화미션을 수행했을경우(즉 아직 수행전인 상태로 db에 남아있는 상태의 경우)에 db반영시킴
                    if((AppSetting.progress/10==mB_id)&&(AppSetting.progress%10==0)){
                        AppSetting.progress = mB_id*10+1;
                        AppSetting.dp_bool = true;
                        // db에 값 반영
                        updateProgressDB();
                    }else{
                        // db 반영이 필요 없는 경우

                    }



                    Log.d("널체크", "동화엔 " + AppSetting.uid);
                    if(mB_id==1) {
                        Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                        startActivity(intent);
                        t.show();
                        finish();
                    }
                    if(mB_id==2) {
                        Intent intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                        startActivity(intent);
                        t.show();
                        finish();
                    }
                    if(mB_id==3) {
                        Intent intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                        startActivity(intent);
                        t.show();
                        finish();
                    }

                }
            });

        }

    }

    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB() {
        networkService = RetrofitSender.getNetworkService();
        int progress=(AppSetting.big_progress+1)*10+1;
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid, progress);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null) {
                    Log.d("#########db수정#####", "updateProgressDB");
                } else {
                    Log.d("#########db수정#####", "updateProgressDB 실패");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("#########db수정#####", "updateProgressDB 실패");
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


    public void btnEngToKor(View view) {
        asyncTask = new NaverTranslateTask();
        String str = "";
        for (int i = 0; i < 3; i++) {
            if (mTvList == null) break;
            str += mTvList[i].getText().toString() + "/";
        }
        asyncTask.execute(str);

//        for (int i=0;i<mTransText.length;i++){
//                mTvList[i].setText(mTransText[i]);
//            }

        // setdisabeld 값 변경하기
        SetKorToEng();


    }

    public void btnKorToEng(View view) {

        for (int i = 0; i < mOrigin.size(); i++) {
            mTvList[i].setText(mOrigin.get(i));
        }

        SetEngToKor();

    }

    // TODO EngTOKor, KorToEng 바꿀때마다 변경하는 함수 제작

    private void SetEngToKor() {
        mBtn_KorToEng.setEnabled(false);
        mBtn_KorToEng.setBackgroundColor(Color.GRAY);
        mBtn_EngToKor.setEnabled(true);
        mBtn_EngToKor.setBackgroundColor(Color.parseColor("#69F0AE"));

        for (int i = 0; i < 3; i++) {
            mTvList[i].setEnabled(true);
        }

    }

    private void SetKorToEng() {
        mBtn_KorToEng.setEnabled(true);
        mBtn_KorToEng.setBackgroundColor(Color.parseColor("#69F0AE"));
        mBtn_EngToKor.setEnabled(false);
        mBtn_EngToKor.setBackgroundColor(Color.GRAY);

        for (int i = 0; i < 3; i++) {
            mTvList[i].setEnabled(false);
        }

    }


}


