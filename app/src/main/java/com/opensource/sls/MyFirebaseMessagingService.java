package com.opensource.sls;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "my_default_channel";
    private static final String CHANNEL_NAME = "Default Channel";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }

    // 클라우드 서버에서 메시지를 전송하면 자동으로 호출
    // 이 메서드 안에서 메시지를 처리하여 사용자에게 알림을 보낼 수 있음
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 기본 알림 소리

        // 채널에 대한 소리 특성을 설정합니다.
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        // 알림 채널을 생성합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(soundUri, audioAttributes); // 채널에 소리 특성 설정
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 빌더에 소리를 설정합니다.
        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setSound(soundUri); // 소리 설정

        Notification notification = builder.build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, notification);
    }
}
