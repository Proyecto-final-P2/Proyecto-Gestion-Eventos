package controller;

import java.util.ArrayList;
import repository.ReporteDAO;
import java.sql.SQLException;
import java.util.List;

public class ReporteController {
    private final ReporteDAO dao;

    public ReporteController() {
        this.dao = new ReporteDAO();
    }

    // --- REPORTES FINANCIEROS Y ESTADÍSTICOS ---

    public List<Object[]> listarPagosPorEvento() {
        try {
            return dao.obtenerPagosPorEvento();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de pagos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarServiciosPorTipo() {
        try {
            return dao.obtenerServiciosPorTipo();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de servicios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarEventosCostosos() {
        try {
            return dao.obtenerEventosCostosos();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de eventos costosos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Object[]> listarTop5EventosCaros() {
        try {
            return dao.obtenerTop5EventosCaros();
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de eventos caros: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // --- REPORTES OPERATIVOS (Eventos Confirmados) ---

    public List<Object[]> getEventosConfirmados() {
        return dao.getEventosConfirmados();
    }

    public List<Object[]> getEventosPorSalon(String salon) {
        return dao.getEventosPorSalon(salon);
    }

    public List<String> getSalones() {
        return dao.getSalones();
    }

    public String[] getColumnNames() {
        return dao.getColumnNames();
    }

    public List<Object[]> getServiciosContratados(int eventoId) {
        return dao.getServiciosContratados(eventoId);
    }

    public List<Object[]> getInvitadosEvento(int eventoId) {
        return dao.getInvitadosEvento(eventoId);
    }
}