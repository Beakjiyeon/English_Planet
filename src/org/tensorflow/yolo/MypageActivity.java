package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.yolo.setting.AppSetting;
import org.tensorflow.yolo.view.CameraWordBookActivity;

public class MypageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        TextView userinfo = (TextView)findViewById(R.id.userInfo);
        userinfo.setText(AppSetting.unickname);

        ImageView book1 = (ImageView)findViewById(R.id.btnBook1);
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraWordBookActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        TextView voca = (TextView)findViewById(R.id.voca);
        voca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraWordBookActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });


    }
}