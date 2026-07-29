USE salonDeEventos;
-- Fix Evento E_ID
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE Evento MODIFY E_ID INT NOT NULL AUTO_INCREMENT;
SET FOREIGN_KEY_CHECKS = 1;

-- Asegurar columnas correctas en la tabla Pago (Nota: MySQL no soporta IF NOT EXISTS en ALTER TABLE, se comentan ya que las columnas ya existen en init.sql y TPBD_gestion_de_eventos.sql)
-- ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_Pagador VARCHAR(100) NULL;
-- ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_MetodoPago VARCHAR(50) NOT NULL DEFAULT 'Efectivo';
-- ALTER TABLE Pago ADD COLUMN IF NOT EXISTS P_FechaPago DATE NOT NULL DEFAULT (CURRENT_DATE);

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
JOIN Evento e ON p.Evento_E_ID = e.E_ID
JOIN Cliente c ON e.Cliente_C_ID = c.C_ID
GROUP BY c.C_ID, c.C_NombreApellido;

-- Vista para detalles dinámicos del Evento (Costo y Saldo)
CREATE OR REPLACE VIEW VistaDetallesEvento AS
SELECT 
    e.E_ID AS EventoID,
    c.C_NombreApellido AS Cliente,
    s.SA_Nombre AS Salon,
    e.E_Fecha AS Fecha,
    e.E_HoraInicio AS HoraInicio,
    e.E_HoraFin AS HoraFin,
    e.E_Tipo AS Tipo,
    e.E_CantInvitados AS Invitados,
    e.E_Estado AS Estado,
    s.SA_Costo + COALESCE((SELECT SUM(CON_Precio) FROM Contratados WHERE Evento_E_ID = e.E_ID), 0) AS CostoTotal,
    (s.SA_Costo + COALESCE((SELECT SUM(CON_Precio) FROM Contratados WHERE Evento_E_ID = e.E_ID), 0)) - COALESCE((SELECT SUM(P_MontoPagado) FROM Pago WHERE Evento_E_ID = e.E_ID), 0) AS SaldoPendiente
FROM Evento e
JOIN Cliente c ON e.Cliente_C_ID = c.C_ID
JOIN Salon s ON e.Salon_SA_ID = s.SA_ID;

-- Vista para VistasEventosConfirmados (Reportes)
CREATE OR REPLACE VIEW VistasEventosConfirmados AS
SELECT 
    E.E_ID AS EventoID, 
    E.E_Tipo AS TipoEvento, 
    E.E_Fecha AS FechaEvento, 
    E.E_HoraInicio AS HoraInicio, 
    E.E_HoraFin AS HoraFin,
    E.E_CantInvitados AS CantidadInvitados, 
    (S.SA_Costo + COALESCE((SELECT SUM(CON_Precio) FROM Contratados WHERE Evento_E_ID = E.E_ID), 0)) AS CostoTotal, 
    C.C_NombreApellido AS Cliente, 
    S.SA_Nombre AS Salon
FROM 
    Evento E
JOIN 
    Cliente C ON E.Cliente_C_ID = C.C_ID
JOIN 
    Salon S ON E.Salon_SA_ID = S.SA_ID
WHERE 
    E.E_Estado = 'confirmado';

