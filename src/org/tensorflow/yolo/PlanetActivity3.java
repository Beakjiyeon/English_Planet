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

public class PlanetActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet3);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);


        // 뒤로가기
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PlanetListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView img = findViewById(R.id.imageView7);
        img.setOnClickListener(view -> {
            MainActivity.btnmp.start();
            Intent intent = new Intent(getApplicationContext(), BookActivity.class);
            intent.putExtra("b_id",3);
            startActivity(intent);
        });

        ImageView img2 = findViewById(R.id.imageView8);
        img2.setOnClickListener(view -> {
            MainActivity.btnmp.start();
            Intent intent = new Intent(getApplicationContext(), WordquizActivity.class);
            //intent.putExtra("b_id",2);
            startActivity(intent);
            //finish();
        });

        ImageView img3 = findViewById(R.id.imageView9);
        img3.setOnClickListener(view -> {
            MainActivity.btnmp.start();
            Intent intent = new Intent(getApplicationContext(),SentenceActivity.class);
            //intent.putExtra("b_id",2);
            startActivity(intent);
            //finish();
        });


    }
}
