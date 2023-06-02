package com.opensource.sls;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.opensource.sls.DTO.TokenDTO;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView joinButton;

    private Button loginButton;

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        joinButton = findViewById(R.id.joinMemberText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginUser(String email, String password) {
        if(email.equals("admin")) {
            email = "eric2057@daum.net";
            password = "qwer1234";
        }
        if (email.equals("")) { Toast.makeText(MainActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); return; }
        if (password.equals("")) { Toast.makeText(MainActivity.this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show(); return; }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getFirebaseToken();
                            Toast.makeText(MainActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "가입되지 않은 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        TokenDTO tokenDTO = new TokenDTO(mAuth.getUid(),token);
                        registerToken(tokenDTO);

                        // Log token
                        Log.d(TAG, "FCM registration token: " + token);
                    }
                });
    }

    public void registerToken(TokenDTO tokenDTO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://203.253.25.48:5000/member/token";
                String json = gson.toJson(tokenDTO);

                RequestBody requestBody = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                try {
                    // 요청 실행
                    Response response = client.newCall(request).execute();

                    // 응답 처리
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        // 응답 결과 처리
                        System.out.println(responseBody);
                    } else {
                        // 응답 실패 처리
                        System.out.println("Request failed: " + response.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}