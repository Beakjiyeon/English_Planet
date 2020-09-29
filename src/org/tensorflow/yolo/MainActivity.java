package org.tensorflow.yolo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.yolo.view.CameraActivity;
import org.tensorflow.yolo.view.CameraWordBookActivity;
import org.tensorflow.yolo.view.ClassifierActivity;

public class MainActivity extends AppCompatActivity {

    // 지연 : 배경음악 객체
    private static MediaPlayer mp;
    // 지연 : 배경음악 설정
    // 지연 : 홈버튼 누를때 음악 종료

    public static MediaPlayer btnmp;
    @Override
    protected void onUserLeaveHint(){
        mp.pause();
        super.onUserLeaveHint();
    }
    @Override
    public void onResume(){
        mp.start();
        super.onResume();
    }
    @Override
    public void onDestroy(){
        mp.stop();
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        mp.stop();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        // 지연 : 배경음악시작
        mp = MediaPlayer.create(this, R.raw.milos);
        mp.setLooping(false); // 반복여부
        mp.start();

        // 버튼 음악
        btnmp = MediaPlayer.create(this, R.raw.btnsound);



        // 두 버튼 연결
        Button main_btn1 = (Button) findViewById(R.id.btn_login);
        Button main_btn2 = (Button) findViewById(R.id.btn_signup);
        // 지연 배경 반짝임 객체 연결
        ImageView blink1=(ImageView)findViewById(R.id.blink);
        ImageView blink2=(ImageView)findViewById(R.id.blink2);
        ImageView blink3=(ImageView)findViewById(R.id.blink3);
        // 지연 : blink 애니메이션
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(800);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        //애니메이션 시작
        blink1.startAnimation(mAnimation);
        blink2.startAnimation(mAnimation);
        blink3.startAnimation(mAnimation);

        // 로그인 버튼 누르면
        main_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 효과틈
                btnmp.start();

                //Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();
                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 회원가입 버튼 누르면
        main_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 효과음
                btnmp.start();

                //Toast.makeText(getApplicationContext(), "액티비티 전환", Toast.LENGTH_LONG).show();
                // 액티비티 전환 코드
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });


//        Button b=(Button)findViewById(R.id.test);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"와우",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
//                startActivity(intent);//액티비티 띄우기
//            }
//        });
//
//        Button b2=(Button)findViewById(R.id.testbook);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"와2우",Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), CameraWordBookActivity.class);
//                startActivity(intent);//액티비티 띄우기
//            }
//        });
    }
}