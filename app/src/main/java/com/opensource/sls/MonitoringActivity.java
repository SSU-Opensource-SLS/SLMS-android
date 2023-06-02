package com.opensource.sls;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonitoringActivity extends AppCompatActivity {
    private VideoView videoView;
    private OkHttpClient client;
    private TextView livestockName;
    private TextView livestockPosition;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        videoView = findViewById(R.id.videoView);
        livestockName = findViewById(R.id.mornitored_name);
        livestockPosition = findViewById(R.id.mornitored_position);
        backButton = findViewById(R.id.mornitoring_back);
        client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://203.253.25.48:5000/stream")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String videoUrl = response.request().url().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playVideo(videoUrl);
                        }
                    });
                }
            }
        });

        //setLivestockInfo();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConnectCamActivity.class);
                startActivity(intent);
            }
        });
    }

    private void playVideo(String videoUrl) {
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    /*void setLivestockInfo() {
        // 가축 정보 요청하는 코드 작성
        RequestQueue queue = Volley.newRequestQueue(this);
        String livestockUrl = "http://10.0.2.2:5000/livestock";

        JsonObjectRequest livestockRequest = new JsonObjectRequest(Request.Method.GET, livestockUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 가축 정보 추출
                            //String type = response.getString("type");
                            String name = response.getString("name");
                            String position = response.getString("Position");

                            // UI 업데이트
                            //livestockType.setImageResource(getImageResource(type));
                            livestockName.setText(name);
                            livestockPosition.setText(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(livestockRequest);
    }*/
}