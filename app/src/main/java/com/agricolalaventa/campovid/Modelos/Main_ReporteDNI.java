package com.agricolalaventa.campovid.Modelos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class Main_ReporteDNI extends AppCompatActivity {

    DatabaseHelper db;
    Context context;
    private TextView tvTotReporteRegS;
    private ListView regsim_list;
    private Button btnRegSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportedni);

        context     = this;
        db          = new DatabaseHelper(context);

        tvTotReporteRegS = (TextView)findViewById(R.id.tvTotReporteRegS);
        btnRegSimple = (Button) findViewById(R.id.btnRegSimple);

        ArrayList<HashMap<String, String>> userList = db.GetObreros();
        ListView lv = (ListView) findViewById(R.id.regsim_list);
        ListAdapter adapter = new SimpleAdapter(Main_ReporteDNI.this, userList, R.layout.activity_reportedni_det,new String[]{"idobrero","dni"}, new int[]{R.id.rsimple01, R.id.rsimple02});
        lv.setAdapter(adapter);

        tvTotReporteRegS.setText(db.totalSimple());

        btnRegSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(), Main_ReporteDNI.class);
                startActivity(i);
            }
        });

    }
}