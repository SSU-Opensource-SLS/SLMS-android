package com.opensource.sls;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button joinButton;
    FragmentManager fragmentManager;
    FirstJoinFragment fragment1;
    SecondJoinFragment fragment2;
    boolean isLastFragment = false;

    UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        joinButton = findViewById(R.id.joinMemberButton);
        fragmentManager = getSupportFragmentManager();
        fragment1 = new FirstJoinFragment();
        fragment2 = new SecondJoinFragment();
        FragmentView(1);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLastFragment) {
                    FirstJoinFragment fragment = (FirstJoinFragment) fragmentManager.findFragmentById(R.id.fragment_container);
                    showNextFragment(fragment.getBirthText(), fragment.getLivestocks());
                } else {
                    SecondJoinFragment fragment = (SecondJoinFragment) fragmentManager.findFragmentById(R.id.fragment_container);
                    if(fragment.getPwd().equals(fragment.getCheckPwd())) {
                        createUser(fragment.getName(),fragment.getEmail(),fragment.getPwd());
                    } else {
                        Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void FragmentView(int fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (fragment) {
            case 1:
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }
    }

    public void showNextFragment(String birth, ArrayList<String> livestocks) {
        if(birth.equals("")) {
            Toast.makeText(getApplicationContext(),"생년월일을 선택해 주세요.", Toast.LENGTH_SHORT).show(); return;
        }
        userModel.setBirth(birth);
        if(livestocks == null || livestocks.size() == 0) {
            Toast.makeText(getApplicationContext(),"사육하는 가축의 종을 선택해 주세요.", Toast.LENGTH_SHORT).show(); return;
        }
        userModel.setLivestocks(livestocks);
        isLastFragment = true;
        joinButton.setText("회원가입");
        FragmentView(2);
    }

    public void createUser(String name, String email, String password) {
        // Exception to allow all Edittexts to be entered
        if (name.equals("")) { Toast.makeText(JoinActivity.this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show(); return; }
        if (email.equals("")) { Toast.makeText(JoinActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); return; }
        if (password.equals("")) { Toast.makeText(JoinActivity.this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show(); return; }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();     // UID(Unified ID) 생성
                            userModel.setName(name); userModel.setEmail(email);
                            db.collection("user").document(uid).set(userModel);
                            Toast.makeText(JoinActivity.this, "회원가입을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(JoinActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}