package com.example.ocr_contract;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;

public class OcrResult extends AppCompatActivity {

    private contractDB contractDB = null;
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextSum;
    private EditText editTextPhone;
    private EditText editTextContractDate;
    private EditText editTextMeasurementDate;
    private EditText editTextWorkingDate;

    private Context mContext;

    private ImageView imageViewName;
    private ImageView imageViewAddress;
    private ImageView imageViewSum;
    private ImageView imageViewPhone;
    private ImageView imageViewContractDate;
    private ImageView imageViewMeasurementDate;
    private ImageView imageViewWorkingDate;
    private Button addItemButton;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_result);
        intent = getIntent();
        String results[] = intent.getExtras().getStringArray("result");
        contractDB = contractDB.getInstance(this);
        mContext = getApplicationContext();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextSum = (EditText) findViewById(R.id.editTextSum);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextContractDate = (EditText) findViewById(R.id.editTextContractDate);
        editTextMeasurementDate = (EditText) findViewById(R.id.editTextMeasurementDate);
        editTextWorkingDate = (EditText) findViewById(R.id.editTextWorkingDate);
        addItemButton = (Button) findViewById(R.id.addItemButton);

        String name = results[1].substring(4).replaceAll(" ", "");
        String address = results[4].substring(4);
        String sum = results[7].substring(4).replaceAll(" ", "");
        String phone = results[10].substring(4).replaceAll("[^0-9]", "");
        String contractDate = results[13].substring(4).replaceAll(" ", "");
        String measurementDate = results[16].substring(4).replaceAll(" ", "");
        String workingDate = results[19].substring(4).replaceAll(" ", "");

        String boundingPoly1 = results[2].replaceAll("[^0-9\\,]", "");
        String boundingPoly2 = results[5].replaceAll("[^0-9\\,]", "");
        String boundingPoly3 = results[8].replaceAll("[^0-9\\,]", "");
        String boundingPoly4 = results[11].replaceAll("[^0-9\\,]", "");
        String boundingPoly5 = results[14].replaceAll("[^0-9\\,]", "");
        String boundingPoly6 = results[17].replaceAll("[^0-9\\,]", "");
        String boundingPoly7 = results[20].replaceAll("[^0-9\\,]", "");

        editTextName.setText(name);
        editTextAddress.setText(address);
        editTextSum.setText(sum);
        editTextPhone.setText(phone);
        editTextContractDate.setText(contractDate);
        editTextMeasurementDate.setText(measurementDate);
        editTextWorkingDate.setText(workingDate);

        imageViewName = (ImageView) findViewById(R.id.imageViewName);
        imageViewAddress = (ImageView) findViewById(R.id.imageViewAddress);
        imageViewSum = (ImageView) findViewById(R.id.imageViewSum);
        imageViewPhone = (ImageView) findViewById(R.id.imageViewPhoneNumber);
        imageViewContractDate = (ImageView) findViewById(R.id.imageViewContractDate);
        imageViewMeasurementDate = (ImageView) findViewById(R.id.imageViewMeasurementDate);
        imageViewWorkingDate = (ImageView) findViewById(R.id.imageViewWorkingDate);

        Matrix matrix = new Matrix();
        matrix.setScale(1.5f,1.5f);
        File imgFile = new File(results[21]);
        //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        final Bitmap[] bitmap = new Bitmap[1];
        Glide.with(this)
                .asBitmap().load(results[21])
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap nameImg = getTargetImage(resource, boundingPoly1, matrix);
                        Bitmap addressImg = getTargetImage(resource, boundingPoly2, matrix);
                        Bitmap sumImg = getTargetImage(resource, boundingPoly3, matrix);
                        Bitmap phoneNumberImg = getTargetImage(resource, boundingPoly4, matrix);
                        Bitmap contractDateImg = getTargetImage(resource, boundingPoly5, matrix);
                        Bitmap measurementDateImg = getTargetImage(resource, boundingPoly6, matrix);
                        Bitmap workingDateImg = getTargetImage(resource, boundingPoly7, matrix);

                        imageViewName.setImageBitmap(nameImg);
                        imageViewAddress.setImageBitmap(addressImg);
                        imageViewSum.setImageBitmap(sumImg);
                        imageViewPhone.setImageBitmap(phoneNumberImg);
                        imageViewContractDate.setImageBitmap(contractDateImg);
                        imageViewMeasurementDate.setImageBitmap(measurementDateImg);
                        imageViewWorkingDate.setImageBitmap(workingDateImg);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    /*
        Bitmap nameImg = getTargetImage(bitmap[0], boundingPoly1, matrix);
        Bitmap addressImg = getTargetImage(bitmap[0], boundingPoly2, matrix);
        Bitmap sumImg = getTargetImage(bitmap[0], boundingPoly3, matrix);
        Bitmap phoneNumberImg = getTargetImage(bitmap[0], boundingPoly4, matrix);
        Bitmap contractDateImg = getTargetImage(bitmap[0], boundingPoly5, matrix);
        Bitmap measurementDateImg = getTargetImage(bitmap[0], boundingPoly6, matrix);
        Bitmap workingDateImg = getTargetImage(bitmap[0], boundingPoly7, matrix);

        imageViewName.setImageBitmap(nameImg);
        imageViewAddress.setImageBitmap(addressImg);
        imageViewSum.setImageBitmap(sumImg);
        imageViewPhone.setImageBitmap(phoneNumberImg);
        imageViewContractDate.setImageBitmap(contractDateImg);
        imageViewMeasurementDate.setImageBitmap(measurementDateImg);
        imageViewWorkingDate.setImageBitmap(workingDateImg);
    */
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                Contract contract = new Contract();
                contract.name = editTextName.getText().toString();
                contract.address = editTextAddress.getText().toString();
                contract.sum = editTextSum.getText().toString();
                contract.phoneNumber = editTextPhone.getText().toString();
                contract.contractDate = editTextContractDate.getText().toString();
                contract.measurementDate = editTextMeasurementDate.getText().toString();
                contract.workingDate = editTextWorkingDate.getText().toString();
                contract.boundingPoly = results[2] + results[5] + results[8] + results[11] + results[14] + results[17] + results[20];
                contract.picturePath = results[21];
                if (contract.name != name)
                    contract.changes = contract.changes + "{1: " + results[1] + "}";
                if (contract.address != address)
                    contract.changes = contract.changes + "{2: " + results[4] + "}";
                if (contract.sum != sum)
                    contract.changes = contract.changes + "{3: " + results[7] + "}";
                if (contract.phoneNumber != phone)
                    contract.changes = contract.changes + "{4: " + results[10] + "}";
                if (contract.contractDate != contractDate)
                    contract.changes = contract.changes + "{5: " + results[13] + "}";
                if (contract.measurementDate != measurementDate)
                    contract.changes = contract.changes + "{6: " + results[16] + "}";
                if (contract.workingDate != workingDate)
                    contract.changes = contract.changes + "{7: " + results[19] + "}";
                contractDB.getInstance(mContext).contractDAO().insertAll(contract);
            }
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread addThread = new Thread(insertRunnable);
                addThread.start();

                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        contractDB.destroyInstance();
    }

    Bitmap getTargetImage(Bitmap source, String boundingPolys, Matrix matrix){
        String[] poly = boundingPolys.split(",");
        int x, y, width, height;
        x = Integer.parseInt(poly[0]);
        y = Integer.parseInt(poly[1]);
        width = Integer.parseInt(poly[2]) - x;;
        height = Integer.parseInt(poly[7]) - y;
        Bitmap target = Bitmap.createBitmap(source, x, y, width, height, matrix, true);
        return target;
    }
}
