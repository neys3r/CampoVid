package com.agricolalaventa.campovid.Clases;

public class Trabajador {
    private String fecha ,idcodigogeneral ,idcodigogeneraldet ,etiqueta ,espareja ,dni01 ,dni02 ,idtipopersonal ,fecharegistro;
    private int status;

    public Trabajador( int status, String fecha, String idcodigogeneral, String idcodigogeneraldet, String etiqueta, String espareja, String dni01, String dni02, String idtipopersonal, String fecharegistro) {
        this.status = status;
        this.fecha = fecha;
        this.idcodigogeneral = idcodigogeneral;
        this.idcodigogeneraldet = idcodigogeneraldet;
        this.etiqueta = etiqueta;
        this.espareja = espareja;
        this.dni01 = dni01;
        this.dni02 = dni02;
        this.idtipopersonal = idtipopersonal;
        this.fecharegistro = fecharegistro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdcodigogeneral() {
        return idcodigogeneral;
    }

    public void setIdcodigogeneral(String idcodigogeneral) {
        this.idcodigogeneral = idcodigogeneral;
    }

    public String getIdcodigogeneraldet() {
        return idcodigogeneraldet;
    }

    public void setIdcodigogeneraldet(String idcodigogeneraldet) {
        this.idcodigogeneraldet = idcodigogeneraldet;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEspareja() {
        return espareja;
    }

    public void setEspareja(String espareja) {
        this.espareja = espareja;
    }

    public String getDni01() {
        return dni01;
    }

    public void setDni01(String dni01) {
        this.dni01 = dni01;
    }

    public String getDni02() {
        return dni02;
    }

    public void setDni02(String dni02) {
        this.dni02 = dni02;
    }

    public String getIdtipopersonal() {
        return idtipopersonal;
    }

    public void setIdtipopersonal(String idtipopersonal) {
        this.idtipopersonal = idtipopersonal;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

