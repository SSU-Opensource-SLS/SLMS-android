package com.opensource.sls;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.opensource.sls.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity {

    private Button joinButton;
    FragmentManager fragmentManager;
    FirstFragment fragment1;
    SecondFragment fragment2;
    boolean isLastFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        joinButton = findViewById(R.id.joinMemberButton);
}