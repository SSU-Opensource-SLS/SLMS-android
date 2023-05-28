package com.opensource.sls;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.opensource.sls.DTO.CamQRDto;
import com.opensource.sls.DTO.InputLivestockDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectCamActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CamAdapter adapter;
    ArrayList<CamItem> camItems;
    Button connectButton;
    ImageView backButton;
    private static final OkHttpClient client = new OkHttpClient();

    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_cam);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.camRecyclerView);
        connectButton = findViewById(R.id.connect_cam_button);
        backButton = findViewById(R.id.cam_back);
        camItems = new ArrayList<CamItem>();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CamAdapter(getApplicationContext());
        //getCamDatas(adapter,camItems);
        recyclerView.setAdapter(adapter);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectCamDlg(adapter);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void getCamDatas(final CamAdapter adapter, final ArrayList<CamItem> camItems) {
        String url = "http://10.0.2.2:5000/cam/" + mAuth.getUid(); // Replace with your actual API URL
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
                        JSONArray camArray = new JSONArray(responseBody);
                        for (int i = 0; i < camArray.length(); i++) {
                            JSONObject camObject = camArray.getJSONObject(i);
                            String uid = camObject.getString("uid");
                            String livestockType = camObject.getString("livestock_type");
                            Long num = camObject.getLong("num");

                            CamItem camItem = new CamItem(uid,livestockType,num);
                            camItems.add(camItem);
                        }

                        // Update the adapter on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addItems(camItems);
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

    public void showConnectCamDlg(final CamAdapter adapter) {
        final String[] livestock_type = {""};

        View dlgView = View.inflate(ConnectCamActivity.this, R.layout.connect_cam_dlg, null);
        AlertDialog.Builder registerDlg = new AlertDialog.Builder(ConnectCamActivity.this);
        registerDlg.setTitle("캠 연결");
        registerDlg.setIcon(R.drawable.main_cow);
        registerDlg.setView(dlgView);
        final EditText connectWifi = dlgView.findViewById(R.id.connect_wifi);
        final EditText connectWifiPwd = dlgView.findViewById(R.id.connect_wifi_pwd);
        final ImageButton cowImageButton = dlgView.findViewById(R.id.connectCowImageButton);
        final ImageButton pigImageButton = dlgView.findViewById(R.id.connectPigImageButton);
        final ImageButton horseImageButton = dlgView.findViewById(R.id.connectHorseImageButton);
        final ImageButton goatImageButton = dlgView.findViewById(R.id.connectGoatImageButton);
        final ImageButton sheepImageButton = dlgView.findViewById(R.id.connectSheepImageButton);
        final View cowLayout = dlgView.findViewById(R.id.connectCowLayout);
        final View pigLayout = dlgView.findViewById(R.id.connectPigLayout);
        final View horseLayout = dlgView.findViewById(R.id.connectHorseLayout);
        final View goatLayout = dlgView.findViewById(R.id.connectGoatLayout);
        final View sheepLayout = dlgView.findViewById(R.id.connectSheepLayout);

        cowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livestock_type[0] = "cow";
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
                sheepLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
            }
        });

        horseLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
        registerDlg.setPositiveButton("QR 생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CamQRDto camQRDto = new CamQRDto(mAuth.getUid(),connectWifi.getText().toString(),connectWifiPwd.getText().toString(),livestock_type[0]);
                //Toast.makeText(getApplicationContext(),camQRDto.getLivestock_type(),Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(getApplicationContext(), ShowQR.class);
                intent.putExtra("QR_info",camQRDto);
                startActivity(intent);
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