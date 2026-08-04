package controller;

import model.Pago;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PagoControllerTest {

    @Test
    void testListarPorClienteInexistente() {
        PagoController controller = new PagoController();
        
        // Validamos el filtrado de pagos para un cliente que no existe
        List<Pago> pagos = controller.listarPorCliente(999999);
        assertNotNull(pagos);
        assertTrue(pagos.isEmpty());
    }

    @Test
    void testListarTodos() {
        PagoController controller = new PagoController();
        
        // Verificamos que el controlador pueda listar todos los pagos sin errores
        List<Pago> pagos = controller.listar();
        assertNotNull(pagos);
    }

    @Test
    void testBuscarPorIdInexistente() {
        PagoController controller = new PagoController();
        
        // Probamos la protección del controlador ante IDs negativos
        Pago pago = controller.buscarPorId(-1); 
        assertNull(pago);
    }

    @Test
    void testListarPorEventoInexistente() {
        PagoController controller = new PagoController();
        
        // Verificamos que devuelva una lista vacía en lugar de un error
        List<Pago> pagos = controller.listarPorEvento(999999);
        assertNotNull(pagos);
        assertTrue(pagos.isEmpty());
    }
}
