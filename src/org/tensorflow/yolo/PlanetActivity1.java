package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

public class PlanetActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet1);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        ImageView img = findViewById(R.id.imageView2);
        img.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BookActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
