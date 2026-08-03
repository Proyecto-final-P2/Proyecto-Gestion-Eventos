# Decisiones de Diseño — Gestor de Eventos

## 1. Arquitectura: MVC + DAO sobre JDBC plano

El proyecto separa responsabilidades en cuatro capas (`model`, `repository`,
`controller`, `GUI`):

- **`model`**: POJOs simples (Cliente, Evento, Salon, etc.), sin lógica.
- **`repository` (DAO)**: única capa que sabe hablar SQL. Cada entidad tiene su
  DAO (`ClienteDAO`, `EventoDAO`, ...) con `listar`, `insertar`, `actualizar`,
  `eliminar` y búsquedas puntuales.
- **`controller`**: capa intermedia entre la UI y el DAO. Atrapa excepciones de
  base de datos y las traduce en mensajes (`JOptionPane`) para que la GUI no
  tenga que conocer SQL ni `SQLException`.
- **`GUI`**: paneles Swing, uno por entidad, todos siguiendo el mismo patrón
  (tabla + formulario + botones ABM), documentado en el README como receta para
  agregar nuevos módulos.



## 2. Base de datos: MySQL 8 vía Docker

Se eligió MySQL por ser el motor visto en la materia de Bases de Datos. Se usa Docker
(`docker-compose.yml`) para que cualquiera levante una instancia idéntica sin
instalar MySQL localmente, evitando el clásico "en mi máquina funciona".

Los scripts de `/database` se montan en `docker-entrypoint-initdb.d`, que MySQL
ejecuta **en orden alfabético** solo la primera vez que el volumen está vacío.
Esto importa: ver la sección de bugs corregidos más abajo.

## 3. Autenticación simple, sin hashing

Las contraseñas (`Administrador.A_Password`) se guardan en texto plano y se
comparan con `.equals()` en `LoginController`. Es una decisión consciente para
el alcance del proyecto (no hay registro público de usuarios, el único actor
autenticado es el administrador interno del salón), pero es una limitación
conocida: en un contexto real correspondería hashear con BCrypt o similar.
Se documenta acá en vez de "arreglarlo silenciosamente" para que quede como
decisión explícita y no como descuido.

Por la misma razón existe un admin "de respaldo" hardcodeado en
`LoginController` (`admin@admin.com` / `admin123`) además del que inserta
`init.sql`. Mantenerlo evita que el sistema quede inaccesible si la fila
de `Administrador` se borra por error; la contrapartida es que ese acceso no se
puede deshabilitar desde la UI.

## 4. Relaciones N:M con tablas intermedias

`Evento`↔`Invitado` se resuelve con `Asiste`, y `Evento`↔`Servicios` con
`Contratados` (esta última agrega el atributo `CON_Precio`, porque el precio
pactado por servicio puede diferir del precio de lista en `Servicios.SE_Costo`).
Ver el diagrama completo en [`DIAGRAMA_BD.md`](./DIAGRAMA_BD.md).

## 5. Dependencia iText (reportes PDF): incluida pero no conectada

`pom.xml` incluye `itextpdf` para exportar reportes a PDF, pero a la fecha de
este sprint el panel `ReportesPanel` solo muestra los datos en una `JTable` (no
hay ningún `PdfWriter` ni exportación real en el código). Se deja registrado
como **deuda técnica** en vez de eliminar la dependencia, ya que la base
(`ReportesControlador`) ya expone los datos necesarios para conectar la
exportación en una iteración futura.

## 6. Bugs encontrados y corregidos en esta auditoría (Sprint 3)

Antes de cerrar la entrega se probó el flujo completo "levantar el contenedor
desde cero → usar cada módulo de la app" con una instancia MySQL real. Se
encontraron y corrigieron los siguientes problemas, todos relacionados con
desincronización entre el código Java y el esquema SQL:

| # | Problema | Síntoma real | Causa | Fix |
|---|----------|---------------|-------|-----|
| 1 | `database/TPBD_gestion_de_eventos.sql` duplicaba las tablas de `init.sql` | Al levantar el contenedor desde cero, `init.sql` fallaba con `Table 'Salon' already exists` y abortaba a mitad de camino (sin cargar los datos de ejemplo) | Docker ejecuta los `.sql` de `/database` en orden alfabético; el archivo legado se ejecutaba primero | Se eliminó `TPBD_gestion_de_eventos.sql` del repositorio |
| 2 | La vista `VistasEventosConfirmados` (usada por Reportes) solo estaba definida en ese mismo archivo legado | Al borrar el archivo, el panel de Reportes rompía con `Table 'VistasEventosConfirmados' doesn't exist` | La vista nunca se migró a `init.sql` | Se consolidaron las vistas directamente en `init.sql` |
| 3 | `Cliente.C_ID`, `Salon.SA_ID` y `Evento.E_ID` no tenían `AUTO_INCREMENT` | Dar de alta un cliente, salón o evento desde la app fallaba con `Field 'X_ID' doesn't have a default value` | Los DAO insertan sin especificar el ID, asumiendo autoincremento | Se definió `AUTO_INCREMENT` en las tablas de `init.sql` |
| 4 | La tabla `Pago` no tenía las columnas `P_Pagador`, `P_MetodoPago`, `P_FechaPago`, y `P_ID` tampoco era autoincremental | Registrar un pago fallaba con `Unknown column 'P_Pagador' in 'INSERT INTO'` | `PagoDAO` se actualizó para guardar más datos del pago, pero el script SQL no se actualizó en paralelo | Se agregaron las columnas y el autoincremento directamente en `init.sql` |

Todos los fixes y esquemas están consolidados en `database/init.sql` de forma limpia y fueron
verificados para que coincidan exactamente con lo que espera cada DAO de la aplicación.



## 7. Limitaciones conocidas / trabajo futuro

- Contraseñas sin hashear (ver punto 3).
- Reportes solo en pantalla, sin exportación PDF real.
- Las credenciales de la base (`Util.java`: `root` / `gestor123`) están
  hardcodeadas en el código en vez de leerse de variables de entorno. Para este
  proyecto académico el riesgo es bajo (es la contraseña del contenedor MySQL
  local, no un servicio expuesto), pero quedaría mejor externalizarlas a una
  variable de entorno con un valor por defecto.
