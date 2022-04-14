package com.example.ocr_contract;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class DatabaseActivity extends MainActivity {
    private List<Contract> contractList;
    private contractDB contractDB = null;
    private contractAdapter contractAdapter;
    private Context context = null;
    private RecyclerView RecyclerView;
    private Button searchButton;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        RecyclerView = (RecyclerView) findViewById(R.id.recycler);
        context = getApplicationContext();
        contractAdapter = new contractAdapter(contractList);
        contractDB = contractDB.getInstance(this);

        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.interrupt();
                searchRunnable searchRunnable = new searchRunnable();
                Thread th = new Thread(searchRunnable);
                th.start();
                //// 검색버튼 다듬기
            }
        });
    }

    class searchRunnable implements Runnable {
        @Override
        public void run(){
            try {
                //String phone_number = editTextSearch.getText().toString();
                //임시 테스트
                //editTextSearch.setText(contractList.get(4).phoneNumber);
                String phoneNumber = editTextSearch.getText().toString();
                Contract searchedContract = contractDB.getInstance(context).contractDAO().findbyphoneNumber(phoneNumber);
                Intent intent;
                intent = new Intent(getApplicationContext(), DatabaseItemActivity.class);
                intent.putExtra("item", searchedContract);
                startActivity(intent);
            }
            catch (Exception e) {

            }
        }
    }

    class InsertRunnable implements Runnable {
        @Override
        public void run(){
            try {
                contractList = contractDB.getInstance(context).contractDAO().getAll();
                contractAdapter = new contractAdapter(contractList);
                contractAdapter.setOnItemClickListener(new contractAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //////    할일 Database item 조회
                        Contract selectedContract = contractList.get(position);
                        System.out.println(selectedContract.name);
                        Intent intent;
                        intent = new Intent(getApplicationContext(), DatabaseItemActivity.class);
                        intent.putExtra("item", selectedContract);
                        startActivity(intent);
                    }
                });
                contractAdapter.notifyDataSetChanged();

                RecyclerView.setAdapter(contractAdapter);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                RecyclerView.setLayoutManager(mLinearLayoutManager);
            }
            catch (Exception e) {

            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        contractDB.destroyInstance();
        contractDB = null;
    }

    public static class SerialObj implements Serializable{
        Contract contract;
    }
}
