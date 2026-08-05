package controller;

import model.Cliente;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClienteControllerTest {

    @Test
    void testListarClientes() {
        ClienteController controller = new ClienteController();
        
        // Ejecutamos el listado a través del controlador
        List<Cliente> clientes = controller.listar();
        
        // Verificamos que el controlador se comunique correctamente con el DAO
        assertNotNull(clientes);
    }
}
