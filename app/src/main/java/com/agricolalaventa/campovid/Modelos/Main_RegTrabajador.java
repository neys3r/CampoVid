package com.agricolalaventa.campovid.Modelos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agricolalaventa.campovid.Network.NetworkStateCheckerTrabajador;
import com.agricolalaventa.campovid.R;
import com.agricolalaventa.campovid.Clases.Trabajador;
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

public class Main_RegTrabajador extends AppCompatActivity implements View.OnClickListener {

    private EditText edtDNITrabajador, edtEtiquetaTrabajador;
    private TextView tvConteoTrabajadores;
    private Button btnDNIEtiqueta, btnReporteDNI;

    public static final String URL_SAVE_TRABAJADOR = "http://wslaventa.agricolalaventa.com/campo/wscampotrab.php";

    //database helper object
    private Context context;
    private DatabaseHelper db;
    private NetworkStateCheckerTrabajador nt;




    private String fechaLectura = (DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString());
    private String fechaLectura2 = (DateFormat.format("dd-MM-yyyy", System.currentTimeMillis()).toString());

    //List to store all the names
    private List<Trabajador> trabajadores;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "com.agricolalaventa.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    //private TrabajadorAdapter trabajadorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regtrabajador);

        registerReceiver(new NetworkStateCheckerTrabajador(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelper(this);
        trabajadores = new ArrayList<>();

        edtDNITrabajador = (EditText) findViewById(R.id.edtDNITrabajador);
        edtEtiquetaTrabajador = (EditText) findViewById(R.id.edtEtiquetaTrabajador);
        btnDNIEtiqueta = (Button) findViewById(R.id.btnDNIEtiqueta);
        tvConteoTrabajadores = (TextView) findViewById(R.id.tvConteoTrabajadores);
        btnReporteDNI = (Button) findViewById(R.id.btnReporteDNI);

        edtDNITrabajador.requestFocus();

        edtDNITrabajador.setInputType(InputType.TYPE_NULL);
        edtEtiquetaTrabajador.setInputType(InputType.TYPE_NULL);

        //tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());

        tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());

        btnDNIEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtDNITrabajador.length()!=8 || edtDNITrabajador.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "DNI incorrecto", Toast.LENGTH_LONG).show();
                    edtDNITrabajador.setText("");
                    edtEtiquetaTrabajador.setText("");
                    edtDNITrabajador.requestFocus();
                    tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                }else if(edtEtiquetaTrabajador.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Etiqueta vacia", Toast.LENGTH_LONG).show();
                    edtDNITrabajador.setText("");
                    edtEtiquetaTrabajador.setText("");
                    edtDNITrabajador.requestFocus();
                    tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                }else{
                    saveNameToServer();
                    edtDNITrabajador.setText("");
                    edtEtiquetaTrabajador.setText("");
                    edtDNITrabajador.requestFocus();
                    tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                }

            }
        });

        // ENTER
        edtEtiquetaTrabajador.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String codDNI = edtEtiquetaTrabajador.getText().toString();
                if((keyEvent.getAction()==KeyEvent.ACTION_DOWN ) && (i==KeyEvent.KEYCODE_ENTER)  ){

                    if (edtDNITrabajador.length()!=8 || edtDNITrabajador.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "DNI incorrecto", Toast.LENGTH_LONG).show();
                        edtDNITrabajador.setText("");
                        edtEtiquetaTrabajador.setText("");
                        edtDNITrabajador.requestFocus();
                        tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                    }else if(edtEtiquetaTrabajador.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Etiqueta vacia", Toast.LENGTH_LONG).show();
                        edtDNITrabajador.setText("");
                        edtEtiquetaTrabajador.setText("");
                        edtDNITrabajador.requestFocus();
                        tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                    }else{
                        saveNameToServer();
                        edtDNITrabajador.setText("");
                        edtEtiquetaTrabajador.setText("");
                        edtDNITrabajador.requestFocus();
                        tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
                    }
                    return true;
                }
                return false;
            }
        });

        btnReporteDNI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Main_ReporteDNI.class);
                //ii.putExtra("codPDA", codPDA);
                startActivity(i);
            }
        });

        registerReceiver(new NetworkStateCheckerTrabajador(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelper(this);
        trabajadores = new ArrayList<>();


    }

    private void loadNames() {
        trabajadores.clear();


        Cursor cursor = db.getTrabajadores();
        //Toast.makeText(getApplicationContext(), "placa: "+placaBus+tipoIS, Toast.LENGTH_LONG).show();
        if (cursor.moveToFirst()) {
            do {
                Trabajador registro = new Trabajador(
                        cursor.getInt(cursor.getColumnIndex(DBContract.GrupoPersonalDet.STATUS)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.FECHA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.IDCODIGOGENERAL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.IDCODIGOGENERALDET)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.ETIQUETA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.ESPAREJA)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.DNI01)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.DNI02)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.IDTIPOPERSONAL)),
                        cursor.getString(cursor.getColumnIndex(DBContract.GrupoPersonalDet.FECHAREGISTRO))
//fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro


                );
                trabajadores.add(registro);
            } while (cursor.moveToNext());
        }

        //trabajadorAdapter = new TrabajadorAdapter(this, R.layout.names, trabajadores);
        //listAsisSeguridad.setAdapter(trabajadorAdapter);
    }

    /*
     * this method will simply refresh the list
     * */
    private void refreshList() {
        //nameAdapter.notifyDataSetChanged();
        //tvConteoTrabajadores.setText(db.statusSync()+"/"+db.statusTotal());
        tvConteoTrabajadores.setText(db.trabSync()+"/"+db.trabTotal());
    }


    private void saveNameToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando Registro...");
        progressDialog.show();

        //cargarTipoIS();
        String mes, dia;
        Integer anio;
        anio = 2000 + Integer.parseInt(edtEtiquetaTrabajador.getText().toString().substring(28,30));
        mes = edtEtiquetaTrabajador.getText().toString().substring(26,28);
        dia = edtEtiquetaTrabajador.getText().toString().substring(24,26);
        String fechaP = anio.toString()+"-"+mes+"-"+dia;

        //final String idsupervisor = edtEtiquetaTrabajador.getText().toString().substring(6,9);
        final String fecha = fechaP;
        final String idcodigogeneral = edtEtiquetaTrabajador.getText().toString().substring(6,9);
        final String idcodigogeneraldet = edtEtiquetaTrabajador.getText().toString().substring(9,16);
        final String etiqueta = edtEtiquetaTrabajador.getText().toString().substring(0,29);
        final String espareja = "0";
        final String dni01 = edtDNITrabajador.getText().toString();
        final String dni02 = "";
        final String idtipopersonal = edtEtiquetaTrabajador.getText().toString().substring(0,1);
        final String fecharegistro = fechaActual();
        final String id = edtEtiquetaTrabajador.getText().toString().substring(0,29);
        //db.actualizarContadorTrab(db1);

        //fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_TRABAJADOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveNameToLocalStorage( NAME_SYNCED_WITH_SERVER, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveNameToLocalStorage( NAME_NOT_SYNCED_WITH_SERVER, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro);
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
                        saveNameToLocalStorage( NAME_NOT_SYNCED_WITH_SERVER, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", fecha);
                params.put("idcodigogeneral", idcodigogeneral);
                params.put("idcodigogeneraldet", idcodigogeneraldet);
                params.put("etiqueta", etiqueta);
                params.put("espareja", espareja);
                params.put("dni01", dni01);
                params.put("dni02", dni02);
                params.put("idtipopersonal", idtipopersonal);
                params.put("fecharegistro", fecharegistro);
                //fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void saveNameToLocalStorage(int status, String fecha, String idcodigogeneral, String idcodigogeneraldet, String etiqueta, String espareja, String dni01, String dni02, String idtipopersonal, String fecharegistro) {

        //edtDNIAsisSeguridad.setText("");

        db.addTrabajador(status, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro);
        Trabajador n = new Trabajador(status, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro);
        trabajadores.add(n);
        refreshList();
        //cargarTipoIS();
        //tvConteoAsistencia.setText(db.statusSync()+"/"+db.statusTotal());
    }
//fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro


    @Override
    public void onClick(View v) {
        saveNameToServer();
    }

    public String fechaActual(){
        String fecha = "";
        fecha = (DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString());
        return fecha;
    }
}