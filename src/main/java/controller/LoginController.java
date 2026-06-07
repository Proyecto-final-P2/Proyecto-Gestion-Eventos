package controller;

import model.Cliente;
import repository.ClienteDAO;
import GUI.MenuPrincipal;
import GUI.Login;
import javax.swing.JOptionPane;
import java.util.List;

// controlador del inicio de sesion
public class LoginController {

    // ventana y la conexion a la bd
    private final Login vista;
    private final ClienteDAO dao = new ClienteDAO();

    // se le pasa la ventana de login
    public LoginController(Login vista) {
        this.vista = vista;
    }

    // metodo que intenta iniciar sesion
    public void iniciarSesion(String email, String password) {
        try {
            // trae todos los clientes de la bd
            List<Cliente> clientes = dao.listar();
            
            // se fija si hay algun cliente con ese email
            boolean encontrado = clientes.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));

            if (encontrado) {
                // si lo encuentra, saca los datos de ese cliente
                Cliente cliente = clientes.stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .findFirst().orElse(null);
                    
                // cierra la ventana de login
                vista.dispose();
                // abre el menu principal pasandole el cliente que entro
                new MenuPrincipal(cliente).setVisible(true);
            } else {
                // si no lo encuentra, tira error
                JOptionPane.showMessageDialog(vista,
                    "Email o contraseña incorrectos.",
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // si se cae la base de datos, tira error de conexion
            JOptionPane.showMessageDialog(vista,
                "Error al conectar con la base de datos:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
