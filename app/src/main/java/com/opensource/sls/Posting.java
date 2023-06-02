package com.opensource.sls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Posting extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText postTitle;
    private EditText postContent;
    private CheckBox annoymity_checkBox;
    private Button posting_button;
    private Toolbar toolbar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        postTitle = findViewById(R.id.show_post_title);
        postContent = findViewById(R.id.postContent);
        annoymity_checkBox = findViewById(R.id.annoymity_checkBox);
        posting_button = findViewById(R.id.posting_button);
        backButton = findViewById(R.id.posting_back);

        posting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = postTitle.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String user_email = user.getEmail();
                String content = postContent.getText().toString();
                String date = getDate();
                boolean is_annoymity = annoymity_checkBox.isChecked();
                PostItem postItem = new PostItem("1", title, user_email, content, date, "0", "Common", is_annoymity);
                savePostData(postItem);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });
    }
    private String getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);
        return getTime;
    }

    public void savePostData(PostItem postItem) {
        if (postItem.getTitle().equals("")) {
            Toast.makeText(this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (postItem.getContent().equals("")) {
            Toast.makeText(this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("posts");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int idx = 1;
                for (DataSnapshot child : snapshot.getChildren())
                    idx = Integer.parseInt(child.getKey());
                idx++;
                postItem.setPost_key(Integer.toString(idx));
                mDatabase.child(postItem.getPost_key()).setValue(postItem)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // 데이터 저장이 완료된 후 화면 전환 수행
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                                    startActivity(intent);
                                } else {
                                    // 데이터 저장 실패 처리
                                    Toast.makeText(getApplicationContext(), "데이터 저장 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}