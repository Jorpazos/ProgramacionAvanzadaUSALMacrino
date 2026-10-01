package edu.usal.model;

public class Producto {

    private String nombre;
    private double precio;
    private boolean enStock;

    public Producto(String nombre, double precio, boolean enStock) {
        this.nombre = nombre;
        this.precio = precio;
        this.enStock = enStock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEnStock() {
        return enStock;
    }

    public void setEnStock(boolean enStock) {
        this.enStock = enStock;
    }
}
