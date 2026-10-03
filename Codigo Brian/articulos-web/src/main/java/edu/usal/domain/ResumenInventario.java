package edu.usal.domain;

/** Resultado del SP sp_resumen_inventario. */
public class ResumenInventario {

    private final int cantidadArticulos;
    private final int stockTotal;
    private final double valorTotal;

    public ResumenInventario(int cantidadArticulos, int stockTotal, double valorTotal) {
        this.cantidadArticulos = cantidadArticulos;
        this.stockTotal = stockTotal;
        this.valorTotal = valorTotal;
    }

    public int getCantidadArticulos() { return cantidadArticulos; }
    public int getStockTotal() { return stockTotal; }
    public double getValorTotal() { return valorTotal; }
}
