package com.agricolalaventa.campovid.Modelos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.agricolalaventa.campovid.Clases.Etiqueta;
import com.agricolalaventa.campovid.Network.NetworkStateCheckerEtiqueta;
import com.agricolalaventa.campovid.R;
import com.agricolalaventa.campovid.VolleySingleton;
import com.agricolalaventa.campovid.db.DBContract;
import com.agricolalaventa.campovid.db.DatabaseHelper;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main_Etiqueta extends AppCompatActivity implements View.OnClickListener {

    private String placaBus, mensaje, idSucursal, dscSucursal, conteo, tipoIngreso, fechaP;
    private Integer fechacompara, fechahoy;
    private MediaPlayer scorrecto, sincorrecto;

    public static final String URL_SAVE_NAME = "http://wslaventa.agricolalaventa.com/campo/wscampopda.php";

    //database helper object
    private Context context;
    private DatabaseHelper db;
    private NetworkStateCheckerEtiqueta nt;

    //View objects
    private Button btnGuardarAsisSeguridad, btnReporteEtiquetas;
    private EditText edtDNIAsisSeguridad;
    private ListView listAsisSeguridad;
    private TextView tvFecha, tvBus, tvSucursal, tvConteoAsistencia, tvTitulo;
    private String codPDA, tipoIS, descIS, idVigilante;
    private ImageView ivLogoTipo;
    private Switch swListViewAsisSeguridad;


    private String fechaLectura = (DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString());
    private String fechaLectura2 = (DateFormat.format("dd-MM-yyyy", System.currentTimeMillis()).toString());

    //List to store all the names
    private List<Etiqueta> etiquetas;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "com.agricolalaventa.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    //private NameAdapter nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiqueta);

        registerReceiver(new NetworkStateCheckerEtiqueta(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelper(this);
        etiquetas = new ArrayList<>();

        btnGuardarAsisSeguridad = (Button) findViewById(R.id.btnGuardarAsisSeguridad);
        edtDNIAsisSeguridad = (EditText) findViewById(R.id.edtDNIAsisSeguridad);
        listAsisSeguridad = (ListView) findViewById(R.id.listAsisSeguridad);
        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        tvSucursal = (TextView) findViewById(R.id.tvSucursal);
        tvConteoAsistencia = (TextView) findViewById(R.id.tvConteoAsistencia);
        btnReporteEtiquetas = (Button) findViewById(R.id.btnReporteEtiquetas);

        edtDNIAsisSeguridad.requestFocus();

        cargarTipoIS();

        tvFecha.setText(fechaLectura2);

        //Cargar las Preferencias
        cargarPreferencias();


        idSucursal = db.miIdSucursal();
        dscSucursal = db.miDescSucursal();

        //cargarPreferenciasTraslado();


        //tvBus.setText(placaBus);
        tvSucursal.setText(dscSucursal);
        cargarTipoIS();
        //cargarPreferenciasTraslado();
        tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());


        // INICIO PRUEBAS ENTER

        //editTextName.setOnClickListener(this);
        edtDNIAsisSeguridad.setInputType(InputType.TYPE_NULL);


        //calling the method to load all the stored names
        //loadNames();



        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                //loadNames();
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

        btnReporteEtiquetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Main_ReporteEtiqueta.class);
                startActivity(i);
            }
        });

        btnGuardarAsisSeguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codDNI = edtDNIAsisSeguridad.getText().toString();
                if (codDNI.length() == 30 || codDNI.length() == 31 ){
                    //refreshList();
                    saveNameToServer();
                    //loadNames();
                    edtDNIAsisSeguridad.setText("");
                    edtDNIAsisSeguridad.requestFocus();
                }else{
                    //saveNameToServer();
                    Toast.makeText(getApplicationContext(), "La etiqueta es incorrecta", Toast.LENGTH_LONG).show();
                    tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());
                    edtDNIAsisSeguridad.setText("");
                    edtDNIAsisSeguridad.clearFocus();
                    edtDNIAsisSeguridad.requestFocus();
                }
            }
        });

        // Enter edtDNIAsisSeguridad

        edtDNIAsisSeguridad.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String codDNI = edtDNIAsisSeguridad.getText().toString();
                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN ) && (i==KeyEvent.KEYCODE_ENTER)  ){


                    if (codDNI.isEmpty() ){
                        Toast.makeText(getApplicationContext(), "La etiqueta es incorrecta", Toast.LENGTH_LONG).show();
                        //establecerFechas();
                        //saveNameToServer();
                        edtDNIAsisSeguridad.setText("");
                        edtDNIAsisSeguridad.clearFocus();
                        edtDNIAsisSeguridad.requestFocus();
                    }else if(codDNI.length() == 30 || codDNI.length() == 31){
                        establecerFechas();
                        if (fechacompara.toString().equalsIgnoreCase(fechahoy.toString())){
                            saveNameToServer();
                            edtDNIAsisSeguridad.setText("");
                            edtDNIAsisSeguridad.clearFocus();
                            edtDNIAsisSeguridad.requestFocus();
                        }else{
                            Toast.makeText(getApplicationContext(), "Etiqueta fuera de fecha", Toast.LENGTH_LONG).show();
                            tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());
                            edtDNIAsisSeguridad.setText("");
                            edtDNIAsisSeguridad.clearFocus();
                            edtDNIAsisSeguridad.requestFocus();
                        }

                    } else{
                        Toast.makeText(getApplicationContext(), "La etiqueta es incorrecta", Toast.LENGTH_LONG).show();
                        //establecerFechas();
                        //saveNameToServer();
                        edtDNIAsisSeguridad.setText("");
                        edtDNIAsisSeguridad.clearFocus();
                        edtDNIAsisSeguridad.requestFocus();
                    }
                    edtDNIAsisSeguridad.setText("");
                    edtDNIAsisSeguridad.clearFocus();
                    edtDNIAsisSeguridad.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }


    private void loadNames() {
        etiquetas.clear();

        cargarTipoIS();

        Cursor cursor = db.getNames(placaBus, tipoIS);
        //Toast.makeText(getApplicationContext(), "placa: "+placaBus+tipoIS, Toast.LENGTH_LONG).show();
        if (cursor.moveToFirst()) {
            do {
                Etiqueta registro = new Etiqueta(

                        cursor.getInt(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.STATUS)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.CODBARRA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.IDSUPERVISOR)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.IDOBRERO)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.PDA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.TIPOPERSONAL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.FECHA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.FECHAPROCESO))

                );
                etiquetas.add(registro);
            } while (cursor.moveToNext());
        }
/*
        nameAdapter = new NameAdapter(this, R.layout.names, names);
        listAsisSeguridad.setAdapter(nameAdapter);*/
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        //nameAdapter.notifyDataSetChanged();
        tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());
    }

    /*
     * this method is saving the name to ther server
     * */
    private void saveNameToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando Registro...");
        progressDialog.show();

        cargarTipoIS();

        establecerFechas();
/*
        String mes, dia;

        Integer anio;
        anio = 2000 + Integer.parseInt(edtDNIAsisSeguridad.getText().toString().substring(28,30));
        mes = edtDNIAsisSeguridad.getText().toString().substring(26,28);
        dia = edtDNIAsisSeguridad.getText().toString().substring(24,26);
        String fechaP = anio.toString()+"-"+mes+"-"+dia;

        fechacompara = Integer.parseInt(anio+mes+dia);
        fechahoy = Integer.parseInt((DateFormat.format("yyyyMMdd", System.currentTimeMillis()).toString()));
*/

        final String codbarra = edtDNIAsisSeguridad.getText().toString().substring(0,30);
        final String idreferencia = "";
        final String idsupervisor = edtDNIAsisSeguridad.getText().toString().substring(6,9);
        final String idobrero = edtDNIAsisSeguridad.getText().toString().substring(9,16);
        final String pda = codPDA;
        final String tipopersonal = edtDNIAsisSeguridad.getText().toString().substring(0,1);
        final String fecha = fechaActual();
        final String fechaproceso = fechaP;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveNameToLocalStorage( NAME_SYNCED_WITH_SERVER, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveNameToLocalStorage( NAME_NOT_SYNCED_WITH_SERVER, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveNameToLocalStorage( NAME_NOT_SYNCED_WITH_SERVER, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("codbarra", codbarra);
                params.put("fecha", fecha);
                params.put("pda", pda);
                // codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //saving the name to local storage

    private void saveNameToLocalStorage(int status, String codbarra, String idsupervisor, String idobrero, String pda, String tipopersonal, String fecha, String fechaproceso) {

        edtDNIAsisSeguridad.setText("");

        db.addName(status, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso);
        Etiqueta n = new Etiqueta(status, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso);
        etiquetas.add(n);
        refreshList();
        cargarTipoIS();
        tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());
    }


    // ATRIBUTOS DE DISPOSITIVO
    public String fabricante(){
        return Build.MANUFACTURER;
    }
    public String modelo(){
        return Build.MODEL;
    }

    private String hostname(){
        return db.seriePda();
    }

    private String macbluetooth(){
        //return UUID.randomUUID().toString();
        return db.seriePda();
    }

    private String seriePda(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        }else{
            return Build.SERIAL;
        }
    }

    @Override
    public void onClick(View view) {
        saveNameToServer();
        cargarTipoIS();
        //cargarPreferenciasTraslado();
        tvConteoAsistencia.setText(db.etiquetasSync()+"/"+db.etiquetasTotal());
        //tvConteoAsistencia.setText(db.totalISTraslado(tipoIS, placaBus));
    }

    /*
    @Override
    public void onBackPressed() {
        //Toast.makeText(getApplicationContext(),"PResione",Toast.LENGTH_LONG).show();
        Intent i =new Intent(getApplicationContext(), Main_Seguridad.class);
        startActivity(i);

    }*/

    private int mYear, mMonth, mDay, mHour, mMinute, mSec;


    public String fechaActual(){
        String fecha = "";
        fecha = (DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString());
        return fecha;
    }
/*
    @Override
    protected void onPause() {
        super.onPause();
        finishAffinity();
    }*/

    private void saveNameMA(final int id, final int status, final String codbarra, final String idsupervisor, final String idobrero, final String pda, final String tipopersonal, final String fecha, final String fechaproceso ) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Main_Etiqueta.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateNameStatus(id, Main_Etiqueta.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Main_Etiqueta.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("codbarra", codbarra);
                params.put("idsupervisor", idsupervisor);
                params.put("idobrero", idobrero);
                params.put("pda", pda);
                params.put("tipopersonal", tipopersonal);
                params.put("fecha", fecha);
                params.put("fechaproceso", fechaproceso);
                return params;
                // status, codbarra,  idsupervisor,  idobrero,  pda,  tipopersonal,  fecha,  fechaproceso
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    // Otros métodos

    private void cargarPreferencias(){
        SharedPreferences preferencias = getSharedPreferences
                ("datosPedateador", Context.MODE_PRIVATE);
        codPDA = preferencias.getString("idpetateador", "No existe Info");
        idVigilante = preferencias.getString("idpda", "No existe Info");
    }

    private void cargarTipoIS(){
        SharedPreferences preferencias = getSharedPreferences
                ("datosTipoIS", Context.MODE_PRIVATE);
        tipoIS = preferencias.getString("idTipoIS", "No existe Info");
        descIS = preferencias.getString("descTipoIS", "No existe Info");
        //tvTitulo.setText("Registro de "+descIS);
    }



    private void establecerFechas(){
        String mes, dia;
        Integer anio;
        anio = 2000 + Integer.parseInt(edtDNIAsisSeguridad.getText().toString().substring(28,30));
        mes = edtDNIAsisSeguridad.getText().toString().substring(26,28);
        dia = edtDNIAsisSeguridad.getText().toString().substring(24,26);
        fechaP = anio.toString()+"-"+mes+"-"+dia;

        fechacompara = Integer.parseInt(anio+mes+dia);
        fechahoy = Integer.parseInt((DateFormat.format("yyyyMMdd", System.currentTimeMillis()).toString()));

    }

}