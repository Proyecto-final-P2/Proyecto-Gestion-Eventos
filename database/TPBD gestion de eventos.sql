-- Crear base de datos
CREATE DATABASE salonDeEventos;
USE salonDeEventos;

-- Tabla Salon
CREATE TABLE Salon (
  SA_ID INT NOT NULL PRIMARY KEY,
  SA_Direccion VARCHAR(45) NOT NULL,
  SA_Nombre VARCHAR(45) NOT NULL,
  SA_Capacidad INT NOT NULL,
  SA_CantSillas INT NOT NULL,
  SA_CantMesas INT NOT NULL,
  SA_Costo DECIMAL(10,2) NOT NULL
);

-- Tabla Reserva
CREATE TABLE Reserva (
  R_ID INT NOT NULL PRIMARY KEY,
  R_Fecha DATE NOT NULL,
  R_HoraInicio TIME NOT NULL,
  R_HoraFin TIME NOT NULL,
  R_Monto DECIMAL(10,2) NOT NULL
);

-- Tabla Pago
CREATE TABLE Pago (
  P_ID INT NOT NULL PRIMARY KEY,
  P_MontoPagado DECIMAL(10,2) NOT NULL,
  Reserva_R_ID INT NOT NULL,
  FOREIGN KEY (Reserva_R_ID) REFERENCES Reserva (R_ID)
);

-- Tabla Cliente
CREATE TABLE Cliente (
  C_ID INT NOT NULL PRIMARY KEY,
  C_DNI INT NOT NULL,
  C_NombreApellido VARCHAR(45) NOT NULL,
  C_Email VARCHAR(255) NOT NULL,
  C_Telefono VARCHAR(15) NOT NULL
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
  IN_ID INT NOT NULL PRIMARY KEY,
  IN_DNI INT NOT NULL,
  IN_NombreApellido VARCHAR(45) NOT NULL,
  IN_Email VARCHAR(255) NOT NULL,
  IN_Telefono VARCHAR(15) NOT NULL,
  IN_Asistencia ENUM('confirmado', 'pendiente de confirmacion', 'cancelado') NOT NULL,
  IN_PreferenciaMenu ENUM('Celiaco', 'Vegetariano', 'Vegano', 'Clasico', 'Infantil') NOT NULL
);

-- Tabla Evento
CREATE TABLE Evento (
  E_ID INT NOT NULL PRIMARY KEY,
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
  Invitado_IN_ID INT NULL,
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

SELECT *FROM Invitado;

-- Datos para la tabla Salon
INSERT INTO Salon (SA_ID, SA_Direccion, SA_Nombre, SA_Capacidad, SA_CantSillas, SA_CantMesas, SA_Costo)
VALUES 
(1, 'Av. Principal 123', 'Salón Dorado', 150, 150, 15, 10000.00),
(2, 'Calle Falsa 456', 'Salón Plateado', 100, 100, 10, 7500.00);

-- Datos para la tabla Cliente
INSERT INTO Cliente (C_ID, C_DNI, C_NombreApellido, C_Email, C_Telefono)
VALUES 
(1, 12345678, 'Juan Pérez', 'juan.perez@example.com', 1234567890),
(2, 87654321, 'María López', 'maria.lopez@example.com', 9876543210),
(3, 11223344, 'Carlos Gómez', 'carlos.gomez@example.com', 1122334455),
(6, 12349865, 'Marta Sánchez', 'marta.sanchez@example.com', 2345678910),
(7, 56781234, 'José Ramírez', 'jose.ramirez@example.com', 3456789011),
(8, 43218765, 'Lucía Torres', 'lucia.torres@example.com', 4567890122),
(9, 65432187, 'Antonio Díaz', 'antonio.diaz@example.com', 5678901233),
(10, 76543219, 'Elena Pérez', 'elena.perez@example.com', 6789012344);

-- Datos para la tabla Reserva
INSERT INTO Reserva (R_ID, R_Fecha, R_HoraInicio, R_HoraFin, R_Monto)
VALUES 
(1, '2024-12-01', '18:00:00', '23:00:00', 10000.00),
(2, '2024-12-05', '19:00:00', '23:59:00', 7500.00),
(3, '2024-12-10', '17:00:00', '22:00:00', 15000.00);

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
(2, 'Decoración', 'Decorarte', 3000.00, 1, 'pendiente de confirmacion'),
(3, 'DJ', 'Sonido Total', 4000.00, 1, 'cancelado'),
(6, 'Sonido', 'Sonidos Perfectos', 2500.00, 1, 'confirmado'),
(7, 'Decoración', 'Estilos Creativos', 3500.00, 1, 'pendiente de confirmacion'),
(8, 'Seguridad', 'Seguridad 24/7', 1500.00, 2, 'confirmado'),
(9, 'Limpieza', 'Limpieza Total', 1000.00, 1, 'cancelado'),
(10, 'Transporte', 'Transporte Express', 2000.00, 2, 'confirmado');

-- Datos para la tabla Invitado
INSERT INTO Invitado (IN_ID, IN_DNI, IN_NombreApellido, IN_Email, IN_Telefono, IN_Asistencia, IN_PreferenciaMenu)
VALUES 
(1, 33445566, 'Ana Martínez', 'ana.martinez@example.com', 2233445566, 'confirmado', 'Vegetariano'),
(2, 44556677, 'Luis Fernández', 'luis.fernandez@example.com', 3344556677, 'pendiente de confirmacion', 'Vegano'),
(3, 55667788, 'Sofía García', 'sofia.garcia@example.com', 4455667788, 'cancelado', 'Clasico');

-- Datos para la tabla Evento
INSERT INTO Evento (E_ID, E_Fecha, E_Horario, E_Tipo, E_CantInvitados, E_Estado, E_CostoFinal, Cliente_C_ID, Salon_SA_ID)
VALUES 
(1, '2024-12-01', '18:00:00', 'Boda', 100, 'confirmado', 12000.00, 1, 1),
(2, '2024-12-05', '19:00:00', 'Cumpleaños', 80, 'pendiente de confirmacion', 9500.00, 2, 2),
(3, '2024-12-10', '17:00:00', 'Conferencia', 150, 'cancelado', 18000.00, 3, 1),
(6, '2024-12-25', '21:00:00', 'Fiesta de Navidad', 200, 'pendiente de confirmacion', 12000.00, 6, 1),
(7, '2025-01-10', '17:30:00', 'Conferencia', 50, 'confirmado', 8000.00, 7, 2),
(8, '2025-01-15', '19:00:00', 'Reunión de Empresa', 30, 'cancelado', 6000.00, 8, 2),
(9, '2025-02-05', '18:00:00', 'Cumpleaños', 100, 'confirmado', 10000.00, 9, 1),
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


-- CONSULTA DOS TABLAS
SELECT 
    IN_NombreApellido, 
    (SELECT E_Tipo 
     FROM Evento 
     WHERE E_ID = (SELECT Evento_E_ID 
                   FROM Asiste 
                   WHERE Invitado_IN_ID = Invitado.IN_ID)
    ) AS TipoEvento
FROM 
    Invitado
WHERE 
    IN_ID IN (SELECT Invitado_IN_ID 
              FROM Asiste);
              
 -- CONSULTA DOS TABLAS            
SELECT 
    Evento.E_ID AS Evento_ID,
    Evento.E_Tipo AS Evento, 
    Servicios.SE_Tipo AS Servicio
FROM 
    Evento, 
    Servicios, 
    Contratados
WHERE 
    Evento.E_ID = Contratados.Evento_E_ID
    AND Servicios.SE_ID = Contratados.Servicios_SE_ID;

-- GROUP BY
SELECT 
    Evento.E_Tipo AS Evento, 
    COUNT(Contratados.Servicios_SE_ID) AS ServiciosContratados
FROM 
    Evento
JOIN 
    Contratados ON Evento.E_ID = Contratados.Evento_E_ID
GROUP BY 
    Evento.E_Tipo;
    
-- HAVING
SELECT 
    Evento.E_ID AS EventoID,
    Evento.E_Tipo AS Evento,
    SUM(Contratados.CON_Precio) AS CostoTotalServicios
FROM 
    Evento
JOIN 
    Contratados ON Evento.E_ID = Contratados.Evento_E_ID
GROUP BY 
    Evento.E_ID, Evento.E_Tipo
HAVING 
    SUM(Contratados.CON_Precio) > 3000;
    
-- GROUP BY & HAVING
SELECT 
    Cliente_C_ID AS ClienteID, 
    SUM(E_CostoFinal) AS CostoTotal
FROM 
    Evento
GROUP BY 
    Cliente_C_ID
HAVING 
    SUM(E_CostoFinal) > 10000;
    
-------------
-- VISTA 1
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

SELECT * FROM VistasEventosConfirmados WHERE Salon = 'Salón Dorado';

-- VISTA 2
-- muestra todos los pagos realizados por cada cliente
CREATE VIEW VistaPagosPorCliente AS
SELECT 
    C.C_NombreApellido AS Cliente,
    SUM(P.P_MontoPagado) AS TotalPagado
FROM 
    Cliente C
JOIN 
    Evento E ON C.C_ID = E.Cliente_C_ID
JOIN 
    Pago P ON E.E_ID = P.Reserva_R_ID
GROUP BY 
    C.C_ID;

SELECT * FROM VistaPagosPorCliente;

-- PROCEDIMIENTO
DELIMITER $$

CREATE PROCEDURE CrearReservaConelID(
    IN p_R_ID INT,
    IN p_Fecha DATE,
    IN p_HoraInicio TIME,
    IN p_HoraFin TIME,
    IN p_Monto DECIMAL(10,2),
    OUT p_Mensaje VARCHAR(255)
)
BEGIN
    DECLARE horarioOcupado INT;

    -- Verificar si el horario ya está reservado
    SELECT COUNT(*)
    INTO horarioOcupado
    FROM Reserva
    WHERE R_Fecha = p_Fecha 
      AND (
          (p_HoraInicio BETWEEN R_HoraInicio AND R_HoraFin) OR
          (p_HoraFin BETWEEN R_HoraInicio AND R_HoraFin) OR
          (R_HoraInicio BETWEEN p_HoraInicio AND p_HoraFin)
      );

    IF horarioOcupado > 0 THEN
        SET p_Mensaje = 'El horario ya está reservado.';
    ELSE
        INSERT INTO Reserva (R_ID, R_Fecha, R_HoraInicio, R_HoraFin, R_Monto)
        VALUES (p_R_ID, p_Fecha, p_HoraInicio, p_HoraFin, p_Monto);
        SET p_Mensaje = 'Reserva creada exitosamente.';
    END IF;
END$$

DELIMITER ;
CALL CrearReservaConelID(6, '2024-10-15', '18:00:00', '22:00:00', 5000.00, @mensaje);
SELECT @mensaje AS Mensaje;

-- TRIGGER
DELIMITER $$
CREATE TRIGGER ActualizarEstadoEvento
AFTER UPDATE ON Invitado
FOR EACH ROW
BEGIN
    DECLARE invitadosTotales INT;
    DECLARE confirmados INT;

    -- Obtener el total de invitados y los confirmados para el evento asociado
    SELECT COUNT(*) INTO invitadosTotales
    FROM Asiste
    WHERE Evento_E_ID = (
        SELECT Evento_E_ID
        FROM Asiste
        WHERE Invitado_IN_ID = NEW.IN_ID
    );

    SELECT COUNT(*) INTO confirmados
    FROM Asiste AS a
    JOIN Invitado AS i ON a.Invitado_IN_ID = i.IN_ID
    WHERE a.Evento_E_ID = (
        SELECT Evento_E_ID
        FROM Asiste
        WHERE Invitado_IN_ID = NEW.IN_ID
    ) AND i.IN_Asistencia = 'confirmado';

    -- Si todos los invitados confirmaron asistencia, actualizar el estado del evento
    IF invitadosTotales = confirmados THEN
        UPDATE Evento
        SET E_Estado = 'confirmado'
        WHERE E_ID = (
            SELECT Evento_E_ID
            FROM Asiste
            WHERE Invitado_IN_ID = NEW.IN_ID
        );
    END IF;
END$$

DELIMITER ;

-- TRANSACCION
DELIMITER $$

CREATE PROCEDURE RegistrarPagoParaReserva(
    IN p_Reserva_R_ID INT,
    IN p_P_MontoPagado DECIMAL(10,2),
    OUT p_Mensaje VARCHAR(255)
)
BEGIN
    DECLARE reservaExiste INT;

    -- Iniciar la transacción
    START TRANSACTION;

    -- Verificar si la reserva existe
    SELECT COUNT(*)
    INTO reservaExiste
    FROM Reserva
    WHERE R_ID = p_Reserva_R_ID;

    IF reservaExiste = 0 THEN
        -- Si no existe la reserva, revertir la transacción
        ROLLBACK;
        SET p_Mensaje = 'Error: La reserva no existe.';
    ELSE
        -- Insertar el pago asociado a la reserva
        INSERT INTO Pago (P_MontoPagado, Reserva_R_ID)
        VALUES (p_P_MontoPagado, p_Reserva_R_ID);

        -- Verificar si el pago se insertó correctamente
        IF ROW_COUNT() = 0 THEN
            ROLLBACK;
            SET p_Mensaje = 'Error: No se pudo registrar el pago.';
        ELSE
            COMMIT;
            SET p_Mensaje = 'Pago registrado exitosamente.';
        END IF;
    END IF;
END$$

DELIMITER ;

-- Declarar una variable para capturar el mensaje
SET @mensaje = '';

-- Llamar al procedimiento
CALL RegistrarPagoParaReserva(
    1,              -- ID de la reserva existente
    2000.00,        -- Monto pagado
    @mensaje        -- Variable para capturar el mensaje
);

-- Verificar el mensaje de salida
SELECT @mensaje AS Resultado;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;