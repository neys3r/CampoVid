package com.agricolalaventa.campovid.Modelos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.agricolalaventa.campovid.R;

public class Main_MenuCampo extends AppCompatActivity {
    private Button btnRegistroSimple, btnRegistroEtiqueta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menucampo);

        btnRegistroSimple = (Button)findViewById(R.id.btnRegistroSimple);
        btnRegistroEtiqueta = (Button)findViewById(R.id.btnRegistroEtiqueta);


        btnRegistroEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Main_Etiqueta.class);
                //ii.putExtra("codPDA", codPDA);
                startActivity(i);
            }
        });

        btnRegistroSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Main_RegTrabajador.class);
                //ii.putExtra("codPDA", codPDA);
                startActivity(i);
            }
        });


    }
}