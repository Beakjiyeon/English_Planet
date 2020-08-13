package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import org.tensorflow.yolo.setting.AppSetting;

public class TutorialActivity extends AppCompatActivity {

    int mIdx=1;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        mView  = (ConstraintLayout) findViewById(R.id.tutorial_layout);
        mView.setOnClickListener(view -> {
            mIdx++;
            NextTutorial(mIdx);
        });


    }

    private void NextTutorial(int mIdx) {
        switch (mIdx) {
            case 2:
                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial2));
                break;
            case 3:
                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial3));
                break;
            case 4:
                mView.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.tutorial4));
                break;
            default:
                // 튜토리얼이 종료되었으므로 값을 true로 바꾼다.
                AppSetting.tutorial=true;
                // PlanetListActivity 실행
                Intent intent = new Intent(getApplicationContext(), PlanetListActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
