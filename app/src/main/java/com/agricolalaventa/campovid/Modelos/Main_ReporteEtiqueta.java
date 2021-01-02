package com.agricolalaventa.campovid.Modelos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.agricolalaventa.campovid.R;
import com.agricolalaventa.campovid.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Main_ReporteEtiqueta extends AppCompatActivity {

    private ListView lstReporte01, user_list;
    Context context;
    DatabaseHelper db;
    private String fecReporte;
    private TextView tvTitReporte, tvTotReporte;
    private Button btnRegEtiqueta;
    private String codPDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporteetiqueta);

        context     = this;
        db          = new DatabaseHelper(context);

        fecReporte = DateFormat.format("dd/MM/yyyy", System.currentTimeMillis()).toString();


        tvTitReporte = (TextView)findViewById(R.id.tvTitReporte);
        btnRegEtiqueta = (Button) findViewById(R.id.btnRegEtiqueta);
        tvTotReporte = (TextView)findViewById(R.id.tvTotReporte);

        // Recogemos el nombre del activity anterior
        //Bundle bundle = getIntent().getExtras();
        //codPDA = bundle.getString("codPDA");
        //tvPDACaja.setText(codPDA);

        ArrayList<HashMap<String, String>> userList = db.GetUsers();
        ListView lv = (ListView) findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(Main_ReporteEtiqueta.this, userList, R.layout.activity_reporteetiqueta_det,new String[]{"idobrero","cantidad"}, new int[]{R.id.campo01, R.id.campo03});
        lv.setAdapter(adapter);


        tvTitReporte.setText("Reporte al "+ fecReporte);
        tvTotReporte.setText("Total: "+db.totalTipo());

        btnRegEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(), Main_Etiqueta.class);
                i.putExtra("codPDA", codPDA);
                startActivity(i);
            }
        });

    }
}