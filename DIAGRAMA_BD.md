# Diagrama de la Base de Datos — `salonDeEventos`

> GitHub renderiza este bloque Mermaid automáticamente al ver el archivo en el repo.

```mermaid
erDiagram
    Evento ||--o{ Pago : "se paga con"
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

    Pago {
        int P_ID PK "AUTO_INCREMENT"
        decimal P_MontoPagado
        int Evento_E_ID FK
        varchar P_Pagador
        varchar P_MetodoPago
        date P_FechaPago
    }

    Evento {
        int E_ID PK "AUTO_INCREMENT"
        date E_Fecha
        time E_HoraInicio
        time E_HoraFin
        varchar E_Tipo
        int E_CantInvitados
        enum E_Estado
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
- Las claves primarias `C_ID`, `SA_ID`, `E_ID` y `P_ID` ahora son `AUTO_INCREMENT`.
- **Refactor:** La entidad "Reserva" fue fusionada conceptualmente dentro de "Evento" para evitar inconsistencias y unificar el flujo de cobros.
