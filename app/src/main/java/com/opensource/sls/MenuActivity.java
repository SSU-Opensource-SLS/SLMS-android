package com.opensource.sls;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MenuActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View monitoringButton;
    private View manageLivestockButton;
    private View communityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        monitoringButton = findViewById(R.id.monitoringButton);
        manageLivestockButton = findViewById(R.id.manageButton);
        communityButton = findViewById(R.id.communityButton);
        //isPostNotificationsPermissionGranted();
        // 권한이 허용되었는지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않은 경우 권한 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
                // 권한 요청 이유를 설명하는 다이얼로그 표시
                showPermissionExplanationDialog();
            } else {
                // 권한 요청 다이얼로그 표시
                requestPermission();
            }
        } else {
            // 권한이 이미 허용된 경우 알림 소리 설정 코드 작성
            //setNotificationSound();
        }

        monitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConnectCamActivity.class);
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

        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        askNotificationPermission();
        displayFirebaseToken();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 알림 소리 설정 코드 작성
                //setNotificationSound();
            } else {
                // 권한이 거부된 경우 사용자에게 설명하거나 다른 조치를 취할 수 있습니다.
                // ...
            }
        }
    }

    private void showPermissionExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한 요청")
                .setMessage("알림 권한이 필요합니다. 권한을 허용하시겠습니까?")
                .setPositiveButton("허용", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 권한 거부 처리
                    }
                })
                .show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, PERMISSION_REQUEST_CODE);
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: Display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("Notification Permission");
                builder.setMessage("This app requires notification permission to send you important updates. Grant permission to enable notifications.");

                // Add "OK" button to directly request permission
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Directly ask for the permission
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                    }
                });

                // Add "No thanks" button to allow the user to continue without notifications
                builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue without notifications
                    }
                });

                // Display the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void isPostNotificationsPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the permission is granted
            int result = checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
            Toast.makeText(getApplicationContext(), "권한 허가 됨", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), "권한 허가 안 됨", Toast.LENGTH_SHORT).show();
    }

    private void displayFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log token
                        Log.d(TAG, "FCM registration token: " + token);
                    }
                });
    }
}