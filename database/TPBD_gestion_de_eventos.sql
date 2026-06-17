-- Crear base de datos
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS salonDeEventos;
USE salonDeEventos;

-- Tabla Salon
CREATE TABLE Salon (
  SA_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  SA_Direccion VARCHAR(45) NOT NULL,
  SA_Nombre VARCHAR(45) NOT NULL,
  SA_Capacidad INT NOT NULL,
  SA_CantSillas INT NOT NULL,
  SA_CantMesas INT NOT NULL,
  SA_Costo DECIMAL(10,2) NOT NULL
);

-- Tabla Cliente
CREATE TABLE Cliente (
  C_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  C_DNI INT NOT NULL,
  C_NombreApellido VARCHAR(45) NOT NULL,
  C_Email VARCHAR(255) NOT NULL,
  C_Telefono VARCHAR(15) NOT NULL
);

-- Tabla Reserva
CREATE TABLE Reserva (
  R_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  R_Fecha DATE NOT NULL,
  R_HoraInicio TIME NOT NULL,
  R_HoraFin TIME NOT NULL,
  R_Monto DECIMAL(10,2) NOT NULL,
  R_ClienteID INT NOT NULL,
  R_SalonID INT NOT NULL,
  FOREIGN KEY (R_ClienteID) REFERENCES Cliente (C_ID),
  FOREIGN KEY (R_SalonID) REFERENCES Salon (SA_ID)
);

-- Tabla Pago
CREATE TABLE Pago (
  P_ID INT NOT NULL PRIMARY KEY,
  P_MontoPagado DECIMAL(10,2) NOT NULL,
  Reserva_R_ID INT NOT NULL,
  FOREIGN KEY (Reserva_R_ID) REFERENCES Reserva (R_ID)
);

-- Tabla Administrador
CREATE TABLE Administrador (
  A_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  A_NombreApellido VARCHAR(45) NOT NULL,
  A_Email VARCHAR(255) NOT NULL,
  A_Password VARCHAR(255) NOT NULL
);

-- Tabla Servicios
CREATE TABLE Servicios (
  SE_ID INT NOT NULL PRIMARY KEY,
  SE_Tipo VARCHAR(45) NOT NULL,
  SE_Proveedor VARCHAR(45) NOT NULL,
  SE_Costo DECIMAL(10,2) NOT NULL,
  SE_Cantidad INT NOT NULL,
  SE_Estado ENUM('confirmado', 'pendiente de confirmacion', 'cancelado') NOT NULL
);

-- Tabla Invitado
CREATE TABLE Invitado (
  IN_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  IN_DNI INT NOT NULL,
  IN_NombreApellido VARCHAR(45) NOT NULL,
  IN_Email VARCHAR(255) NOT NULL,
  IN_Telefono VARCHAR(15) NOT NULL,
  IN_Asistencia ENUM('confirmado', 'pendiente de confirmacion', 'cancelado') NOT NULL,
  IN_PreferenciaMenu ENUM('Celiaco', 'Vegetariano', 'Vegano', 'Clasico', 'Infantil') NOT NULL
);

-- Tabla Evento
CREATE TABLE Evento (
  E_ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  E_Fecha DATE NOT NULL,
  E_Horario TIME NOT NULL,
  E_Tipo VARCHAR(45) NOT NULL,
  E_CantInvitados INT NOT NULL,
  E_Estado ENUM('confirmado', 'pendiente de confirmacion', 'cancelado') NOT NULL,
  E_CostoFinal DECIMAL(10,2) NOT NULL, -- CALCULADO
  Cliente_C_ID INT NOT NULL,
  Salon_SA_ID INT NOT NULL,
  FOREIGN KEY (Cliente_C_ID) REFERENCES Cliente (C_ID),
  FOREIGN KEY (Salon_SA_ID) REFERENCES Salon (SA_ID)
);

-- Tabla Asiste
CREATE TABLE Asiste (
  Invitado_IN_ID INT NOT NULL,
  Evento_E_ID INT NOT NULL,
  PRIMARY KEY (Invitado_IN_ID, Evento_E_ID),
  FOREIGN KEY (Invitado_IN_ID) REFERENCES Invitado (IN_ID),
  FOREIGN KEY (Evento_E_ID) REFERENCES Evento (E_ID)
);

-- Tabla Contratados
CREATE TABLE Contratados (
  Evento_E_ID INT NOT NULL,
  Servicios_SE_ID INT NOT NULL,
  CON_Precio DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (Evento_E_ID, Servicios_SE_ID),
  FOREIGN KEY (Evento_E_ID) REFERENCES Evento (E_ID),
  FOREIGN KEY (Servicios_SE_ID) REFERENCES Servicios (SE_ID)
);

-- Datos para la tabla Salon
INSERT INTO Salon (SA_ID, SA_Direccion, SA_Nombre, SA_Capacidad, SA_CantSillas, SA_CantMesas, SA_Costo)
VALUES 
(1, 'Av. Principal 123', 'Salon Dorado', 150, 150, 15, 10000.00),
(2, 'Calle Falsa 456', 'Salon Plateado', 100, 100, 10, 7500.00);

-- Datos para la tabla Cliente
INSERT INTO Cliente (C_ID, C_DNI, C_NombreApellido, C_Email, C_Telefono)
VALUES 
(1, 12345678, 'Juan Perez', 'juan.perez@example.com', 1234567890),
(2, 87654321, 'Maria Lopez', 'maria.lopez@example.com', 9876543210),
(3, 11223344, 'Carlos Gomez', 'carlos.gomez@example.com', 1122334455),
(6, 12349865, 'Marta Sanchez', 'marta.sanchez@example.com', 2345678910),
(7, 56781234, 'Jose Ramirez', 'jose.ramirez@example.com', 3456789011),
(8, 43218765, 'Lucia Torres', 'lucia.torres@example.com', 4567890122),
(9, 65432187, 'Antonio Diaz', 'antonio.diaz@example.com', 5678901233),
(10, 76543219, 'Elena Perez', 'elena.perez@example.com', 6789012344);

-- Datos para la tabla Administrador
INSERT INTO Administrador (A_ID, A_NombreApellido, A_Email, A_Password)
VALUES 
(1, 'Admin Principal', 'admin@admin.com', 'admin123');

-- Datos para la tabla Reserva
INSERT INTO Reserva (R_ID, R_Fecha, R_HoraInicio, R_HoraFin, R_Monto, R_ClienteID, R_SalonID)
VALUES 
(1, '2024-12-01', '18:00:00', '23:00:00', 10000.00, 1, 1),
(2, '2024-12-05', '19:00:00', '23:59:00', 7500.00, 2, 2),
(3, '2024-12-10', '17:00:00', '22:00:00', 15000.00, 3, 1);

-- Datos para la tabla Pago
INSERT INTO Pago (P_ID, P_MontoPagado, Reserva_R_ID)
VALUES 
(1, 5000.00, 1),
(2, 7500.00, 2),
(3, 15000.00, 3);

-- Datos para la tabla Servicios
INSERT INTO Servicios (SE_ID, SE_Tipo, SE_Proveedor, SE_Costo, SE_Cantidad, SE_Estado)
VALUES 
(1, 'Catering', 'Catering Pro', 5000.00, 1, 'confirmado'),
(2, 'Decoracion', 'Decorarte', 3000.00, 1, 'pendiente de confirmacion'),
(3, 'DJ', 'Sonido Total', 4000.00, 1, 'cancelado'),
(6, 'Sonido', 'Sonidos Perfectos', 2500.00, 1, 'confirmado'),
(7, 'Decoracion', 'Estilos Creativos', 3500.00, 1, 'pendiente de confirmacion'),
(8, 'Seguridad', 'Seguridad 24/7', 1500.00, 2, 'confirmado'),
(9, 'Limpieza', 'Limpieza Total', 1000.00, 1, 'cancelado'),
(10, 'Transporte', 'Transporte Express', 2000.00, 2, 'confirmado');

-- Datos para la tabla Invitado
INSERT INTO Invitado (IN_ID, IN_DNI, IN_NombreApellido, IN_Email, IN_Telefono, IN_Asistencia, IN_PreferenciaMenu)
VALUES 
(1, 33445566, 'Ana Martinez', 'ana.martinez@example.com', 2233445566, 'confirmado', 'Vegetariano'),
(2, 44556677, 'Luis Fernandez', 'luis.fernandez@example.com', 3344556677, 'pendiente de confirmacion', 'Vegano'),
(3, 55667788, 'Sofia Garcia', 'sofia.garcia@example.com', 4455667788, 'cancelado', 'Clasico');

-- Datos para la tabla Evento
INSERT INTO Evento (E_ID, E_Fecha, E_Horario, E_Tipo, E_CantInvitados, E_Estado, E_CostoFinal, Cliente_C_ID, Salon_SA_ID)
VALUES 
(1, '2024-12-01', '18:00:00', 'Boda', 100, 'confirmado', 12000.00, 1, 1),
(2, '2024-12-05', '19:00:00', 'Cumpleanos', 80, 'pendiente de confirmacion', 9500.00, 2, 2),
(3, '2024-12-10', '17:00:00', 'Conferencia', 150, 'cancelado', 18000.00, 3, 1),
(6, '2024-12-25', '21:00:00', 'Fiesta de Navidad', 200, 'pendiente de confirmacion', 12000.00, 6, 1),
(7, '2025-01-10', '17:30:00', 'Conferencia', 50, 'confirmado', 8000.00, 7, 2),
(8, '2025-01-15', '19:00:00', 'Reunion de Empresa', 30, 'cancelado', 6000.00, 8, 2),
(9, '2025-02-05', '18:00:00', 'Cumpleanos', 100, 'confirmado', 10000.00, 9, 1),
(10, '2025-03-03', '20:00:00', 'Boda', 150, 'pendiente de confirmacion', 15000.00, 10, 2);

INSERT INTO Asiste (Invitado_IN_ID, Evento_E_ID)
VALUES 
(1, 1),
(2, 1),
(3, 2);

INSERT INTO Contratados (Evento_E_ID, Servicios_SE_ID, CON_Precio)
VALUES 
(1, 1, 5000.00),  -- Evento 1 con Servicio 1
(2, 2, 3000.00),  -- Evento 2 con Servicio 2
(3, 3, 4000.00),  -- Evento 3 con Servicio 3
(6, 6, 2500.00),
(7, 7, 3500.00),
(8, 8, 1500.00),
(9, 9, 1000.00),
(10, 10, 2000.00);

-- Vista de Eventos Confirmados
CREATE VIEW VistasEventosConfirmados AS
SELECT 
    E.E_ID AS EventoID, 
    E.E_Tipo AS TipoEvento, 
    E.E_Fecha AS FechaEvento, 
    E.E_Horario AS Horario, 
    E.E_CantInvitados AS CantidadInvitados, 
    E.E_CostoFinal AS CostoFinal, 
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