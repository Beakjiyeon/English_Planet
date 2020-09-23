package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.yolo.setting.AppSetting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class modifyinfoActivity  extends AppCompatActivity {

    TextView mid;
    TextView mnickname;
    TextView mpwd;
    TextView mpwd2;
    Button mConfirm;
    NetworkService networkService;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyifo);

        Window w = getWindow();
        Navigation_Bar n = new Navigation_Bar();
        n.HideNavigationBar(w);

        //network
        networkService = RetrofitSender.getNetworkService();


        mid = findViewById(R.id.m_user_id);
        mnickname = findViewById(R.id.m_user_nickname);
        mpwd = findViewById(R.id.m_user_pwd);
        mpwd2 = findViewById(R.id.m_user_pwd2);
        mConfirm = findViewById(R.id.modify_enter);

        mid.setEnabled(false);
        mid.setText(AppSetting.uid);
        mnickname.setText(AppSetting.unickname);


        mConfirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                User user = new User();
                user.setUid(AppSetting.uid);
                user.setNickname(mnickname.getText().toString());
                user.setUpw(mpwd.getText().toString());
                user.setP_progress(AppSetting.progress);
                user.setBig_progress(AppSetting.big_progress);

                if (mpwd.getText().toString().equals(mpwd2.getText().toString())){
                    Call<User> response = networkService.patch_user(AppSetting.uid,user);
                    response.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            try{
                                if(response.isSuccessful()){
                                    Log.e("##############성공","성공");
                                    String msg = "회원정보가 변경되었습니다.";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                                    AppSetting.unickname = mnickname.getText().toString();
                                    AppSetting.upwd = mpwd.getText().toString();

                                    finish();

                                }

                                else {
                                    Log.e("########response isnt","실패1");


                                }} catch(Exception e){
                                Log.e("Error2",e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("########response isnt",t.getMessage());
                        }
                    });

                } else {
                    String msg = "비밀번호가 다릅니다.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    mpwd.setText("");
                    mpwd2.setText("");
                    mpwd.setFocusable(true);
                }





            }
        });
    }

}
