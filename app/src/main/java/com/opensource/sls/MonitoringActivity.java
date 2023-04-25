package com.opensource.sls;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MonitoringActivity extends AppCompatActivity {
    private WebView videoWebView;
    private ImageView livestockType;
    private TextView livestockName;
    private TextView livestockPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        videoWebView = findViewById(R.id.video_webView);
        livestockType = findViewById(R.id.mornitored_type);
        livestockName = findViewById(R.id.mornitored_name);
        livestockPosition = findViewById(R.id.mornitored_position);
        WebSettings webSettings = videoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        videoWebView.setWebChromeClient(new WebChromeClient());
        videoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });
        String url = "http://10.0.2.2:5000/stream";
        videoWebView.loadUrl(url);
        //setLivestockInfo();
    }

    void setLivestockInfo() {
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
    }

    // 가축 종류에 따라 이미지 리소스 반환
    /*private int getImageResource(String type) {
        switch (type) {
            case "cow":
                return R.drawable.cow;
            case "pig":
                return R.drawable.pig;
            case "sheep":
                return R.drawable.sheep;
            default:
                return R.drawable.animal;
        }
    }*/
}