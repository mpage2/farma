/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modelo;

/**
 *
 * @author zarpa
 */
public class ProductoFacturacion {

    private int idDetalleVenta;
    private int idCabeceraVenta;
    private int idProducto;
    //llamar unicamente
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double descuento;
    private double igv;
    private double totalPagar;
    
    public ProductoFacturacion(){}

    public ProductoFacturacion(int idDetalleVenta, int idCabeceraVenta, int idProducto, String nombre, int cantidad, double precioUnitario, double subtotal, double descuento, double igv, double totalPagar) {
        this.idDetalleVenta = idDetalleVenta;
        this.idCabeceraVenta = idCabeceraVenta;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.igv = igv;
        this.totalPagar = totalPagar;
//        this.estado = estado;
    }
public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getIdCabeceraVenta() {
        return idCabeceraVenta;
    }

    public void setIdCabeceraVenta(int idCabeceraVenta) {
        this.idCabeceraVenta = idCabeceraVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

//public Object[] GestionarProductoFacturacion(){
//        Object[] fila =  {idDetalleVenta,idCabeceraVenta,idProducto,nombre,cantidad,precioUnitario,subtotal,descuento,igv,totalPagar}; 
//        return fila;
//    } 



}





































////
////
////public class ProductoFacturacion {
////
////    private Producto producto;
////    private double subtotal;
////    private double descuento;
////    private double montoIgv;
////    private double montoTotal;
////    
////    public ProductoFacturacion(){}
////
////    public ProductoFacturacion(Producto producto, double subtotal, double descuento, double montoIgv, double montoTotal) {
////        this.producto = producto;
////        this.subtotal = subtotal;
////        this.descuento = descuento;
////        this.montoIgv = montoIgv;
////        this.montoTotal = montoTotal;
////    }
////
////    public Producto getProducto() {
////        return producto;
////    }
////
////    public void setProducto(Producto producto) {
////        this.producto = producto;
////    }
////
////    public double getSubtotal() {
////        return subtotal;
////    }
////
////    public void setSubtotal(double subtotal) {
////        this.subtotal = subtotal;
////    }
////
////    public double getDescuento() {
////        return descuento;
////    }
////
////    public void setDescuento(double descuento) {
////        this.descuento = descuento;
////    }
////
////    public double getMontoIgv() {
////        return montoIgv;
////    }
////
////    public void setMontoIgv(double montoIgv) {
////        this.montoIgv = montoIgv;
////    }
////
////    public double getMontoTotal() {
////        return montoTotal;
////    }
////
////    public void setMontoTotal(double montoTotal) {
////        this.montoTotal = montoTotal;
////    }
////
////}
