package org.tensorflow.yolo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;

import java.net.CacheResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialActivity extends AppCompatActivity {

    NetworkService networkService;

    int mIdx=1;
    View mView;

    // 더빙 재생
    private MediaPlayer tutorial1;
    private MediaPlayer tutorial2;
    private MediaPlayer tutorial3;

    // 효과음
    private MediaPlayer explosion;

    private TextView txt;

    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        txt = findViewById(R.id.tutorial_text);

        // 만나서 반가워. 나는 너의 우주탐험을 도와줄 밀로라고해
        tutorial1 = MediaPlayer.create(this, R.raw.tutorial1);
        // 행성들이 서로 충돌해 빛을 잃고 말았어.
        tutorial2 = MediaPlayer.create(this, R.raw.tutorial2);
        // 우리 함께 힘을 합쳐 행성의 빛을 되찾아주자.
        tutorial3 = MediaPlayer.create(this, R.raw.tutorial3);
        // 우당탕탕 폭발음
        explosion = MediaPlayer.create(this, R.raw.explosion);



        mView  = (ConstraintLayout) findViewById(R.id.tutorial_layout);
        // 애니메이션 넣고싶어
        mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        //애니메이션 시작
        txt.startAnimation(mAnimation);


        tutorial1.start(); // 첫번째 튜토리얼


        mView.setOnClickListener(view -> {
            mIdx++;
            NextTutorial(mIdx);
        });


    }

    private void NextTutorial(int mIdx) {
        switch (mIdx) {
            case 2: // 두번째 튜토리얼
                txt.setVisibility(View.GONE);
                mAnimation.cancel();

                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial2));
                tutorial1.stop();
                explosion.start();
                break;
            case 3: // 세번째 튜토리얼
                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial3));
                explosion.stop();
                tutorial2.start();
                break;
            case 4:
                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial4));
                tutorial2.stop();
                tutorial3.start();
                break;
            default:
                tutorial3.stop();

                // 튜토리얼이 종료되었으므로 값을 0으로 바꾼다.
                networkService = RetrofitSender.getNetworkService();
                Call<ResponseBody> resp = networkService.updateBigProgress(AppSetting.uid, 0);
                resp.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response != null) {
                            Log.d("#########db수정#####", "updateProgressDB");
                            // PlanetListActivity 실행
                            Intent intent = new Intent(getApplicationContext(), PlanetListActivity.class);
                            startActivity(intent);
                            finish();

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
    }

}
