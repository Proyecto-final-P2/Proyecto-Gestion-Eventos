package model;

public class PagoPorCliente {
    private String cliente;
    private double totalPagado;

    public PagoPorCliente() {}

    public PagoPorCliente(String cliente, double totalPagado) {
        this.cliente = cliente;
        this.totalPagado = totalPagado;
    }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }
}