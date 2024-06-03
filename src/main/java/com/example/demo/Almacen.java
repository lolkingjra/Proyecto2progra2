package com.example.demo;

public class Almacen {
    private String nombreComprador;
    private String producto;
    private int precio;
    private int almacen_id;

    

    public int getAlmacen_id() {
        return almacen_id;
    }

    public void setAlmacen_id(int almacen_id) {
        this.almacen_id = almacen_id;
    }

    public Almacen() {

    }

    /**
     * @param nombreComprador
     * @param producto
     * @param precio
     * @param almacen_id
     */
    public Almacen(String nombreComprador, String producto, int precio, int almacen_id) {
        this.nombreComprador = nombreComprador;
        this.precio = precio;
        this.producto = producto;
        this.almacen_id = almacen_id;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getIdComprador() {
        return almacen_id;
    }

    public void setIdComprador(int almacen_id) {
        this.almacen_id = almacen_id;
    }
}