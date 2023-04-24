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
        setLivestockInfo();
    }

    void setLivestockInfo() {
        livestockName.setText("누렁이");
        livestockPosition.setText("1번 축사");
    }
}