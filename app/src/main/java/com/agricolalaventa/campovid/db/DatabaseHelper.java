package com.agricolalaventa.campovid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "security.db";
    public static final String VIEW_REPO01 = "repo01";

    //database version
    private static final int DB_VERSION = 1;



    public DatabaseHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //String sql2 = "CREATE VIEW " + VIEW_REPO01 + " as SELECT idreferencia, count(1) as cantidad FROM checkinout group by idreferencia ";
        //db.execSQL(sql2);

        createContactos(db);
        createPdas(db);
        createVigilantes(db);
        createAsistencia(db);
        createMovLectorCampo(db);
        createGrupoGeneralDet(db);
        createContadores(db);
        createVistaConteoEtiquetas(db);
        createVistaConteoDNIs(db);
    }

    // VISTAS
    public void createVistaConteoEtiquetas(SQLiteDatabase db)
    {
        db.execSQL("CREATE VIEW "+DBContract.VISTA_CONTEOETIQUETAS +" as select "+ DBContract.ConteoEtiquetas.IDOBRERO +
                ", count(*) as "+ DBContract.ConteoEtiquetas.CANTIDAD +", "+ DBContract.ConteoEtiquetas.FECHAPROCESO +
                " from mov_lector group by "+DBContract.ConteoEtiquetas.IDOBRERO+", "+DBContract.ConteoEtiquetas.FECHAPROCESO+";");
    }

    public void createVistaConteoDNIs(SQLiteDatabase db)
    {
        db.execSQL("CREATE VIEW "+DBContract.VISTA_DNIS +" as select idcodigogeneraldet as "+ DBContract.ConteoDNI.IDOBRERO +
                ", dni01 as "+ DBContract.ConteoDNI.DNI +", "+ DBContract.ConteoDNI.FECHA +
                " from grupopersonal_det;");
    }

    // FIN VISTAS
    public void createMovLectorCampo(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_MOV_LECTOR+"("+DBContract.MovimientoLectorCampo.ID+" INTEGER PRIMARY KEY ,"+
                ""+DBContract.MovimientoLectorCampo.CODBARRA +" VARCHAR UNIQUE,"+DBContract.MovimientoLectorCampo.IDSUPERVISOR+" VARCHAR,"+DBContract.MovimientoLectorCampo.IDOBRERO+" VARCHAR,"+
                ""+DBContract.MovimientoLectorCampo.PDA+" VARCHAR,"+DBContract.MovimientoLectorCampo.TIPOPERSONAL+" VARCHAR ,"+
                ""+DBContract.MovimientoLectorCampo.FECHA+" DATE DEFAULT (datetime('now','localtime')),"+DBContract.MovimientoLectorCampo.FECHAPROCESO+" VARCHAR,"+
                ""+DBContract.MovimientoLectorCampo.STATUS+" VARCHAR);");
    }

    public void createGrupoGeneralDet(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_GRUPO_PERSONAL_DET+"("+DBContract.GrupoPersonalDet.FECHA+" VARCHAR ,"+
                ""+DBContract.GrupoPersonalDet.IDCODIGOGENERAL +" VARCHAR ,"+DBContract.GrupoPersonalDet.IDCODIGOGENERALDET+" VARCHAR,"+
                ""+DBContract.GrupoPersonalDet.ETIQUETA +" VARCHAR UNIQUE,"+DBContract.GrupoPersonalDet.ESPAREJA+" VARCHAR,"+
                ""+DBContract.GrupoPersonalDet.DNI01 +" VARCHAR ,"+DBContract.GrupoPersonalDet.DNI02+" VARCHAR,"+
                ""+DBContract.GrupoPersonalDet.ID +" INTEGER ,"+DBContract.GrupoPersonalDet.IDTIPOPERSONAL +" VARCHAR ,"+DBContract.GrupoPersonalDet.STATUS +" VARCHAR ,"+DBContract.GrupoPersonalDet.FECHAREGISTRO+" DATE DEFAULT (datetime('now','localtime')), PRIMARY KEY (id));");
        // PRIMARY KEY (fecha,idcodigogeneral,idcodigogeneraldet)
    }

    public void createContadores(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_CONTADORES+"("+DBContract.Contadores.TRABAJADOR+" INTEGER DEFAULT 0,"+
                ""+DBContract.Contadores.ETIQUETA +" INTEGER DEFAULT 0);");
    }


    // Métodos SQLITE
    public void createAsistencia(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_ASISTENCIA+"("+DBContract.Checkinout.ID+" INTEGER PRIMARY KEY,"+
                ""+DBContract.Checkinout.STATUS+" VARCHAR,"+DBContract.Checkinout.DNI+" VARCHAR,"+DBContract.Checkinout.IDREFERENCIA+" VARCHAR,"+DBContract.Checkinout.IDSUCURSAL+" VARCHAR,"+
                ""+DBContract.Checkinout.IDPDA+" VARCHAR,"+DBContract.Checkinout.FECHA+" VARCHAR,"+DBContract.Checkinout.PEDATEADOR+" VARCHAR,"+
                ""+DBContract.Checkinout.IDTRASLADO+" VARCHAR,"+DBContract.Checkinout.IDTIPO+" VARCHAR);");
    }

    public void createContactos(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_CONTACTOS+"("+DBContract.Contactos.ID+" INTEGER PRIMARY KEY,"+DBContract.Contactos.NOMBRE+" TEXT," +
                ""+DBContract.Contactos.APELLIDOS+" TEXT,"+DBContract.Contactos.TELEFONO+" TEXT);");
    }

    public void createPdas(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_PDAS+"("+DBContract.Pdas.IDPDA+" TEXT PRIMARY KEY,"+DBContract.Pdas.NOMBRE+" TEXT," +
                ""+DBContract.Pdas.IDSUCURSAL+" TEXT,"+DBContract.Pdas.DESCSUCURSAL+" TEXT);");
    }

    public void createVigilantes(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+DBContract.TABLE_VIGILANTES+"("+DBContract.Vigilantes.IDVIGILANTE+" TEXT PRIMARY KEY,"+DBContract.Vigilantes.NOMBRES+" TEXT," +
                ""+DBContract.Vigilantes.ESTADO+" TEXT,"+DBContract.Vigilantes.IDAREA+" TEXT);");
    }

    public void savePdas(String idpda,String nombre,String idsucursal,String descsucursal, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Pdas.IDPDA,idpda);
        contentValues.put(DBContract.Pdas.NOMBRE,nombre);
        contentValues.put(DBContract.Pdas.IDSUCURSAL,idsucursal);
        contentValues.put(DBContract.Pdas.DESCSUCURSAL,descsucursal);

        db.insert(DBContract.TABLE_PDAS,null, contentValues);
    }

    public void saveVigilantes(String idvigilante,String nombres,String estado,String idarea, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Vigilantes.IDVIGILANTE,idvigilante);
        contentValues.put(DBContract.Vigilantes.NOMBRES,nombres);
        contentValues.put(DBContract.Vigilantes.ESTADO,estado);
        contentValues.put(DBContract.Vigilantes.IDAREA,idarea);

        db.insert(DBContract.TABLE_VIGILANTES,null, contentValues);
    }

    // Fin Metodos SQLITE

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }


    public boolean addName( int status, String codbarra, String idsupervisor, String idobrero, String pda, String tipopersonal, String fecha, String fechaproceso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);
        contentValues.put("codbarra", codbarra);
        contentValues.put("idsupervisor", idsupervisor);
        contentValues.put("idobrero", idobrero);
        contentValues.put("pda", pda);
        contentValues.put("tipopersonal", tipopersonal);
        contentValues.put("fecha", fecha);
        contentValues.put("fechaproceso", fechaproceso);

        db.insert("mov_lector", null, contentValues);
        db.close();
        return true;
    }

    public boolean addTrabajador( int status, String fecha, String idcodigogeneral, String idcodigogeneraldet, String etiqueta, String espareja, String dni01, String dni02, String idtipopersonal, String fecharegistro) {
        //fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);
        contentValues.put("fecha", fecha);
        contentValues.put("idcodigogeneral", idcodigogeneral);
        contentValues.put("idcodigogeneraldet", idcodigogeneraldet);
        contentValues.put("etiqueta", etiqueta);
        contentValues.put("espareja", espareja);
        contentValues.put("dni01", dni01);
        contentValues.put("dni02", dni02);
        contentValues.put("idtipopersonal", idtipopersonal);
        contentValues.put("fecharegistro", fecharegistro);

        db.insert("grupopersonal_det", null, contentValues);
        db.close();
        return true;
    }



    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("mov_lector", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateTrabajadorStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("grupopersonal_det", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public void actualizarContadorTrab(SQLiteDatabase db){
        db.execSQL("UPDATE grupopersonal_det SET trabajador = trabajador + 1");
    }

    public Cursor getNames(String placa, String idtipo) {
        //String prueba = "MMMMMM";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, status, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso FROM mov_lector ";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getTrabajadores() {
        //String prueba = "MMMMMM";
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, status, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso FROM mov_lector ";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, status, codbarra, idsupervisor, idobrero, pda, tipopersonal, fecha, fechaproceso FROM mov_lector" + " WHERE status" + " = 0 ;";
        //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  " + COLUMN_STATUS + " = 0 ;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedTrabajadores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, status, fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro FROM grupopersonal_det" + " WHERE status" + " = 0 ;";
        //String sql = "SELECT * FROM " + TABLE_NAME + " WHERE  " + COLUMN_STATUS + " = 0 ;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public String totalDiario(){
        String lsViaje="0";
        String sql="select count(1) from checkinout WHERE length(dni) = 8 and strftime('%Y-%m-%d',fecha) = date('now','localtime')";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String miIdSucursal(){
        String lsViaje="0";
        String sql="SELECT idsucursal FROM pdas where idpda = '"+ seriePda() +"';";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String seriePda(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        }else{
            return Build.SERIAL;
        }
    }

    public String miDescSucursal(){
        String lsViaje="0";
        String sql="SELECT descsucursal FROM pdas where idpda = '"+ Build.SERIAL +"';";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String existeVigilante(String dni){
        String lsViaje="0";
        String sql="SELECT idvigilante from vigilantes where idvigilante = '"+dni+"'";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }


    // #####################################
    // ------- CONTADOR DE ETIQUETAS -------
    // #####################################

    public String etiquetasSync(){
        String lsViaje="0";
        String sql="select count(1) as cantidad from mov_lector where status = 1  and strftime('%Y-%m-%d',fechaproceso) = date('now','localtime') group by status ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String etiquetasTotal(){
        String lsViaje="0";
        String sql="select count(1) as cantidad from mov_lector where strftime('%Y-%m-%d',fechaproceso) = date('now','localtime') ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    // #########################################
    // ------- FIN CONTADOR DE ETIQUETAS -------
    // #########################################

    // ######################################
    // ------- CONTADOR DE TRABAJADOR -------
    // ######################################

    public String trabSync(){
        String lsViaje="0";
        String sql="select count(1) as cantidad from grupopersonal_det where status = 1  and strftime('%Y-%m-%d',fecha) = date('now','localtime') group by status ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String trabTotal(){
        String lsViaje="0";
        String sql="select count(1) as cantidad from grupopersonal_det where strftime('%Y-%m-%d',fecha) = date('now','localtime') ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    // ##########################################
    // ------- FIN CONTADOR DE TRABAJADOR -------
    // ##########################################

    public String areaVigilante(String dni){
        String lsViaje="0";
        String sql="SELECT idarea from vigilantes where idvigilante = '"+dni+"'";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }


    public ArrayList<HashMap<String, String>> qrepo02(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "select idreferencia, cantidad from repo01 ";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("idreferencia",cursor.getString(cursor.getColumnIndex("idreferencia")));
            user.put("cantidad",cursor.getString(cursor.getColumnIndex("cantidad")));
            userList.add(user);
        }
        return  userList;
    }

    public String totalSync(){
        String lsViaje="0";
        String sql="select count(1) from checkinout WHERE length(dni) = 8 and status = 1 and strftime('%Y-%m-%d',fecha) = date('now','localtime')";
        //String sql="select count(1) from checkinout WHERE length(dni) = 8 and status = 1 and strftime('%Y-%m-%d',fecha) = strftime('%Y-%m-%d','now')";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String totalIngresos(){
        String lsViaje="0";
        String sql="select count(1) from checkinout WHERE length(dni) = 8 and status = 1 and strftime('%Y-%m-%d',fecha) = date('now','localtime') and idtipo = '0' ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    public String totalSalidas(){
        String lsViaje="0";
        String sql="select count(1) from checkinout WHERE length(dni) = 8 and status = 1 and strftime('%Y-%m-%d',fecha) = date('now','localtime') and idtipo = '1' ";
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                lsViaje = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsViaje;
    }

    // OBREROS

    public ArrayList<HashMap<String, String>> GetObreros(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

        String query = "SELECT idobrero, dni FROM conteo_dnis where fecha = date('now','localtime') ";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("idobrero",cursor.getString(cursor.getColumnIndex("idobrero")));
            user.put("dni",cursor.getString(cursor.getColumnIndex("dni")));
            userList.add(user);
        }
        return  userList;
    }

    public String totalSimple(){
        String lsTotal="0";
        // Prueba de localtime
        String sql="select count(1) from grupopersonal_det where esPareja = '0' and fecha = date('now') " ;
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do {
                lsTotal = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsTotal;
    }

    // Reporte Etiquetas

    public ArrayList<HashMap<String, String>> GetUsers(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "select idobrero, cantidad from conteo_etiquetas where fechaproceso = date('now','localtime') ";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("idobrero",cursor.getString(cursor.getColumnIndex("idobrero")));
            user.put("cantidad",cursor.getString(cursor.getColumnIndex("cantidad")));
            userList.add(user);
        }
        return  userList;
    }

    public String totalTipo(){
        String lsTotal="0";

        String sql="select count(1)  from mov_lector  where fechaproceso = date('now','localtime')" ;
        /* and substring(codbarra,25,30) = '24112019' */
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor= db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                //mov_lectorEn item=new mov_lectorEn( cursor.getString(0),cursor.getString(1));
                //mov_lectorList.add(item);
                lsTotal = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return lsTotal;
    }





}
