package org.tensorflow.yolo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.tensorflow.yolo.R;

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//qㅎㅎ
        setContentView(R.layout.activity_detail);
        Button back2camera = (Button) findViewById(R.id.back2camera) ;
        back2camera.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
    }

}
