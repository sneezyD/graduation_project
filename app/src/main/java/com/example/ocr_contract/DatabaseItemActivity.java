package com.example.ocr_contract;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.Serializable;

public class DatabaseItemActivity extends AppCompatActivity {
    Intent intent;

    private TextView TextViewName;
    private TextView TextViewAddress;
    private TextView TextViewSum;
    private TextView TextViewPhone;
    private TextView TextViewContractDate;
    private TextView TextViewMeasurementDate;
    private TextView TextViewWorkingDate;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_item);

        intent = getIntent();
        Contract selectedContract = (Contract) intent.getSerializableExtra("item");

        TextViewName = (TextView) findViewById(R.id.TextViewName);
        TextViewAddress = (TextView) findViewById(R.id.TextViewAddress);
        TextViewSum = (TextView) findViewById(R.id.TextViewSum);
        TextViewPhone = (TextView) findViewById(R.id.TextViewPhone);
        TextViewContractDate = (TextView) findViewById(R.id.TextViewContractDate);
        TextViewMeasurementDate = (TextView) findViewById(R.id.TextViewMeasurementDate);
        TextViewWorkingDate = (TextView) findViewById(R.id.TextViewWorkingDate);
        imageView = (ImageView) findViewById(R.id.searchedImage);

        TextViewName.setText("계약자: " + selectedContract.name);
        TextViewAddress.setText("주소: " + selectedContract.address);
        TextViewSum.setText("총금액: " + selectedContract.sum);
        TextViewPhone.setText("연락처: " + selectedContract.phoneNumber);
        TextViewContractDate.setText("계약일: " + selectedContract.contractDate);
        TextViewMeasurementDate.setText("실측일: " + selectedContract.measurementDate);
        TextViewWorkingDate.setText("시공일: " + selectedContract.workingDate);

        File imgFile = new  File(selectedContract.picturePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView = (ImageView) findViewById(R.id.searchedImage);
            imageView.setImageBitmap(bitmap);
        }
    }
}
