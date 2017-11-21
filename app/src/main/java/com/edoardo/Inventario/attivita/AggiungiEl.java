package com.edoardo.Inventario.attivita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.edoardo.Inventario.R;

public class AggiungiEl extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scannerBtn = (Button) findViewById(R.id.btn_scanner);
        Button zbarScannerBtn = (Button) findViewById(R.id.btn_zbar_scanner);

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ZxingScannerActivity.class);
                startActivity(intent);
            }
        });

        zbarScannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ZbarScannerActivity.class);
                startActivity(intent);
            }
        });
    }
}
