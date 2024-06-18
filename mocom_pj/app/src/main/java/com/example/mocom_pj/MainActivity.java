package com.example.mocom_pj;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText joinname, joinEmail, joinPassword, joinPwck;
    private Button checkname, checkid, joinButton, cancelButton;
    private boolean check, check2;
    private String checked, checked2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinname = findViewById(R.id.join_name);
        joinEmail = findViewById(R.id.join_email);
        checkname = findViewById(R.id.check_button1);
        checkid = findViewById(R.id.check_button2);
        joinPassword = findViewById(R.id.join_password);
        joinPwck = findViewById(R.id.join_pwck);
        joinButton = findViewById(R.id.join_button);
        cancelButton = findViewById(R.id.delete);
        check = false; // 아이디 값 중복 체크 여부
        checked = ".";
        check2 = false; // 닉네임 값 중복 체크 여부
        checked2 = ".";

        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://port-0-nodejsmocom-lxixg5056c8a8a72.sel5.cloudtype.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        checkname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = joinname.getText().toString();
                UserName user = new UserName(name);

                Call<ResponseBody> call = apiService.checkName(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // check 성공 처리
                            Toast.makeText(MainActivity.this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            check2 = true;
                            checked2 = name;

                        } else {
                            // 오류 처리
                            Toast.makeText(MainActivity.this, "이미 있는 닉네임이거나 사용할 수 없는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 네트워크 오류 등의 처리
                        Toast.makeText(MainActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        checkid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = joinEmail.getText().toString();
                UserId user = new UserId(email);

                Call<ResponseBody> call = apiService.checkUser(user);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // check 성공 처리
                            Toast.makeText(MainActivity.this, "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                            check = true;
                            checked = email;

                        } else {
                            // 오류 처리
                            Toast.makeText(MainActivity.this, "이미 있는 ID이거나 사용할 수 없는 ID입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 네트워크 오류 등의 처리
                        Toast.makeText(MainActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = joinname.getText().toString();
                String email = joinEmail.getText().toString();
                if (name.equals(checked2)&&check2) {
                    if (email.equals(checked)&&check) {
                        if (joinPassword.getText().toString().equals(joinPwck.getText().toString())) {
                            String password = joinPassword.getText().toString();
                            User user = new User(email, password, name);

                            // API 호출
                            Call<ResponseBody> call = apiService.signupUser(user);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        // 회원가입 성공 처리
                                        Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // 오류 처리
                                        Toast.makeText(MainActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 네트워크 오류 등의 처리
                                    Toast.makeText(MainActivity.this, "통신 오류", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(MainActivity.this, "비밀번호와 비밀번호확인이 서로 다릅니다. 다시 해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "ID 체크를 다시 해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "닉네임 체크를 다시 해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 취소 버튼 클릭 처리
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
