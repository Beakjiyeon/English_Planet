package org.tensorflow.yolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.example.common.LoginActivity.UserLogin;

public class SignupActivity extends AppCompatActivity {
    EditText id;
    EditText pwd;
    EditText nick;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        networkService = RetrofitSender.getNetworkService();
         // post method
        Button btn = findViewById(R.id.signup_enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = findViewById(R.id.user_id);
                pwd = findViewById(R.id.user_pwd);
                nick = findViewById(R.id.user_nickname);
                Call<User> response = networkService.register_user(id.getText().toString(),pwd.getText().toString(),nick.getText().toString(),10,1);
                response.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        try{
                        if(response.isSuccessful()){
                            Log.e("##############성공","성공");
                            // TODO: 회원가입 이후 화면 전환
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        else {
                            Log.e("########response isnt","실패1");
                            EditText id = (EditText) findViewById(R.id.user_id);
                            EditText nick = (EditText) findViewById(R.id.user_nickname);
                            EditText pwd = (EditText) findViewById(R.id.user_pwd);

                            id.setText(""); nick.setText(""); pwd.setText("");
                            id.requestFocus();
                            String msg = "회원가입 실패";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        }} catch(Exception e){
                            Log.e("Error2",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("########response isnt",t.getMessage());
                    }
                });
            }
        });
    }
}
