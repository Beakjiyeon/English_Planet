package org.tensorflow.yolo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends Activity {

    // iterator
    Iterator<String> mItr;
    ArrayList<String> mBookList;
    NetworkService networkService;
    Button mBtn;

    TextView mT1;
    TextView mT2;
    TextView mT3;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);

        setContentView(R.layout.activity_book);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        mBtn = findViewById(R.id.btn_next); // 다음페이지 버튼

        // textview
        mT1 = findViewById(R.id.book_text1);
        mT2 = findViewById(R.id.book_text2);
        mT3 = findViewById(R.id.book_text3);

        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<Book> response = networkService.get_book(1);
        response.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                Book b = (Book) response.body();
                String[] b_text = b.getB_text().split("\r\n");
                mBookList = new ArrayList<String>(Arrays.asList(b_text));
                mItr = mBookList.iterator();

                // book iterator
                BookItr();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });

    }

    public void btnNext(View v) {
        BookItr();

    }

    private void BookItr() {
        try {
            String s = "";
            mT1.setText("");
            mT2.setText("");
            mT3.setText("");

            if (mItr.hasNext()) {
                s = mItr.next();
                mT1.setText(s);

                s = mItr.next();
                mT2.setText(s);

                s = mItr.next();
                mT3.setText(s);
            }

        } catch (NoSuchElementException ne) {
            Toast t = Toast.makeText(this.getApplicationContext(), "마지막 페이지입니다.", Toast.LENGTH_SHORT);
            // 지연 : progressBar 진행상황 디비 수정
            // AppSetting 값 수정 db에서 받아오는데 시간이 걸리기떄문...
            AppSetting.progress=1;
            AppSetting.dp_bool=true;
            // 쳅터 1의 시작=0, 동화=1
            // db에 값 반영
            updateProgressDB();
            Log.d("널체크","동화엔 "+AppSetting.uid);
            Intent intent = new Intent(getApplicationContext(), PlanetActivity1.class);
            startActivity(intent);
            t.show();
            finish();


        }

    }
    // 지연: DB 진행률을 수정하는 함수
    void updateProgressDB(){
        networkService = RetrofitSender.getNetworkService();
        // b_id : 1번으로 설정
        Call<ResponseBody> response2 = networkService.updateProgress(AppSetting.uid,1);
        response2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response!=null) {
                    Log.d("ㅋㅋㅋ#", "수정하고싶다");


                }else{
                    Log.d("ㅋㅋㅋ#", "ㄴㄴㄴxxxxxxx");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ㅋㅋㅋ#", t.getMessage());
            }
        });
    }
}


