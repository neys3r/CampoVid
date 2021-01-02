package com.agricolalaventa.campovid.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.agricolalaventa.campovid.Modelos.Main_RegTrabajador;
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

import java.util.HashMap;
import java.util.Map;

public class NetworkStateCheckerTrabajador extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelper db;


    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedTrabajadores();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(DBContract.GrupoPersonalDet.ID)),
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
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    private void saveName(final int id, final String fecha, final String idcodigogeneral, final String idcodigogeneraldet, final String etiqueta, final String espareja, final String dni01, final String dni02, final String idtipopersonal, final String fecharegistro) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Main_RegTrabajador.URL_SAVE_TRABAJADOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateTrabajadorStatus(id, Main_RegTrabajador.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Main_RegTrabajador.DATA_SAVED_BROADCAST));
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
