package controller;

import model.PagoPorCliente;
import repository.ReporteDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteController {
    private ReporteDAO dao;

    public ReporteController() {
        this.dao = new ReporteDAO();
    }

    public List<PagoPorCliente> listarPagosPorCliente() {
        try {
            return dao.obtenerPagosPorCliente();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de pagos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}