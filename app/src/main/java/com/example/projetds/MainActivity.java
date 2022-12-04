package com.example.projetds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public Button _btnContract;
    public Button _btnClient;
    private dataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new dataBaseHelper(this);

        _btnContract = findViewById(R.id.contractBtn);
        _btnClient = findViewById(R.id.clientBtn);


        _btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Contract.class);
                startActivity(intent1);
            }
        });

        _btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Client.class);
                startActivity(intent1);
            }
        });


    }
}