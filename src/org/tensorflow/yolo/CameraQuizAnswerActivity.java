package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

public class CameraQuizAnswerActivity extends AppCompatActivity {
    RelativeLayout answerlayout;
    TextView tvans;
    ImageButton btn;
    Intent intent2 = null;
    Intent intent3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordanswer);
        answerlayout = (RelativeLayout) findViewById(R.id.answerlayout);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        tvans = (TextView) findViewById(R.id.tvans);
        btn = (ImageButton) findViewById(R.id.btn);
        Intent intent = getIntent();
        Camera cquiz_answer = (Camera) intent.getSerializableExtra("cquiz_answer");
        String clicked_word=intent.getExtras().getString("clicked_word");
        int b_id=intent.getExtras().getInt("b_id");
        answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));

        if(cquiz_answer.getC_word_e().equals(clicked_word)){
            // 정답입니다.
            answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordcorrect));

        }else{
            // 오답입니다.

            tvans.setText(cquiz_answer.getC_word_e() + "의 뜻은 " + cquiz_answer.getC_word_k());
            answerlayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.wordwrong));
        }

        btn.setOnClickListener(view -> {
            AppSetting.cq_count++;
                Log.v("cqCount=======", AppSetting.cq_count + "");
                if (AppSetting.cq_count < 5) {
                    Log.v("cq_countCount=======", AppSetting.cq_count + "");
                    intent2 = new Intent(getApplicationContext(), CameraQuizActivity.class);
                    startActivity(intent2);

                } else if (AppSetting.cq_count == 5) {
                    Toast.makeText(getApplicationContext(), "이동하자", Toast.LENGTH_LONG).show();

                    Log.v("Sentence Quiz=======", "OK");
                    intent3 = new Intent(getApplicationContext(), PlanetListActivity.class);
                    intent.putExtra("b_id",1 );
                    startActivity(intent3);
                    AppSetting.cq_count=0;
                    finish();
                }

            }
        );

    }
}