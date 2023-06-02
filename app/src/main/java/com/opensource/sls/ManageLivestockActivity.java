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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.opensource.sls.DTO.InputLivestockDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManageLivestockActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private Button registerButton;
    LinearLayoutManager layoutManager;
    LivestockAdapter adapter;
    ImageView backButton;
    ArrayList<LivestockItem> livestockItems;

    private static final OkHttpClient client = new OkHttpClient();

    private static final Gson gson = new Gson();

    InputLivestockDTO inputLivestockDTO[] = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_livestock);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.livestockRecyclerView);
        registerButton = findViewById(R.id.register_livestock_button);
        backButton = findViewById(R.id.livestock_back);
        livestockItems = new ArrayList<LivestockItem>();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LivestockAdapter(getApplicationContext());
        getLivestockDatas(adapter);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter.setOnItemLongClickListener(new LivestockAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(LivestockAdapter.ViewHolder holder, View view, int position) {
                LivestockItem item = adapter.getItem(position);
                showRemoveLivestock(adapter,item);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterLivestock(adapter);
                /*showRegisterLivestock(adapter, new OnRegisterLivestockListener() {
                    @Override
                    public void onLivestockRegistered(InputLivestockDTO livestockDTO) {
                        //getLastLivestock(adapter, livestockDTO);
                    }å
                });*/
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public void registerLivestock(InputLivestockDTO item) {
        //Toast.makeText(getApplicationContext(),item.getLivestock_type()+item.getName(),Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://203.253.25.48:5000/livestock";
                String json = gson.toJson(item);

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

    public void deleteLivestock(LivestockItem livestockItem) {
        // 요청 URL 생성
        String url = "http://203.253.25.48:5000/livestock/" + livestockItem.getUid() + "/" +
                livestockItem.getLivestock_type() + "/" + livestockItem.getNum();

        // DELETE 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        // 요청 실행
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    // 삭제 성공
                    try {
                        String result = response.body().string();
                        // 처리할 로직 작성
                    } catch (IOException e) {
                        // 예외 처리
                        e.printStackTrace();
                    }
                    // 처리 결과를 이용하여 필요한 작업 수행
                } else {
                    // 삭제 실패
                    // 오류 처리
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 통신 실패
                // 오류 처리
            }
        });
    }

    public void getLivestockDatas(final LivestockAdapter adapter) {
        ArrayList<LivestockItem> livestockItems = new ArrayList<LivestockItem>();
        String url = "http://203.253.25.48:5000/livestock/" + mAuth.getUid(); // Replace with your actual API URL
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray livestockArray = new JSONArray(responseBody);
                        for (int i = 0; i < livestockArray.length(); i++) {
                            JSONObject livestockObject = livestockArray.getJSONObject(i);
                            String uid = livestockObject.getString("uid");
                            String livestockType = livestockObject.getString("livestock_type");
                            Long num = livestockObject.getLong("num");
                            String name = livestockObject.getString("name");
                            String cattle = livestockObject.getString("cattle");
                            Number is_pregnancy = 0;
                            if (!livestockObject.isNull("is_pregnancy")) {
                                is_pregnancy = livestockObject.getInt("is_pregnancy");
                            }
                            LivestockItem livestockItem = new LivestockItem(uid, livestockType, num, name, cattle, is_pregnancy);
                            System.out.println(livestockItem.getName());
                            livestockItems.add(livestockItem);
                        }

                        // Update the adapter on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addItems(livestockItems);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }

    public void showRemoveLivestock(final LivestockAdapter adapter, LivestockItem item) {
        AlertDialog.Builder deleteDlg = new AlertDialog.Builder(this);
        deleteDlg.setTitle(item.getName() + "를(을) 삭제 하시겠습니까?");
        deleteDlg.setIcon(R.drawable.main_cow);
        deleteDlg.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogIn제erface, int i) {
                deleteLivestock(item);
                adapter.deleteItem(item);
                adapter.notifyDataSetChanged();
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

    public void showRegisterLivestock(final LivestockAdapter adapter) {
        final String[] livestock_type = {""};
        //final AtomicReference<String> finalLivestockType = new AtomicReference<>(livestock_type);

        View dlgView = View.inflate(ManageLivestockActivity.this, R.layout.register_livestock_dlg, null);
        AlertDialog.Builder registerDlg = new AlertDialog.Builder(ManageLivestockActivity.this);
        registerDlg.setTitle("가축 등록");
        registerDlg.setIcon(R.drawable.main_cow);
        registerDlg.setView(dlgView);
        final EditText livestockName = dlgView.findViewById(R.id.register_livestock_name);
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

        cowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "cow";
                //finalLivestockType.set("cow");
                cowLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        pigImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "pig";
                //finalLivestockType.set("pig");
                pigLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        horseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "horse";
                //finalLivestockType.set("horse");
                horseLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        goatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "goat";
                //finalLivestockType.set("goat");
                goatLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        sheepImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "sheep";
                //finalLivestockType.set("sheep");
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        registerDlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InputLivestockDTO item = new InputLivestockDTO(mAuth.getUid(), livestock_type[0], livestockName.getText().toString(), livestockCattle.getText().toString());
                inputLivestockDTO[0] = item;
                registerLivestock(item);

                // 완료 콜백을 호출하여 getLastLivestock 함수를 호출
                //listener.onLivestockRegistered(item);
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