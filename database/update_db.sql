-- Asegurar columnas correctas en la tabla Pago
ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_Pagador VARCHAR(100) NULL;
ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_MetodoPago VARCHAR(50) NOT NULL DEFAULT 'Efectivo';
ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_FechaPago DATE NOT NULL DEFAULT (CURRENT_DATE);

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

-- Vista para reporte de pagos por cliente
CREATE OR REPLACE VIEW VistaPagosPorCliente AS
SELECT 
    c.C_NombreApellido AS Cliente, 
    SUM(p.P_MontoPagado) AS TotalPagado
FROM Pago p
JOIN Reserva r ON p.Reserva_R_ID = r.R_ID
JOIN Cliente c ON r.R_ClienteID = c.C_ID
GROUP BY c.C_ID, c.C_NombreApellido;

