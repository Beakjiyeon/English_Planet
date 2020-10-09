package org.tensorflow.yolo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.yolo.view.CameraWordBookActivity;

public class VocaActivity extends AppCompatActivity {

    ImageView btn_cameraword; // 사물인식 단어장
    ImageView btn_picQuiz; // 사진퀴즈
    ImageView btn_wrongNote; // 오답노트
    ImageView btn_guide; // 사용설명서

    String str1="사물인식 단어장 - 촬영한 사진들을 확인할 수 있습니다.\n";
    String str2="사진퀴즈 - 촬영한 사진들을 활용한 퀴즈를 풀 수 있습니다.\n";
    String str3="오답노트 - 틀린 문제를 다시 학습할 수 있습니다.\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        btn_cameraword = findViewById(R.id.btn_carmeraword);
        btn_picQuiz=findViewById(R.id.btn_picquiz);
        btn_wrongNote=findViewById(R.id.btn_wrong);
        btn_guide = findViewById(R.id.btn_guide);

        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow(); 

            }
        });

        // 사물인식단어장 연결
        btn_cameraword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(), CameraWordBookActivity.class);
                startActivity(intent);
            }
        });

        // 사진퀴즈
        btn_picQuiz.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(),CameraQuizActivity.class);
                startActivity(intent);
            }
        });

        // 오답노트
        btn_wrongNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnmp.start();
                Intent intent = new Intent(getApplicationContext(),MywordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 사물인식 단어장, 사진퀴즈, 오답노트
        builder.setTitle("간단 설명서");
        builder.setMessage(str1+str2+str3);
        builder.setNeutralButton("OK",null);

        builder.show();
    }
 }
