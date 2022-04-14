package com.example.ocr_contract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasCamPerm = checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasWritePerm = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (!hasCamPerm || !hasWritePerm)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        SharedPreferences sharedPref1 = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        String ocrApiGwUrl = sharedPref1.getString("ocr_api_gw_url", "");
        String ocrSecretKey = sharedPref1.getString("ocr_secret_key", "");
        ocrApiGwUrl = "https://f06c7b96c66848f3b2fe59dd08c1a11b.apigw.ntruss.com/custom/v1/11190/cee73c8fd62a897329348ab9f0a4c9339f3433705bfaf36c9fbd448fddfd6223/infer";
        ocrSecretKey = "SnV3T1dGT2R2YVJzVENqcnNERFBkTUd1cWh5a0p5SkE=";

        Button ocrTranslateBtn = (Button) findViewById(R.id.button2);
        Button databaseAccessBtn = (Button) findViewById(R.id.button3);
        ocrTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), OcrActivity.class);
                startActivity(intent);
            }
        });
        databaseAccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(intent);
            }
        });
    }

}
