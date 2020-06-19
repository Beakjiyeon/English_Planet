package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WordanswerActivity extends AppCompatActivity {
    RelativeLayout answerlayout;
    TextView tvans;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordanswer);
        answerlayout = (RelativeLayout)findViewById(R.id.answerlayout);
        tvans = (TextView)findViewById(R.id.tvans);
        btn = (ImageButton)findViewById(R.id.btn);
        Intent intent = getIntent();
        int correct = intent.getExtras().getInt("correct");

        if(correct==1){
            answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));
        }
        else{
            String answer = intent.getExtras().getString("answer");
            String answer2 = intent.getExtras().getString("answer2");
            tvans.setText(answer2+"의 뜻은 "+answer);
            answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordwrong));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WordquizActivity.class);
                startActivity(intent);
            }
        });
    }

}
