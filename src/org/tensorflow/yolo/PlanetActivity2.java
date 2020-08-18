package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;

public class PlanetActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet2);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        ImageView img = findViewById(R.id.imageView);
        img.setOnClickListener(view -> {
            MainActivity.btnmp.start();
            Intent intent = new Intent(getApplicationContext(), BookActivity.class);
            intent.putExtra("b_id",2);
            startActivity(intent);
        });

//        ImageView img2 = findViewById(R.id.imageView3);
//        img2.setOnClickListener(view -> {
        // MainActivity.btnmp.start();
//            Intent intent = new Intent(getApplicationContext(), WordquizActivity.class);
//            startActivity(intent);
//            //finish();
//        });
//
//        ImageView img3 = findViewById(R.id.imageView4);
//        img3.setOnClickListener(view -> {
        // MainActivity.btnmp.start();
//            Intent intent = new Intent(getApplicationContext(),SentenceActivity.class);
//            startActivity(intent);
//            //finish();
//        });

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PlanetListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
