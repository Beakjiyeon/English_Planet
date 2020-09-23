package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraQuizActivity extends AppCompatActivity {

    TextView wordtv;
    ImageView ivans1 ;
    ImageView ivans2;
    ImageView ivans3;
    ImageView cimage;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);
        setContentView(R.layout.activity_camera_quiz);

        wordtv = (TextView) findViewById(R.id.word);
        ivans1 = (ImageView)findViewById(R.id.wiv1);
        ivans2 = (ImageView)findViewById(R.id.wiv2);
        ivans3 = (ImageView)findViewById(R.id.wiv3);
        cimage=(ImageView)findViewById(R.id.cimage);

        tv1= (TextView)findViewById(R.id.wtv1);
        tv2= (TextView)findViewById(R.id.wtv2);
        tv3= (TextView)findViewById(R.id.wtv3);
        // back btn
        Button btn_back = findViewById(R.id.btn_back);
        Intent intent0=null;

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 1. 서버에서 카메라 단어장 전체를 불러오기
        // 2. Map<영어,camera객체>로 저장하므로써 영어 중복 단어 지우기
        // 3. 랜덤으로 3개의 객체 고르기
        //    AppSetting.cq1_e=apple AppSetting.cq1_k=사과 로 저장
        // 4. 사진 불러오기
        // 4-1. 이 3개 객체 이외의 객체들중 영어 뜻을 띄우기
        // 5. 답을 선택하면 버튼 text와 AppSetting.cq1_e 비교
        // 6. 정답일 경우, 정답 front 오답일 경우, 오답 프론트와 AppSetting.cq_e는 k입니다
        // 1.
        NetworkService networkService = RetrofitSender.getNetworkService();

        Call<List<Camera>> response = networkService.get_Cameras();
        response.enqueue(new Callback<List<Camera>>() {
            @Override
            public void onResponse(Call<List<Camera>> call, Response<List<Camera>> response) {
                if(response!=null) {
                    List<Camera> list = response.body();
                    //2.
                    LinkedHashMap<String,Camera> map=new LinkedHashMap<String,Camera>();
                    int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
                    for (int index = 0; index < totalElements; index++) {
                        //if(list.get(index).getUid().equals(AppSetting.uid)) {
                            if(true){
                            Log.d("########", list.get(index).getC_word_e() + "/" + list.get(index).getC_word_k());
                            map.put(list.get(index).getC_word_e(),list.get(index));
                        }
                    }
                    Log.d("#####","2완료");
                    // 3. 중복이 제거된 맵을 다시 list로 변환
                    ArrayList<Camera> list2=new ArrayList<Camera>();
                    for (String key : map.keySet()) {
                        Log.d("#####",key + " : "+ map.get(key));
                        list2.add(map.get(key));
                    }
                    Log.d("#####","3완료");
                    // 3.
                    // 3-1. list에서 0~2까지 뽑기
                    //if(AppSetting.cq_count==-1){//&&list2.size()>=3){
                        AppSetting.cqlist=list2;
                        Collections.shuffle(list2);
                        // Math.ramdom이용
                        int a;
                        int n1=0,n2=0,n3=0;
                        Random r = new Random(); //객체생성
                        for(int i=1;i<=3;i++) //5개의 숫자를 출력하기 위하여 for문을 사용하여 5번 반복실행
                        {
                            a = r.nextInt(list2.size());
                            /*nextInt(90)은 0~89까지 숫자중 랜덤함수 발생이므로 +11을 더해줘서 11~100까지의
                                숫자중 하나를 뽑아 변수 a에 값을 저장한다 */
                           if(i==1){n1=a;}
                            if(i==2){n2=a;}
                            if(i==3){n3=a;}
                        }

                        AppSetting.cq1=list2.get(n1);
                        AppSetting.cq2=list2.get(n2);
                        AppSetting.cq3=list.get(n3);
                        //AppSetting.cq_count=0;

                        Log.d("#####","3-1if완료"+AppSetting.cq1.getC_word_e());
                        Log.d("#####","3-1if완료"+AppSetting.cq2.getC_word_e());
                        Log.d("#####","3-1if완료"+AppSetting.cq3.getC_word_e());
                /*
                }else{
                        Log.d("#####","현재"+AppSetting.cq_count);
                    }
*/
                    Log.d("#####","3-1완료 앱새팅"+AppSetting.cq_count);
                    // 4.
                    Camera img_word=null;


                    img_word=AppSetting.cq1;/*
                    if(AppSetting.cq_count==-1) { //처음 미션 들어온 경우, 데이터 처리 필요
                        Toast.makeText(getApplicationContext(), "데이터 처리가 필요합니다", Toast.LENGTH_LONG).show();
                    }else if(AppSetting.cq_count==0) { // 첫번째 띄워줘야함
                        img_word=AppSetting.cq1;
                        Toast.makeText(getApplicationContext(), "데이터 처리를 완료했습니다.", Toast.LENGTH_LONG).show();
                    }else if(AppSetting.cq_count==1){
                        img_word=AppSetting.cq2;
                    }else if(AppSetting.cq_count==2){
                        img_word=AppSetting.cq3;
                    }else if(AppSetting.cq_count==3){
                        // 퀴즈 종료
                        // planet1Activity로 이동
                    }
                    */

                    if(img_word!=null) {
                        Log.d("#####","img_word널이니"+img_word.getC_word_e());
                        Glide
                                .with(getApplicationContext())
                                .load(img_word.getC_url())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.star)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .fitCenter())
                                .into(cimage);

                        // 4-1. 정답 TEXT와 오답 텍스트 띄우기
                        ArrayList<Camera> temp=new ArrayList<Camera>();
                        for(int i=0;i<AppSetting.cqlist.size();i++){
                            if(!AppSetting.cqlist.get(i).getC_word_e().equals(img_word.getC_word_e())){
                                temp.add(AppSetting.cqlist.get(i));
                            }

                        }
                        // 4-2. temp 중에서 랜덤으로 2개 뽑기
/*
                        // Math.ramdom이용
                        double randomValue = Math.random();
                        int ran = (int)(randomValue * temp.size()) -1;
                        Camera ex1 = temp.get(ran);
                        temp.remove(ran);
                        int ran2 = (int)(randomValue * temp.size()) -1;
                        Camera ex2 = temp.get(ran2);
                        temp.remove(ran2);
*/
                        // 4-3. 이미지 뷰에 텍스트를 배열하기 위한 맵 생성
                        ArrayList<Camera> shuffle=new ArrayList<Camera>();
                        shuffle.add(temp.get(0));
                        shuffle.add(temp.get(1));
                       // shuffle.add(AppSetting.cqlist.get(1));
                        shuffle.add(img_word);
                        // 4-4. 순서 섞기
                        Collections.shuffle(shuffle);

                        tv1.setText(shuffle.get(0).getC_word_e());
                        tv2.setText(shuffle.get(1).getC_word_e());
                        tv3.setText(shuffle.get(2).getC_word_e());

                        // 5. 정답처리 로직
                        Camera finalImg_word = img_word;
                        tv1.setOnClickListener(view -> {
                            Toast t = Toast.makeText(getApplicationContext(), tv1.getText()+"를 고르셨습니다.", Toast.LENGTH_SHORT);
                            t.show();
                            Intent intent = new Intent(getApplicationContext(),CameraQuizAnswerActivity.class);
                            intent.putExtra("cquiz_answer", finalImg_word);
                            intent.putExtra("clicked_word",tv1.getText());
                            intent.putExtra("b_id",1);
                            startActivity(intent);
                        });
                        tv2.setOnClickListener(view -> {
                            Toast t = Toast.makeText(getApplicationContext(), tv2.getText()+"를 고르셨습니다.", Toast.LENGTH_SHORT);
                            t.show();
                            Intent intent = new Intent(getApplicationContext(),CameraQuizAnswerActivity.class);
                            intent.putExtra("cquiz_answer", finalImg_word);
                            intent.putExtra("clicked_word",tv2.getText());
                            intent.putExtra("b_id",1);
                            startActivity(intent);
                        });
                        tv3.setOnClickListener(view -> {
                            Toast t = Toast.makeText(getApplicationContext(), tv3.getText()+"를 고르셨습니다.", Toast.LENGTH_SHORT);
                            t.show();
                            Intent intent = new Intent(getApplicationContext(),CameraQuizAnswerActivity.class);
                            intent.putExtra("cquiz_answer", finalImg_word);
                            intent.putExtra("clicked_word",tv3.getText());
                            intent.putExtra("b_id",1);
                            startActivity(intent);
                        });
                    }else{
                        Log.d("#####","#####글라이드:"+"널이다");
                    }

                }else{
                    Log.d("########", "xxxxxxx");
                }
                Log.d("########", "xxxㅇㅇxxxx");
            }

            @Override
            public void onFailure(Call<List<Camera>> call, Throwable t) {
                Log.d("########", t.getMessage());
            }
        });



    }




}
