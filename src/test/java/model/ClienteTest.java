package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void testCreacionCliente() {
        // Arrange (Preparación)
        int id = 1;
        int dni = 12345678;
        String nombre = "Juan Pérez";
        String email = "juan@example.com";
        String telefono = "123456789";

        // Act (Ejecución)
        Cliente cliente = new Cliente(id, dni, nombre, email, telefono);

        // Assert (Verificación)
        assertEquals(id, cliente.getId(), "El ID debería ser 1");
        assertEquals(dni, cliente.getDni(), "El DNI debería coincidir");
        assertEquals(nombre, cliente.getNombreApellido(), "El nombre debería coincidir");
        assertEquals(email, cliente.getEmail(), "El email debería coincidir");
        assertEquals(telefono, cliente.getTelefono(), "El teléfono debería coincidir");
    }

    @Test
    void testClienteEmailVacio() {
        // Prueba específica para el email opcional
        Cliente cliente = new Cliente(2, 87654321, "María López", "", "987654321");
        
        assertTrue(cliente.getEmail().isEmpty(), "El email debería estar vacío");
    }
}
