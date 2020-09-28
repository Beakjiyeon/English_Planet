package org.tensorflow.yolo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class MywordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myword);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);
    }
}
