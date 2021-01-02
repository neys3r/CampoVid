package com.agricolalaventa.campovid.db;

import android.provider.BaseColumns;

public class DBContract {

    public static  final String DATABASE_NAME = "campo.db";
    public static final String TABLE_CONTACTOS = "contactos";
    public static final String TABLE_ASISTENCIA = "checkinout";
    public static final String TABLE_PDAS = "pdas";
    public static final String TABLE_VIGILANTES = "vigilantes";
    public static final String TABLE_MOV_LECTOR= "mov_lector";
    public static final String TABLE_GRUPO_PERSONAL_DET= "grupopersonal_det";
    public static final String TABLE_CONTADORES= "Contadores";
    public static final String VISTA_CONTEOETIQUETAS= "conteo_etiquetas";
    public static final String VISTA_DNIS= "conteo_dnis";

    public static class Contactos implements BaseColumns {
        public static String ID = "id";
        public static String NOMBRE = "nombre";
        public static String APELLIDOS = "apellidos";
        public static String TELEFONO = "telefono";
    }

    public static class Contadores implements BaseColumns {
        public static String TRABAJADOR = "trabajador";
        public static String ETIQUETA = "etiqueta";
    }

    public static class MovimientoLectorCampo implements BaseColumns {
        public static String ID = "id";
        public static String CODBARRA = "codbarra";
        public static String IDSUPERVISOR = "idsupervisor";
        public static String IDOBRERO = "idobrero";
        public static String PDA = "pda";
        public static String TIPOPERSONAL = "tipopersonal";
        public static String FECHA = "fecha";
        public static String FECHAPROCESO = "fechaproceso";
        public static String STATUS = "status";
    }

    public static class Checkinout implements BaseColumns {
        public static String ID = "id";
        public static String STATUS = "status";
        public static String DNI = "dni";
        public static String IDREFERENCIA = "idreferencia";
        public static String IDSUCURSAL = "idsucursal";
        public static String IDPDA = "idpda";
        public static String FECHA = "fecha";
        public static String PEDATEADOR = "pedateador";
        public static String IDTRASLADO = "idtraslado";
        public static String IDTIPO = "idtipo";
    }

    public static class Pdas implements BaseColumns{
        public static String IDPDA = "idpda";
        public static String NOMBRE = "nombre";
        public static String IDSUCURSAL = "idsucursal";
        public static String DESCSUCURSAL = "descsucursal";

    }

    public static class Vigilantes implements BaseColumns{
        public static String IDVIGILANTE = "idvigilante";
        public static String NOMBRES = "nombres";
        public static String ESTADO = "estado";
        public static String IDAREA = "idarea";
    }

    public static class GrupoPersonalDet implements BaseColumns{
        public static String ID = "id";
        public static String FECHA = "fecha";
        public static String IDCODIGOGENERAL = "idcodigogeneral";
        public static String IDCODIGOGENERALDET = "idcodigogeneraldet";
        public static String ETIQUETA = "etiqueta";
        public static String ESPAREJA = "espareja";
        public static String DNI01 = "dni01";
        public static String DNI02 = "dni02";
        public static String IDTIPOPERSONAL = "idtipopersonal";
        public static String FECHAREGISTRO = "fecharegistro";
        public static String STATUS = "status";
    }

    // VISTAS

    public static class ConteoEtiquetas implements BaseColumns{
        public static String IDOBRERO = "idobrero";
        public static String CANTIDAD = "cantidad";
        public static String FECHAPROCESO = "fechaproceso";
    }

    public static class ConteoDNI implements BaseColumns{
        public static String IDOBRERO = "idobrero";
        public static String DNI = "dni";
        public static String FECHA = "fecha";
    }


}

