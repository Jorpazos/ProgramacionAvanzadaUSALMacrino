package edu.usal.model;

import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {

    private List<Productos> listaProductos;

    public ProductosDAO() {
        this.listaProductos = new ArrayList<>();
        this.listaProductos.add(new Productos(1, "Placa de Video", 420000));
        this.listaProductos.add(new Productos(2, "Procesador 4 nucleos", 280000.50));
        this.listaProductos.add(new Productos(3, "Memoria Ram 16GB", 210500));

    }

    public List<Productos> getAll(){
        return this.listaProductos;
    }
}
