# Diagrama de la Base de Datos — `salonDeEventos`


> GitHub renderiza este bloque Mermaid automáticamente al ver el archivo en el repo.
> Si se necesita visualizarlo en otro formato se puede ver en https://mermaid.ai/d/8fe0f490-fa5b-4b66-bf94-55c072127d8f

```mermaid
erDiagram
    Cliente ||--o{ Reserva : "realiza"
    Salon   ||--o{ Reserva : "se reserva en"
    Reserva ||--o{ Pago : "se paga con"
    Cliente ||--o{ Evento : "organiza"
    Salon   ||--o{ Evento : "se realiza en"
    Evento  ||--o{ Asiste : "tiene"
    Invitado||--o{ Asiste : "asiste a"
    Evento  ||--o{ Contratados : "contrata"
    Servicios||--o{ Contratados : "se contrata en"

    Cliente {
        int C_ID PK "AUTO_INCREMENT"
        int C_DNI
        varchar C_NombreApellido
        varchar C_Email
        varchar C_Telefono
    }

    Salon {
        int SA_ID PK "AUTO_INCREMENT"
        varchar SA_Direccion
        varchar SA_Nombre
        int SA_Capacidad
        int SA_CantSillas
        int SA_CantMesas
        decimal SA_Costo
    }

    Reserva {
        int R_ID PK "AUTO_INCREMENT"
        date R_Fecha
        time R_HoraInicio
        time R_HoraFin
        decimal R_Monto
        int R_ClienteID FK
        int R_SalonID FK
    }

    Pago {
        int P_ID PK "AUTO_INCREMENT"
        decimal P_MontoPagado
        int Reserva_R_ID FK
        varchar P_Pagador
        varchar P_MetodoPago
        date P_FechaPago
    }

    Evento {
        int E_ID PK "AUTO_INCREMENT"
        date E_Fecha
        time E_Horario
        varchar E_Tipo
        int E_CantInvitados
        enum E_Estado
        decimal E_CostoFinal
        int Cliente_C_ID FK
        int Salon_SA_ID FK
    }

    Invitado {
        int IN_ID PK "AUTO_INCREMENT"
        int IN_DNI
        varchar IN_NombreApellido
        varchar IN_Email
        varchar IN_Telefono
        enum IN_Asistencia
        enum IN_PreferenciaMenu
    }

    Asiste {
        int Invitado_IN_ID FK
        int Evento_E_ID FK
    }

    Servicios {
        int SE_ID PK "AUTO_INCREMENT"
        varchar SE_Tipo
        varchar SE_Proveedor
        decimal SE_Costo
        int SE_Cantidad
        enum SE_Estado
    }

    Contratados {
        int Evento_E_ID FK
        int Servicios_SE_ID FK
        decimal CON_Precio
    }

    Administrador {
        int A_ID PK "AUTO_INCREMENT"
        varchar A_NombreApellido
        varchar A_Email
        varchar A_Password
    }
```

## Notas

- **`Asiste`** y **`Contratados`** son tablas intermedias que resuelven las relaciones
  N:M entre `Evento`↔`Invitado` y `Evento`↔`Servicios` respectivamente.
- **`Administrador`** no tiene relación con el resto del modelo en el esquema actual;
  se usa únicamente para el login del panel de administración.
- La vista `VistasEventosConfirmados` (creada en `update_db.sql`) no es una tabla,
  sino una consulta guardada sobre `Evento` ⋈ `Cliente` ⋈ `Salon` filtrada por
  `E_Estado = 'confirmado'`. La usa exclusivamente el panel de Reportes.
- Las claves primarias `C_ID`, `SA_ID`, `E_ID` y `P_ID` ahora son `AUTO_INCREMENT`
  (antes no lo eran, ver `DECISIONES_DISEÑO.md` → "Bugs corregidos").
