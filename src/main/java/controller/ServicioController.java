package controller;

import model.Servicio;
import repository.ServicioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioController {
    private ServicioDAO dao;

    public ServicioController() {
        this.dao = new ServicioDAO();
    }

    // Trae la lista de la BD, si hay error devuelve lista vacía
    public List<Servicio> listarServicios() {
        try {
            return dao.listar();
        } catch (SQLException e) {
            System.err.println("Error al listar servicios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Busca servicios que coincidan con el proveedor
    public List<Servicio> buscar(String proveedor) {
        try {
            return dao.buscarPorProveedor(proveedor);
        } catch (SQLException e) {
            System.err.println("Error al buscar servicios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Guarda y devuelve true si fue un éxito
    public boolean guardarServicio(Servicio s) {
        try {
            dao.insertar(s);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar servicio: " + e.getMessage());
            return false;
        }
    }

    // Actualiza y devuelve true si fue un éxito
    public boolean actualizarServicio(Servicio s) {
        try {
            dao.actualizar(s);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar servicio: " + e.getMessage());
            return false;
        }
    }

    // Elimina y devuelve true si fue un éxito
    public boolean eliminarServicio(int id) {
        try {
            dao.eliminar(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar servicio: " + e.getMessage());
            return false;
        }
    }
}