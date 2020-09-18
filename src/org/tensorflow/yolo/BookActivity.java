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

    NetworkService networkService;
    Button mBtnNext;
    Button mBtn_EngToKor;
    Button mBtn_KorToEng;
    Button mBtnPrev;

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

    Stack<String> mStackA;
    Stack<String> mStackB;

    // for book list
    int idx = 0;
    String temp = "";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_book);

        // 네비게이션바 없애는 코드
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

        // tap imageview findviewbyid : 손가락 표시
        mTap = findViewById(R.id.tap1);
        // 손가락 animation
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        //애니메이션 시작
        mTap.startAnimation(mAnimation);


        mStackA = new Stack<>();
        mStackB = new Stack<>();


        // chaper하고 책 제목 변경
        switch (mB_id) {
            case 2:
                mChapter.setText("Chapter2");
                mBooktitle.setText("Friends");
                // 2번째 행성 애니메이션 취소
                mTap.setVisibility(View.GONE);
                mAnimation.cancel();
                break;

            case 3:
                mChapter.setText("Chapter3");
                mBooktitle.setText("Tall and Short");
                // 3번째 행성 애니메이션 취소
                mTap.setVisibility(View.GONE);
                mAnimation.cancel();
                break;
        }

        mBtnNext = findViewById(R.id.btn_next); // 다음페이지 버튼
        mBtnPrev = findViewById(R.id.prev); // 이전페이지 버튼
        // 초기값 false : 이전 페이지로 넘어가지 않도록
        mBtnPrev.setVisibility(View.INVISIBLE);

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

        if(AppSetting.progress%10==0&&AppSetting.progress/10==mB_id) {
            Intent inten = new Intent(getApplicationContext(), BookwordActivity.class);
            inten.putExtra("b_id", mB_id);
            startActivity(inten);
        }

        // back btn 뒤로가기 버튼
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

                // 첫번째 페이지 설정
                for (int i = 0; i < 3; i++) {
                    mTvList[i].setText(b_text[idx]);
                    idx++;
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });


    }

    public void btnNext(View v) {
        boolean fin = false;

        // 효과음
        mp.start();
        // 다음페이지로 넘어가면 손가락 애니메이션 사라짐
        mTap.setVisibility(View.GONE);
        mAnimation.cancel();
        // 다음페이지로 넘어가면 초기값 세팅
        SetEngToKor();

        // prev 버튼 활성화
        mBtnPrev.setVisibility(View.VISIBLE);
        mBtnPrev.setEnabled(true);
        mBtnPrev.setBackgroundColor(Color.parseColor("#69F0AE"));

        //현재 내용을 Stack A push
        temp = "";
        for (int i = 0; i < 3; i++) {
            temp += mTvList[i].getText().toString() + "/";
        }
        mStackA.push(temp);

        clearTextView(); // textview 초기화

        // mStackB가 비어져 있으면
        if (mStackB.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                if (idx == b_text.length) {
                    fin = true;
                    break;
                }
                mTvList[i].setText(b_text[idx]);
                idx++;
            }

        } else { // mStackB에 내용이 있으면
            String[] str_list = mStackB.pop().split("/");
            for (int i = 0; i < 3; i++) {
                mTvList[i].setText(str_list[i]);
            }

        }

        // 종료 코드
        if (fin) {
            mBtnNext.setText("QUIT");
            mBtnNext.setOnClickListener(view -> {
                Toast t = Toast.makeText(getApplicationContext(), "학습 종료", Toast.LENGTH_SHORT);
                t.show();
                // 지연 : 동화 학습 종료한 상태로 변경
                // 1. 만약 처음 동떄
                // 2. 두번째 학습인데 십의 자리가 b_id와 다른 경우는 db에 입력하지 않음
                // 즉!!!
                // 현재 p_progress값과 현재 b_id가 같고, 동화미션을 수행했을경우(즉 아직 수행전인 상태로 db에 남아있는 상태의 경우)에 db반영시킴
                if ((AppSetting.progress / 10 == mB_id) && (AppSetting.progress % 10 == 0)) {
                    AppSetting.progress = mB_id * 10 + 1;
                    AppSetting.dp_bool = true;
                    // db에 값 반영
                    updateProgressDB();
                } else {
                    // db 반영이 필요 없는 경우

                }

                Log.d("널체크", "동화엔 " + AppSetting.uid);
                if (mB_id == 1) {
                    Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                    startActivity(intent);
                    t.show();
                    finish();
                }
                if (mB_id == 2) {
                    Intent intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                    startActivity(intent);
                    t.show();
                    finish();
                }
                if (mB_id == 3) {
                    Intent intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                    startActivity(intent);
                    t.show();
                    finish();
                }

        });
    }


    setOriginArray();

}


    public void btnPrev(View v) {
        mp.start(); // 효과음

        temp = "";
        for (int i = 0; i < 3; i++) {
            temp += mTvList[i].getText().toString() + "/";
        }
        mStackB.push(temp);

        clearTextView();

        String[] str_list = mStackA.pop().split("/");
        for (int i = 0; i < 3; i++) {
            mTvList[i].setText(str_list[i]);
        }

        setOriginArray();

        if (mStackA.empty()) {
            mBtnPrev.setEnabled(false);
            mBtnPrev.setBackgroundColor(Color.GRAY);
        }

    }

    // 한글로 번역 후 다시 원문으로 되돌리는 코드
    private void setOriginArray() {
        mOrigin.clear();
        for (int i = 0; i < 3; i++) {
            mOrigin.add(mTvList[i].getText().toString());
        }
    }

    // 텍스트 뷰를 깨끗하게.. 만드는 코드
    private void clearTextView() {
        for (int i = 0; i < 3; i++) {
            mTvList[i].setText("");
        }
    }


    // 지연: DB 진행률을 수정하는 함수
    // 아직 연결 X!!!!!!
    void updateProgressDB() {
        networkService = RetrofitSender.getNetworkService();
        int progress = (AppSetting.big_progress + 1) * 10 + 1;
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


    // TTS 설정 코드
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


    // TTS 설정 코드
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


        // setdisabeld 값 변경하기
        SetKorToEng();


    }

    public void btnKorToEng(View view) {

        for (int i = 0; i < mOrigin.size(); i++) {
            mTvList[i].setText(mOrigin.get(i));
        }

        SetEngToKor();

    }

    // EngTOKor, KorToEng 바꿀때마다 변경하는 함수 제작
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


