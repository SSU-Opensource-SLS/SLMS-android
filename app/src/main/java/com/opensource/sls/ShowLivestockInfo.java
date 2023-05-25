package com.opensource.sls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class ShowLivestockInfo extends AppCompatActivity {
    TextView curName;
    TextView curPosition;
    ImageView curType;
    Switch curPregnant;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_livestock_info);
        curName = findViewById(R.id.show_livestock_name);
        curPosition = findViewById(R.id.show_cattle_position);
        curType = findViewById(R.id.show_livestock_type);
        curPregnant = findViewById(R.id.show_pregnant_switch);
        backButton = findViewById(R.id.show_livestock_back);

        Intent intent  = getIntent();
        LivestockItem livestockItem = (LivestockItem) intent.getSerializableExtra("item");
        setLivestockInfo(livestockItem);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent1 = new Intent(getApplicationContext(), ManageLivestockActivity.class);
                startActivity(intent1);
            }
        });
    }

    public void setLivestockInfo(LivestockItem livestockItem) {
        curName.setText(livestockItem.getName());
        curPosition.setText(livestockItem.getCattle());
        if((livestockItem.getIs_pregnancy() != null && livestockItem.getIs_pregnancy().intValue() != 0)) {
            curPregnant.setChecked(true);
        }
    }
}