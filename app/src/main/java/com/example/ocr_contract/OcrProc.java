package com.example.ocr_contract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

public class OcrProc {

    public static String main(String ocrApiUrl, String ocrSecretKey, String imagePath) {

        String ocrMessage = "";

        try {

            String apiURL = ocrApiUrl;
            String secretKey = ocrSecretKey;

            //String objectStorageURL = "https://kr.object.ncloudstorage.com/handwrite-recognition/image3.jpg"; for test
            String imageSourcePath = imagePath;

            URL url = new URL(apiURL);

            //String message = getReqMessage(objectStorageURL);
            String message = getReqMessage(imageSourcePath);
            System.out.println("##" + message);

            long timestamp = new Date().getTime();

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(imageSourcePath);
            writeMultiPart(wr, message, file, boundary);
            wr.close();

            int responseCode = con.getResponseCode();

            if(responseCode==200) { // 정상 호출
                System.out.println(con.getResponseMessage());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    ocrMessage = decodedString;
                }
                //chatbotMessage = decodedString;
                in.close();

            } else {
                System.out.println("error>>>>>>" + responseCode);
                ocrMessage = con.getResponseMessage();

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(">>>>>>>>>>"+ ocrMessage);
        return ocrMessage;
    }

    public static String getReqMessage(String imagePath) {

        String requestBody = "";

        try {

            long timestamp = new Date().getTime();

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", Long.toString(timestamp));
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "test_ocr");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);

            requestBody = json.toString();

        } catch (Exception e){
            System.out.println("## Exception : " + e);
        }

        return requestBody;

    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

}