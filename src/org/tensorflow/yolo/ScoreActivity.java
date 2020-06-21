package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ScoreActivity extends AppCompatActivity {
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ImageButton btn = (ImageButton)findViewById(R.id.btn);
        LinearLayout scorelayout = (LinearLayout)findViewById(R.id.scorelayout);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        // 발음 점수 받아오기
       Intent intent=new Intent(this.getIntent());
        Double s=intent.getDoubleExtra("myscore",0);
        int countword = intent.getIntExtra("countword",0);

        count++;

        // 점수에 따라 배경 설정
        if(s>=3) {
            if(countword>=2)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.excellent));
            else if(countword==1)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.good));
            else
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));
        }
        else if (s>=2) {
            if(countword>=2)
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.good));
            else
                scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));
        }
        else
            scorelayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.miss));


        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), SentenceActivity.class);
//                startActivity(intent);
                finish();
            }
        });



    }
}
