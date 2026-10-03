package edu.usal.domain;

public class Articulo {

    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Articulo() {
    }

    public Articulo(String codigo, String nombre, String descripcion, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public Articulo(int id, String codigo, String nombre, String descripcion, double precio, int stock) {
        this(codigo, nombre, descripcion, precio, stock);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Articulo{id=" + id + ", codigo='" + codigo + "', nombre='" + nombre +
                "', precio=" + precio + ", stock=" + stock + '}';
    }
}
