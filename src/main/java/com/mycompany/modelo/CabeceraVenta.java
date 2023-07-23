package com.mycompany.modelo;

public class CabeceraVenta {

    /*Atributos*/
    private int idCabeceraventa;
    private int idCliente;
    private String nombresCliente;
    private double valorPagar;
    private String fechaVenta;
    private int estado;

    /*Constructor*/
    public CabeceraVenta() {
        this.idCabeceraventa = 0;
        this.idCliente = 0;
        this.valorPagar = 0.0;
        this.fechaVenta = "";
        this.estado = 0;
    }
    /*Constructor sobrecargado*/

    public CabeceraVenta(int idCabeceraventa, int idCliente, double valorPagar, String fechaVenta, int estado) {
        this.idCabeceraventa = idCabeceraventa;
        this.idCliente = idCliente;
        this.valorPagar = valorPagar;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
    }
    //getter and setter

    public int getIdCabeceraventa() {
        return idCabeceraventa;
    }

    public void setIdCabeceraventa(int idCabeceraventa) {
        this.idCabeceraventa = idCabeceraventa;
    }

    public int getIdCliente() {
        return idCliente;
    }
    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(double valorPagar) {
        this.valorPagar = valorPagar;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    //toString

    public  Object[] GestionarVenta() {
        Object[] fila =  {idCabeceraventa,idCliente, valorPagar,fechaVenta,estado};
        return fila;
    }
    
}
