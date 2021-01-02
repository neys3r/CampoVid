package com.agricolalaventa.campovid.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.agricolalaventa.campovid.Modelos.Main_Etiqueta;
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

public class NetworkStateCheckerEtiqueta extends BroadcastReceiver {

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
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.ID)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.CODBARRA)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.IDSUPERVISOR)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.IDOBRERO)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.PDA)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.TIPOPERSONAL)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.FECHA)),
                                cursor.getString(cursor.getColumnIndex(DBContract.MovimientoLectorCampo.FECHAPROCESO))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    private void saveName(final int id, final String codbarra, final String idsupervisor, final String idobrero, final String pda, final String tipopersonal, final String fecha, final String fechaproceso) {

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
                //id,  status,  codbarra,  idsupervisor, idobrero,  pda,  tipopersonal, fecha,  fechaproceso
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
