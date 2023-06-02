package com.opensource.sls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowPost extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ListView listView;
    CommentAdapter adapter;
    EditText new_comment;
    TextView show_post_title;
    TextView show_post_writer;
    TextView show_post_date;
    TextView show_post_content;
    TextView commentCount;
    CheckBox annoymity_comment;
    int commentNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        PostItem postItem = (PostItem) intent.getSerializableExtra("item");
        //String pType = intent.getStringExtra("type");

        ImageButton delete_post_button = findViewById(R.id.delete_post_button);
        ImageButton revise_post_button = findViewById(R.id.revise_post_button);
        ImageButton add_comment_button = findViewById(R.id.add_comment_button);
        TextView postingType = findViewById(R.id.posting_type);
        commentCount = findViewById(R.id.comment_count);
        show_post_title = findViewById(R.id.show_post_title);
        show_post_writer = findViewById(R.id.show_post_writer);
        show_post_date = findViewById(R.id.show_post_date);
        show_post_content = findViewById(R.id.show_post_content);
        new_comment = findViewById(R.id.new_comment);
        annoymity_comment = findViewById(R.id.annoymity_checkBox);

        show_post_title.setText(postItem.getTitle());
        //postingType.setText(pType);
        listView = findViewById(R.id.comments);
        adapter = new CommentAdapter();

        getCommentDatas(postItem.getPost_key());
        listView.setAdapter(adapter);

        if(!postItem.isAnnoymity())
            show_post_writer.setText(postItem.getWriter());
        else
            show_post_writer.setText("Anonymous");
        show_post_date.setText(postItem.getDate());
        show_post_content.setText(postItem.getContent());

        if(!user.getEmail().equals(postItem.getWriter())) {
            delete_post_button.setVisibility(View.INVISIBLE);
            revise_post_button.setVisibility(View.INVISIBLE);
        }

        delete_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("posts").child(postItem.getPost_key());
                mDatabase.removeValue();
                onBackPressed();
            }
        });

        revise_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), RevisePost.class);
                intent.putExtra("item",postItem);
                finish();
                startActivity(intent);*/
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CommentItem item = (CommentItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getWriter() + item.getContent(), Toast.LENGTH_SHORT).show();
            }
        });

        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(postItem.getPost_key());
            }
        });
    }

    class CommentAdapter extends BaseAdapter {
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CommentItem item) {
            items.add(item);
        }

        public void deleteItem(CommentItem item) { items.remove(item); }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            CommentItemView commentItemView = null;
            if(view == null) {
                commentItemView = new CommentItemView(getApplicationContext());
            } else {
                commentItemView = (CommentItemView) view;
            }
            CommentItem item = items.get(position);
            commentItemView.setWriter(item.getWriter(),item.isAnnoymity());
            commentItemView.setDate(item.getDate());
            commentItemView.setContent(item.getContent());

            Button delete_comment_button = commentItemView.findViewById(R.id.delete_comment_button);
            if(!user.getEmail().equals(item.getWriter())) delete_comment_button.setVisibility(View.INVISIBLE);
            delete_comment_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    show_dialog_delete_comment(item);
                }
            });
            return commentItemView;
        }
    }

    class CommentItemView extends LinearLayout {
        TextView comment_writer;
        TextView comment_date;
        TextView comment_content;
        public CommentItemView(Context context) {
            super(context);
            init(context);
        }

        public CommentItemView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }
        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.comment_item, this, true);

            comment_writer = findViewById(R.id.comment_writer);
            comment_date = findViewById(R.id.comment_date);
            comment_content = findViewById(R.id.comment_content);
        }
        public void setWriter(String email, boolean annoymity) {
            if(annoymity)
                comment_writer.setText("Anonymous");
            else
                comment_writer.setText(email);
        }
        public void setDate(String date) {
            comment_date.setText(date);
        }
        public void setContent(String content) {
            comment_content.setText(content);
        }
    }

    public void getCommentDatas(String post_key) {
        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("comments").child(post_key);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    CommentItem item = child.getValue(CommentItem.class);
                    adapter.addItem(item);
                    commentNum++;
                }
                adapter.notifyDataSetChanged();
                commentCount.setText("댓글 " + commentNum + "개");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addComment(String post_key) {
        String comment = new_comment.getText().toString();
        String date = getDate();
        boolean annoymity = annoymity_comment.isChecked();
        CommentItem item = new CommentItem("1",post_key,user.getEmail(),comment,date,"0",annoymity);

        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("comments").child(post_key);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int idx = 1;
                for(DataSnapshot child : snapshot.getChildren()) {
                    if(!child.getKey().equals(Integer.toString(idx))) {
                        break;
                    }
                    idx++;
                }
                item.setComment_key(Integer.toString(idx));
                mDatabase.child(item.getComment_key()).setValue(item);
                adapter.addItem(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    private void show_dialog_delete_comment(CommentItem item) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(ShowPost.this);
        dlg.setTitle("삭제 하시겠습니까?");
        dlg.setMessage(item.getContent() + "\n댓글을 삭제하시겠습니까?");
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete_comment(item);
                Toast.makeText(getApplicationContext(), "삭제 되었습니다", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        dlg.show();
    }

    private void delete_comment(CommentItem item) {
        mDatabase = FirebaseDatabase.getInstance().getReference("communityData").child("comments").child(item.getPost_key()).child(item.getComment_key());
        mDatabase.removeValue();
        adapter.deleteItem(item);
        adapter.notifyDataSetChanged();
    }
}