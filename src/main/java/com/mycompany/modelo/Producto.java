package com.mycompany.modelo;

public class Producto {
   
    private int IdProducto;
    private String nombre;
    private int cantidad;
    private double precio;
    private String descripcion;
    private int porcentajeIgv;
    private String categoria; //Nombre de la categoria
    private int IdCategoria;
//    private int estado;
    
    //constructor
    public Producto(){
        this.IdProducto = 0;
        this.nombre = "";
        this.cantidad = 0;
        this.precio = 0.0;
        this.descripcion = "";
        this.porcentajeIgv = 0;
        this.IdCategoria = 0;
//        this.estado = 0;
             
    }

    public Producto(int IdProducto, String nombre, int cantidad, double precio, String descripcion, int porcentajeIgv, int IdCategoria) {
        this.IdProducto = IdProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descripcion = descripcion;
        this.porcentajeIgv = porcentajeIgv;
        this.IdCategoria = IdCategoria;
//        this.estado = estado;
    }

    public int getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(int IdProducto) {
        this.IdProducto = IdProducto;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPorcentajeIgv() {
        
        return porcentajeIgv;
    }

    public void setPorcentajeIgv(int porcentajeIgv) {
        this.porcentajeIgv = porcentajeIgv;
    }

    public int getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(int IdCategoria) {
        this.IdCategoria = IdCategoria;
    }
    public String getCategoria()                    {   return categoria;               }
    public void setCategoria(String categoria)      {   this.categoria = categoria;     }

 
    public Object[] GestionarProducto(){
        Object[] fila =  {IdProducto,nombre,cantidad,precio,descripcion,porcentajeIgv,categoria}; 
       
        return fila;
    }
    

    

    
}
