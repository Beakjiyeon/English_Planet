package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;


import org.tensorflow.yolo.setting.AppSetting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    static NetworkService networkService;
    static boolean tf=false; // 로그인 함수

    public static boolean UserLogin(final String id, final String pwd){
        networkService = RetrofitSender.getNetworkService();

        Call<User> response = networkService.get_pk_user(id);
        response.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    User u = response.body();
                    System.out.println("###############"+u.getUid());
                    if((id.equals(u.getUid()))&&pwd.equals(u.getUpw())){
                        System.out.println("debug1");
                        tf=true;
                        System.out.print("#############");
                        System.out.println(tf);
                        AppSetting.uid = id;
                        AppSetting.upwd = pwd;
                        AppSetting.unickname = u.getNickname();
                        AppSetting.progress = u.getP_progress();
                    } else {

                        //tf = false;
                    }
                }
                else { //tf = false;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("#########Fail:", t.getMessage());
                tf = false;
            }

        });
        return tf;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_login);

        // 네이게이션 바
        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        // 세영 : 커서 포커스
        final EditText mName = (EditText) findViewById(R.id.user_id);
        final EditText mPwd = (EditText) findViewById(R.id.user_pwd);

        // 주희 : 이름 입력하면 다음 페이지로 넘어감 (행성 리스트)
        Button enter_btn = (Button) findViewById(R.id.enter_btn);

        // TODO : 로그인
        // 아이디 존재하는지
        // 아이디랑 비밀번호 값이 일치하는지 
        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("TTTTTTT");
                boolean loginstatus = UserLogin(mName.getText().toString(),mPwd.getText().toString());
                System.out.print("login status debug:");
                System.out.println(loginstatus);
                if (loginstatus){
                Intent intent = new Intent(getApplicationContext(), PlanetListActivity.class);
                startActivity(intent);
                finish();
            } else {
                    mName.setText("");
                    mPwd.setText("");
                    mName.requestFocus();
                    String msg = "로그인 실패";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    }


