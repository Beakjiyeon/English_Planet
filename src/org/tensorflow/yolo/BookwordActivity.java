package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookwordActivity extends AppCompatActivity {
    ImageButton btnx;
    NetworkService networkService;
    int mB_id;
    int wb[] = {R.id.wbtn1,R.id.wbtn2,R.id.wbtn3,R.id.wbtn4,R.id.wbtn5,R.id.wbtn6};
    Button[] wbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);
        setContentView(R.layout.activity_bookword);
        btnx = (ImageButton)findViewById(R.id.btnxx);
        wbtn = new Button[6];
        for(int i=0;i<6;i++){
            wbtn[i] = (Button)findViewById(wb[i]);
        }

        Intent intent = getIntent();
        mB_id = intent.getIntExtra("b_id", 0);
        // 서버 데이터 가져옴
        networkService = RetrofitSender.getNetworkService();
        Call<List<Bookword>> call =  networkService.get_bw(mB_id);
//
        call.enqueue(new Callback<List<Bookword>>() {
                        @Override
                        public void onResponse(Call<List<Bookword>> call, Response<List<Bookword>> response) {
                            if (response.isSuccessful()) {
                                for(int i=0;i<6;i++) {
                                    Bookword bw = response.body().get(i);
                                    String en = bw.getB_word_e();
                                    String ko = bw.getB_word_k();
                                    String content = en+"\n"+ko;
                                    wbtn[i].setText(content);
                                }

                            }
                        }
            @Override
            public void onFailure(Call<List<Bookword>> call, Throwable t) {
                Log.d("tag", "onFailure: " + t.toString()); //서버와 연결 실패
            }
        });

        btnx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
} 
