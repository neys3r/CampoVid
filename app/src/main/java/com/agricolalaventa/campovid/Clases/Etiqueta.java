package com.agricolalaventa.campovid.Clases;

public class Etiqueta {
    private String codbarra, fecha, pda, idsupervisor, idobrero, tipopersonal, fechaproceso;
    private int status;

    public Etiqueta( int status, String codbarra, String idsupervisor, String idobrero, String pda, String tipopersonal, String fecha, String fechaproceso) {
        this.status = status;
        this.idsupervisor = idsupervisor;
        this.idobrero = idobrero;
        this.pda = pda;
        this.tipopersonal = tipopersonal;
        this.fecha = fecha;
        this.fechaproceso = fechaproceso;
    }

    public String getCodbarra() {
        return codbarra;
    }

    public void setCodbarra(String codbarra) {
        this.codbarra = codbarra;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPda() {
        return pda;
    }

    public void setPda(String pda) {
        this.pda = pda;
    }

    public String getIdsupervisor() {
        return idsupervisor;
    }

    public void setIdsupervisor(String idsupervisor) {
        this.idsupervisor = idsupervisor;
    }

    public String getIdobrero() {
        return idobrero;
    }

    public void setIdobrero(String idobrero) {
        this.idobrero = idobrero;
    }

    public String getTipopersonal() {
        return tipopersonal;
    }

    public void setTipopersonal(String tipopersonal) {
        this.tipopersonal = tipopersonal;
    }

    public String getFechaproceso() {
        return fechaproceso;
    }

    public void setFechaproceso(String fechaproceso) {
        this.fechaproceso = fechaproceso;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
