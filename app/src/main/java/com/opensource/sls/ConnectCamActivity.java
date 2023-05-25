package com.opensource.sls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

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
        getCamDatas(adapter,camItems);
        recyclerView.setAdapter(adapter);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}