package com.opensource.sls;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.opensource.sls.databinding.ActivityMonitoringBinding;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class MonitoringActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        videoView = (VideoView) findViewById(R.id.video_view);

        String videoUrl = "http://address.com/video";
        // Volley 라이브러리를 이용해 서버에서 비디오 스트림을 요청하고, 요청 결과를 받는 코드
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        VideoStreamRequest videoRequest = new VideoStreamRequest(videoUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // 서버로부터 비디오 스트림을 성공적으로 받았을 때 실행될 코드
                        videoView.setVideoURI(Uri.parse(videoUrl));
                        videoView.start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // 서버로부터 비디오 스트림을 받지 못했을 때 실행될 코드
                        Log.e("VideoStreamRequest", "Error loading video stream: " + volleyError.getMessage());
                    }
                });
        // 비디오 스트림 요청 큐에 추가
        requestQueue.add(videoRequest);
    }

    /*private void downloadVideo(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url) // 다운로드할 영상의 URL을 설정
                .build();
        client.newCall(request).enqueue(new Callback() {  // OkHttp3를 사용하여 요청
            @Override
            public void onFailure(Call call, IOException e) {
                // 실패 시 처리하는 로직을 작성
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String fileName = "video.mp4";
                    final File file = new File(getApplicationContext().getExternalFilesDir(null), fileName); // 저장할 파일의 경로와 이름을 설정
                    BufferedSink sink = null;
                    BufferedSource source = null;
                    try {
                        sink = Okio.buffer(Okio.sink(file));
                        source = Okio.buffer(response.body().source()); // 받은 데이터를 파일에 기록
                        sink.writeAll(source);
                        sink.flush();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() { // 파일에서 비디오를 재생
                                Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "video/mp4");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(intent);
                            }
                        });
                    } catch (Exception e) {
                        // 예외가 발생했을 때 처리하는 로직
                    } finally {
                        if (sink != null) {
                            sink.close();
                        }
                        if (source != null) {
                            source.close();
                        }
                    }
                } else {
                    // 응답이 실패했을 때 처리하는 로직을 작성
                }
            }
        });
    }*/
}