package com.opensource.sls;

import android.content.Intent;
import android.media.MediaParser;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.opensource.sls.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {
    private View monitoringButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        monitoringButton = findViewById(R.id.monitoringButton);

        monitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MonitoringActivity.class);
                startActivity(intent);
            }
        });
    }
}