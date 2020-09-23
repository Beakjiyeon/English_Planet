package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        ImageView book1 = (ImageView)findViewById(R.id.btnVoca);
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VocaActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        ImageView logout = (ImageView)findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentt = new Intent(getApplicationContext(),MainActivity.class);
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentt.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //intent.putExtra("KILL",true);
                Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intentt);
            }
        });

        ImageView modify = (ImageView)findViewById(R.id.btnSettings);
        modify.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent info = new Intent(getApplicationContext(), modifyinfoActivity.class);
                startActivity(info);
            }
        });



    }
}