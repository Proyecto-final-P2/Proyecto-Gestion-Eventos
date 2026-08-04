package repository;

import model.Cliente;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {

    @Test
    void testListarClientes() throws Exception {
        ClienteDAO dao = new ClienteDAO();
        
        // Ejecutamos la consulta a la base de datos
        List<Cliente> clientes = dao.listar();
        
        // Verificamos que la conexión se haya realizado correctamente y devuelva una lista (vacía o con datos)
        assertNotNull(clientes);
    }
    
    @Test
    void testBuscarClienteInexistente() throws Exception {
        ClienteDAO dao = new ClienteDAO();
        
        // Simulamos la búsqueda de un ID fuera de rango
        Cliente cliente = dao.buscarPorId(999999);
        
        // Validamos que el DAO maneje bien el error y retorne null
        assertNull(cliente);
    }
}
