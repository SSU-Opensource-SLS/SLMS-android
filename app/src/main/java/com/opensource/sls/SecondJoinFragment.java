package com.opensource.sls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.opensource.sls.databinding.FragmentSecondBinding;

public class SecondJoinFragment extends Fragment implements View.OnClickListener{

    EditText joinName;
    EditText joinEmail;
    EditText joinPwd;
    EditText checkPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_second, container, false);
        joinName = root.findViewById(R.id.sign_name);
        joinEmail = root.findViewById(R.id.sign_email);
        joinPwd = root.findViewById(R.id.sign_pwd);
        checkPwd = root.findViewById(R.id.passwd_check);
        return root;
    }

    @Override
    public void onClick(View view) {

    }

    public String getName() {
        return joinName.getText().toString();
    }

    public String getEmail() {
        return joinEmail.getText().toString();
    }

    public String getPwd() {
        return joinPwd.getText().toString();
    }

    public String getCheckPwd() {
        return checkPwd.getText().toString();
    }
}