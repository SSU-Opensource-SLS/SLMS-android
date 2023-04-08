package com.opensource.sls;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.opensource.sls.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class FirstJoinFragment extends Fragment implements View.OnClickListener{

    private View root;
    ImageButton openCalender;
    private ImageButton cowImageButton;
    private ImageButton pigImageButton;
    private ImageButton horseImageButton;
    private ImageButton goatImageButton;
    private ImageButton sheepImageButton;
    private View cowLayout;
    private View pigLayout;
    private View horseLayout;
    private View goatLayout;
    private View sheepLayout;

    TextView birthText;

    String birth = "";

    ArrayList<String> livestocks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first, container, false);
        openCalender = root.findViewById(R.id.openCalender); openCalender.setOnClickListener(this);
        cowImageButton = root.findViewById(R.id.cowImageButton); cowImageButton.setOnClickListener(this);
        pigImageButton = root.findViewById(R.id.pigImageButton); pigImageButton.setOnClickListener(this);
        horseImageButton = root.findViewById(R.id.horseImageButton); horseImageButton.setOnClickListener(this);
        goatImageButton = root.findViewById(R.id.goatImageButton); goatImageButton.setOnClickListener(this);
        sheepImageButton = root.findViewById(R.id.sheepImageButton); sheepImageButton.setOnClickListener(this);
        cowLayout = root.findViewById(R.id.cowLayout);
        pigLayout = root.findViewById(R.id.pigLayout);
        horseLayout = root.findViewById(R.id.horseLayout);
        goatLayout = root.findViewById(R.id.goatLayout);
        sheepLayout = root.findViewById(R.id.sheepLayout);
        birthText = root.findViewById(R.id.birth_text);
        return root;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cowImageButton:
                if(livestocks.contains("cow")) {
                    cowLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                    livestocks.remove("cow");
                } else {
                    cowLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                    livestocks.add("cow");
                }
                break;
            case R.id.pigImageButton:
                if(livestocks.contains("pig")) {
                    pigLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                    livestocks.remove("pig");
                } else {
                    pigLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                    livestocks.add("pig");
                }
                break;
            case R.id.horseImageButton:
                if(livestocks.contains("horse")) {
                    horseLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                    livestocks.remove("horse");
                } else {
                    horseLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                    livestocks.add("horse");
                }
                break;
            case R.id.goatImageButton:
                if(livestocks.contains("goat")) {
                    goatLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                    livestocks.remove("goat");
                } else {
                    goatLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                    livestocks.add("goat");
                }
                break;
            case R.id.sheepImageButton:
                if(livestocks.contains("sheep")) {
                    sheepLayout.setBackground(getResources().getDrawable(R.drawable.normal_view));
                    livestocks.remove("sheep");
                } else {
                    sheepLayout.setBackground(getResources().getDrawable(R.drawable.view_edge));
                    livestocks.add("sheep");
                }
                break;
            case R.id.openCalender:
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getContext(), mDateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    birth = String.format("%d-%d-%d", yy,mm+1,dd);
                    birthText.setText(String.format("%d년 %d월 %d일", yy,mm+1,dd));
                }
            };

    public String getBirthText() {
        return birth;
    }

    public ArrayList<String> getLivestocks() {
        return livestocks;
    }
}