package com.example.ocr_contract;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OcrActivity extends MainActivity{

    Intent intent;
    SimpleDateFormat imageDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        //SharedPreferences sharedPref = getSharedPreferences("PREF", Context.MODE_PRIVATE);

        //final String ocrApiGwUrl = sharedPref.getString("ocr_api_gw_url", "");
        //final String ocrSecretKey = sharedPref.getString("ocr_secret_key", "");
        String ocrApiGwUrl = "https://f06c7b96c66848f3b2fe59dd08c1a11b.apigw.ntruss.com/custom/v1/11190/cee73c8fd62a897329348ab9f0a4c9339f3433705bfaf36c9fbd448fddfd6223/infer";
        String ocrSecretKey = "SnV3T1dGT2R2YVJzVENqcnNERFBkTUd1cWh5a0p5SkE=";
        System.out.println("## Exception : " + ocrApiGwUrl + "\n" + ocrSecretKey);

        ImageButton select_Image;
        select_Image = (ImageButton) findViewById(R.id.imageButton);
        select_Image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                final int GALLERY = 101;
                startActivityForResult(intent, GALLERY);
            }
        });

        Button ocrTranslateBtn;
        ocrTranslateBtn = (Button) findViewById(R.id.btn_ocr_translate);
        ocrTranslateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imagePath == null){
                    Toast toast = Toast.makeText(getApplicationContext(),"select image", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    OcrActivity.NaverOcrTask OcrTask = new OcrActivity.NaverOcrTask();
                    OcrTask.execute(ocrApiGwUrl, ocrSecretKey, imagePath);
                }
            }
        });

        Button cameraBtn;
        cameraBtn = (Button) findViewById(R.id.btn_camera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File imageFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imageFile != null) {
                        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.getimage.fileprovider", imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        final int CAMERA = 100;
                        startActivityForResult(intent, CAMERA);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = null;
            ImageButton select_Image;
            select_Image = (ImageButton) findViewById(R.id.imageButton);
            switch (requestCode) {
                case 101:
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imagePath = cursor.getString(index);
                        bitmap = BitmapFactory.decodeFile(imagePath);
                        cursor.close();
                    }
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        select_Image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 100:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(imagePath, options);
                    break;
            }
            //select_Image.setImageBitmap(bitmap);
            Glide.with(getApplicationContext()).asBitmap().load(imagePath).into(select_Image);
        }
    }
    
    public class NaverOcrTask extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... strings) {

            return OcrProc.main(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(String result) {

            ReturnThreadResult(result);
        }
    }

    public void ReturnThreadResult(String result) {
        System.out.println("###  Return Thread Result");
        String translateText = "";
        String[] final_result = new String[25];
        int k = 0;
        String rlt = result;
        try {
            JSONObject jsonObject = new JSONObject(rlt);

            JSONArray jsonArray  = jsonObject.getJSONArray("images");

            for (int i = 0; i < jsonArray.length(); i++ ){

                JSONArray jsonArray_fields  = jsonArray.getJSONObject(i).getJSONArray("fields");

                for (int j=0; j < jsonArray_fields.length(); j++ ){

                    String inferText = jsonArray_fields.getJSONObject(j).getString("inferText");
                    String field_name = jsonArray_fields.getJSONObject(j).getString("name");
                    String boundingPoly = jsonArray_fields.getJSONObject(j).getString("boundingPoly");

                    final_result[k] = field_name;
                    final_result[k+1] = inferText;
                    final_result[k+2] = boundingPoly;
                    k = k + 3;
                }
            }
            final_result[k] = imagePath;

        } catch (Exception e) {

        }
        intent = new Intent(getApplicationContext(), OcrResult.class);
        intent.putExtra("result", final_result);
        startActivity(intent);
        finish();
    }

    File createImageFile() throws IOException {
        String timeStamp = imageDate.format(new Date());
        String fileName = "IMAGE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(fileName, ".jpg", storageDir);
        imagePath = file.getAbsolutePath();
        return file;
    }
}
