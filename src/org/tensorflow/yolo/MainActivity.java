package org.tensorflow.yolo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.tensorflow.yolo.view.CameraActivity;
import org.tensorflow.yolo.view.ClassifierActivity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=(Button)findViewById(R.id.test);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"와우",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
    }
}