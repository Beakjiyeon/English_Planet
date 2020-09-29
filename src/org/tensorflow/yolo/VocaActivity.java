package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
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
                // TODO
                Toast.makeText(getApplicationContext(),"구현예정.",Toast.LENGTH_SHORT).show();
            }
        });

        // 사물인식단어장 연결
        btn_cameraword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraWordBookActivity.class);
                startActivity(intent);
            }
        });

        // 사진퀴즈
        btn_picQuiz.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CameraQuizActivity.class);
                startActivity(intent);
            }
        });

        // 오답노트
        btn_wrongNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MywordActivity.class);
                startActivity(intent);
            }
        });

    }}