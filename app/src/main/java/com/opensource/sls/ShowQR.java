package com.opensource.sls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.opensource.sls.DTO.CamQRDto;

public class ShowQR extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private Button generateButton;
    Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);
        Intent intent  = getIntent();
        CamQRDto camQRDto = (CamQRDto) intent.getSerializableExtra("QR_info");
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        generateButton = findViewById(R.id.generateQrCodeButton);
        try {
            qrCodeBitmap = generateQRCode(camQRDto);
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    qrCodeBitmap = generateQRCode(camQRDto);
                    qrCodeImageView.setImageBitmap(qrCodeBitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Bitmap generateQRCode(CamQRDto camQRDto) throws WriterException {
        String qrCodeData = camQRDto.getUid() + "|"
                + camQRDto.getLivestock_type() + "|"
                + camQRDto.getWifi_name() + "|"
                + camQRDto.getWifi_pwd();
        int qrCodeSize = 500;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
            }
        }

        return qrCodeBitmap;
    }
}