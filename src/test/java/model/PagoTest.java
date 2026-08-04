package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void testCreacionPagoConValoresPorDefecto() {
        // Instanciamos el pago con los campos obligatorios
        Pago pago = new Pago(1, 5000.0, 10);

        // Verificamos que respete lo ingresado
        assertEquals(1, pago.getId());
        assertEquals(5000.0, pago.getMontoPagado());
        assertEquals(10, pago.getEventoId());
        
        // Comprobamos que asigne correctamente los valores por defecto (efectivo y fecha actual)
        assertNull(pago.getPagador());
        assertEquals("Efectivo", pago.getMetodoPago());
        assertNotNull(pago.getFechaPago());
        assertEquals(LocalDate.now(), pago.getFechaPago());
    }

    @Test
    void testCreacionPagoCompleto() {
        // Configuramos una fecha personalizada para probar el constructor completo
        LocalDate fecha = LocalDate.of(2025, 10, 15);
        Pago pago = new Pago(2, 12500.0, 11, "Carlos López", "Transferencia", fecha);

        // Validamos la correcta asignación de todos los datos
        assertEquals(2, pago.getId());
        assertEquals(12500.0, pago.getMontoPagado());
        assertEquals(11, pago.getEventoId());
        assertEquals("Carlos López", pago.getPagador());
        assertEquals("Transferencia", pago.getMetodoPago());
        assertEquals(fecha, pago.getFechaPago());
    }
}
