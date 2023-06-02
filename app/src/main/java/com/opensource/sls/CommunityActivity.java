package com.opensource.sls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    ArrayList<PostItem> postItems;
    ImageView addPost;
    Button commonButton;
    Button noticeButton;
    ImageView backButton;
    String postingType = "공통 게시판";
    public static Context context_community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        mAuth = FirebaseAuth.getInstance();
        context_community = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addPost = findViewById(R.id.addPost);
        commonButton = findViewById(R.id.post_button);
        noticeButton = findViewById(R.id.notice_button);
        commonButton.setOnClickListener(postType);
        noticeButton.setOnClickListener(postType);
        backButton = findViewById(R.id.community_back);
        recyclerView = findViewById(R.id.community_recyclerView);
        postItems = new ArrayList<PostItem>();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.gray_line));
        recyclerView.addItemDecoration(dividerItemDecoration);

        postAdapter = new PostAdapter(getApplicationContext());

        recyclerView.setAdapter(postAdapter);
        getPostCommonDatas(postAdapter,postItems);
        postAdapter.notifyDataSetChanged();

        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostAdapter.ViewHolder holder, View view, int position) {
                PostItem item = postAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),"아이템 선택됨 : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent(getApplicationContext(), ShowPost.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Posting.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    View.OnClickListener postType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_button:
                    commonButton.setTextColor(Color.parseColor("#FFFFFF"));
                    noticeButton.setTextColor(Color.parseColor("#BBBBBB"));
                    commonButton.setEnabled(false);
                    noticeButton.setEnabled(true);
                    commonButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
                    noticeButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
                    getPostCommonDatas(postAdapter,postItems);
                    postingType = "Common";
                    Toast.makeText(getApplicationContext(), "공통 게시판입니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.notice_button:
                    commonButton.setTextColor(Color.parseColor("#BBBBBB"));
                    noticeButton.setTextColor(Color.parseColor("#FFFFFF"));
                    commonButton.setEnabled(true);
                    noticeButton.setEnabled(false);
                    commonButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
                    noticeButton.setBackgroundTintList(getResources().getColorStateList(R.color.button_color));
                    getPostRoleDatas(postAdapter,postItems);
                    postingType = "Notice";
                    Toast.makeText(getApplicationContext(), "공지 게시판입니다.", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    public void getPostCommonDatas(PostAdapter adapter, ArrayList<PostItem> postItems) {
        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("posts");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clearItem();
                postItems.clear();
                for(DataSnapshot child : snapshot.getChildren()) {
                    PostItem postItem = child.getValue(PostItem.class);
                    if(postItem.getPostType().equals("Common")) {
                        postItems.add(postItem);
                        adapter.addItem(postItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getPostRoleDatas(PostAdapter adapter, ArrayList<PostItem> postItems) {
        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("posts");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clearItem();
                postItems.clear();
                for(DataSnapshot child : snapshot.getChildren()) {
                    PostItem postItem = child.getValue(PostItem.class);
                    if(postItem.getPostType().equals("Notice")) {
                        postItems.add(postItem);
                        adapter.addItem(postItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}