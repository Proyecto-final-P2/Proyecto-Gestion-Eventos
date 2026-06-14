-- Tabla Administrador
CREATE TABLE IF NOT EXISTS Administrador (
  A_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  A_NombreApellido VARCHAR(45) NOT NULL,
  A_Email VARCHAR(255) NOT NULL,
  A_Password VARCHAR(255) NOT NULL
);

-- Datos para la tabla Administrador
INSERT INTO Administrador (A_ID, A_NombreApellido, A_Email, A_Password)
VALUES 
(1, 'Admin Principal', 'admin@admin.com', 'admin123')
ON DUPLICATE KEY UPDATE A_Email = A_Email;
