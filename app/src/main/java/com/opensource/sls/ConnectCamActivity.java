package com.opensource.sls;

import android.content.AttributionSource;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.opensource.sls.DTO.CamQRDto;
import com.opensource.sls.DTO.InputLivestockDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectCamActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    CamAdapter adapter;
    ArrayList<CamItem> camItems;
    Button connectButton;
    ImageView backButton;
    private static final OkHttpClient client = new OkHttpClient();

    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_cam);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.camRecyclerView);
        connectButton = findViewById(R.id.connect_cam_button);
        backButton = findViewById(R.id.cam_back);
        camItems = new ArrayList<CamItem>();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CamAdapter(getApplicationContext());
        getCamDatas(adapter,camItems);
        recyclerView.setAdapter(adapter);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConnectCamDlg();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter.setOnItemClickListener(new CamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CamAdapter.ViewHolder holder, View view, int position) {
                CamItem item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"아이템 선택됨 : " + item.getLivestock_type() + " " + item.getNum(), Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent(getApplicationContext(),MonitoringActivity.class);
                intent.putExtra("item",item);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new CamAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(CamAdapter.ViewHolder holder, View view, int position) {
                CamItem item = adapter.getItem(position);
                showRemoveCam(adapter, item);
            }
        });
    }

    public void getCamDatas(final CamAdapter adapter, final ArrayList<CamItem> camItems) {
        String url = "http://10.0.2.2:5000/cam/" + mAuth.getUid(); // Replace with your actual API URL
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray camArray = new JSONArray(responseBody);
                        for (int i = 0; i < camArray.length(); i++) {
                            JSONObject camObject = camArray.getJSONObject(i);
                            String uid = camObject.getString("uid");
                            String livestockType = camObject.getString("livestock_type");
                            Long num = camObject.getLong("num");
                            String livestockName = camObject.getString("livestock_name");
                            String url = camObject.getString("url");

                            CamItem camItem = new CamItem(uid,livestockType,num,livestockName,url);
                            camItems.add(camItem);
                        }

                        // Update the adapter on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addItems(camItems);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }

    class MyLivestockAdapter extends BaseAdapter {
        ArrayList<LivestockItem> items = new ArrayList<LivestockItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(LivestockItem item) {
            items.add(item);
        }

        public void addItems(ArrayList<LivestockItem> livestockItems) {
            items.addAll(livestockItems);
        }

        public void deleteItem(LivestockItem item) { items.remove(item); }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            MyLivestockView myLivestockView = null;
            if(view == null) {
                myLivestockView = new MyLivestockView(getApplicationContext());
            } else {
                myLivestockView = (MyLivestockView) view;
            }
            LivestockItem item = items.get(position);
            myLivestockView.setName(item.getName());
            return myLivestockView;
        }
    }

    class MyLivestockView extends LinearLayout {
        TextView livestock_name;

        public MyLivestockView(Context context) {
            super(context);
            init(context);
        }
        public MyLivestockView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.select_livestock_item, this, true);
            livestock_name = findViewById(R.id.my_livestock_name);
        }

        public void setName(String name) {
            livestock_name.setText(name);
        }
    }


    public void showRemoveCam(final CamAdapter camAdapter, CamItem item) {
        AlertDialog.Builder deleteDlg = new AlertDialog.Builder(this);
        deleteDlg.setTitle(item.getLivestock_type() + " " + item.getNum() + "를(을) 삭제 하시겠습니까?");
        deleteDlg.setIcon(R.drawable.main_cow);
        deleteDlg.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogIn제erface, int i) {
                deleteCam(item);
                adapter.deleteItem(item);
                adapter.notifyDataSetChanged();
            }
        });
        deleteDlg.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();;
            }
        });
        deleteDlg.show();
    }

    public void deleteCam(CamItem camItem) {
        // 요청 URL 생성
        String url = "http://10.0.2.2:5000/cam/" + camItem.getUid() + "/" +
                camItem.getLivestock_type() + "/" + camItem.getNum();

        // DELETE 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        // 요청 실행
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    // 삭제 성공
                    try {
                        String result = response.body().string();
                        // 처리할 로직 작성
                    } catch (IOException e) {
                        // 예외 처리
                        e.printStackTrace();
                    }
                    // 처리 결과를 이용하여 필요한 작업 수행
                } else {
                    // 삭제 실패
                    // 오류 처리
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 통신 실패
                // 오류 처리
            }
        });
    }

    public void showConnectCamDlg() {
        final String[] livestock_type = {""};
        final String[] livestock_name = {""};

        View dlgView = View.inflate(ConnectCamActivity.this, R.layout.connect_cam_dlg, null);
        AlertDialog.Builder registerDlg = new AlertDialog.Builder(ConnectCamActivity.this);
        registerDlg.setTitle("캠 연결");
        registerDlg.setIcon(R.drawable.main_cow);
        registerDlg.setView(dlgView);
        final EditText connectWifi = dlgView.findViewById(R.id.connect_wifi);
        final EditText connectWifiPwd = dlgView.findViewById(R.id.connect_wifi_pwd);
        final ListView camLivestockList = dlgView.findViewById(R.id.cam_select_livestock);
        MyLivestockAdapter adapter1 = new MyLivestockAdapter();

        getLivestockDatas(adapter1);
        camLivestockList.setAdapter(adapter1);

        camLivestockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LivestockItem livestockItem = (LivestockItem) adapter1.getItem(position);
                livestock_name[0] = livestockItem.getName();
                livestock_type[0] = livestockItem.getLivestock_type();
                Toast.makeText(getApplicationContext(),livestock_name[0] + " 선택 됨",Toast.LENGTH_SHORT).show();
            }
        });

        registerDlg.setPositiveButton("QR 생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CamQRDto camQRDto = new CamQRDto(mAuth.getUid(),connectWifi.getText().toString(),connectWifiPwd.getText().toString(),livestock_type[0],livestock_name[0]);
                finish();
                Intent intent = new Intent(getApplicationContext(), ShowQR.class);
                intent.putExtra("QR_info",camQRDto);
                startActivity(intent);
            }
        });
        registerDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        registerDlg.show();
    }

    public void getLivestockDatas(final MyLivestockAdapter adapter) {
        ArrayList<LivestockItem> livestockItems = new ArrayList<LivestockItem>();
        String url = "http://10.0.2.2:5000/livestock/" + mAuth.getUid(); // Replace with your actual API URL
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray livestockArray = new JSONArray(responseBody);
                        for (int i = 0; i < livestockArray.length(); i++) {
                            JSONObject livestockObject = livestockArray.getJSONObject(i);
                            String uid = livestockObject.getString("uid");
                            String livestockType = livestockObject.getString("livestock_type");
                            Long num = livestockObject.getLong("num");
                            String name = livestockObject.getString("name");
                            String cattle = livestockObject.getString("cattle");
                            Number is_pregnancy = 0;
                            if (!livestockObject.isNull("is_pregnancy")) {
                                is_pregnancy = livestockObject.getInt("is_pregnancy");
                            }

                            LivestockItem livestockItem = new LivestockItem(uid, livestockType, num, name, cattle, is_pregnancy);
                            livestockItems.add(livestockItem);
                        }

                        // Update the adapter on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.addItems(livestockItems);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }
}