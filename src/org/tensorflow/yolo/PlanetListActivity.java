package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.yolo.setting.AppSetting;
import org.tensorflow.yolo.view.ClassifierActivity;

// 주희 : 행성리스트 액티비티
public class PlanetListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planets);
        String msg = AppSetting.unickname+"님 환영합니다.";
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        ImageView planet1 = (ImageView) findViewById(R.id.planet1);
        planet1.setImageResource(R.drawable.planet1);

        planet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
                startActivity(intent);
            }
        });

        ImageView planet2 = (ImageView) findViewById(R.id.planet2);
        planet2.setImageResource(R.drawable.planet2);

        planet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanetActivity2.class);
                startActivity(intent);
            }
        });

        ImageView planet3 = (ImageView) findViewById(R.id.planet3);
        planet3.setImageResource(R.drawable.planet3);

        planet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlanetActivity3.class);
                startActivity(intent);
            }
        });

        ImageView dev = (ImageView) findViewById(R.id.dev);
        dev.setImageResource(R.drawable.planet_devs);

        ImageView btnCamera = (ImageView) findViewById(R.id.BtnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"영어 뜻을 알고싶은 물건을 찍어보세요",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        ImageView btnMypage = (ImageView) findViewById(R.id.BtnMypage);
        btnMypage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

    }



}
