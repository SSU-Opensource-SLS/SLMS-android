package com.opensource.sls;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ManageLivestockActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private Button registerButton;
    LinearLayoutManager layoutManager;
    LivestockAdapter adapter;
    ArrayList<LivestockItem> livestockItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_livestock);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.livestockRecyclerView);
        registerButton = findViewById(R.id.register_livestock_button);
        livestockItems = new ArrayList<LivestockItem>();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LivestockAdapter(getApplicationContext());
        LivestockItem item1 = new LivestockItem(1L,"asfaa","누렁이","cow","1번 축사",true);
        adapter.addItem(item1);
        LivestockItem item2 = new LivestockItem(2L,"asfaa","얼룩이","cow","2번 축사",false);
        adapter.addItem(item2);
        //getLivestockDatas(adapter,livestockItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LivestockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LivestockAdapter.ViewHolder holder, View view, int position) {
                LivestockItem item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"아이템 선택됨 : " + item.getName(), Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent(getApplicationContext(), ShowLivestockInfo.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new LivestockAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(LivestockAdapter.ViewHolder holder, View view, int position) {
                LivestockItem item = adapter.getItem(position);
                showRemoveLivestock(item.getName());
                adapter.deleteItem(item);
                adapter.notifyDataSetChanged();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"sdsafa",Toast.LENGTH_SHORT).show();
                showRegisterLivestock();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public void registerLivestock(LivestockItem item) {
    }
    public void getLivestockDatas(LivestockAdapter adapter, ArrayList<LivestockItem> studentItems) {
    }
    public void showRemoveLivestock(String removeName) {
        AlertDialog.Builder deleteDlg = new AlertDialog.Builder(this);
        deleteDlg.setTitle(removeName + "를(을) 삭 하시겠습니까?");
        deleteDlg.setIcon(R.drawable.main_cow);
        deleteDlg.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogIn제erface, int i) {
                // 가축 삭제 함수 호출
            }
        });
        deleteDlg.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();;
            }
        });
        deleteDlg.show();
    }

    public void showRegisterLivestock() {
        View dlgView = View.inflate(ManageLivestockActivity.this, R.layout.register_livestock, null);
        AlertDialog.Builder registerDlg = new AlertDialog.Builder(ManageLivestockActivity.this);
        registerDlg.setTitle("가축 등록");
        registerDlg.setIcon(R.drawable.main_cow);
        registerDlg.setView(dlgView);
        final EditText livestcokName = dlgView.findViewById(R.id.register_livestock_name);
        final EditText livestockCattle = dlgView.findViewById(R.id.register_livestock_cattle);
        final ImageButton cowImageButton = dlgView.findViewById(R.id.registerCowImageButton);
        final ImageButton pigImageButton = dlgView.findViewById(R.id.registerPigImageButton);
        final ImageButton horseImageButton = dlgView.findViewById(R.id.registerHorseImageButton);
        final ImageButton goatImageButton = dlgView.findViewById(R.id.registerGoatImageButton);
        final ImageButton sheepImageButton = dlgView.findViewById(R.id.registerSheepImageButton);
        final View cowLayout = dlgView.findViewById(R.id.registerCowLayout);
        final View pigLayout = dlgView.findViewById(R.id.registerPigLayout);
        final View horseLayout = dlgView.findViewById(R.id.registerHorseLayout);
        final View goatLayout = dlgView.findViewById(R.id.registerGoatLayout);
        final View sheepLayout = dlgView.findViewById(R.id.registerSheepLayout);
        horseLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
        registerDlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        registerDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        registerDlg.show();
    }
}