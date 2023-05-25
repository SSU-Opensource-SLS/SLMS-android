package com.opensource.sls;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class MenuActivity extends AppCompatActivity {
    private View monitoringButton;
    private View manageLivestockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        monitoringButton = findViewById(R.id.monitoringButton);
        manageLivestockButton = findViewById(R.id.manageButton);

        monitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MonitoringActivity.class);
                startActivity(intent);
            }
        });

        manageLivestockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageLivestockActivity.class);
                startActivity(intent);
            }
        });
    }
}