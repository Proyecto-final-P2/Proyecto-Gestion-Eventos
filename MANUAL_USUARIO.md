# Manual de Usuario — Gestor de Eventos

Guía rápida para el administrador del salón que va a usar la aplicación.
Para instrucciones de instalación técnica, ver el [`README.md`](../README.md).

## 1. Iniciar sesión

1. Abrí la aplicación. Aparece la ventana **"Gestor de Eventos - Iniciar Sesión"**.
2. Ingresá tu email y contraseña.
3. Si es la primera vez que usás el sistema, podés entrar con la cuenta de
   administrador por defecto:
   - **Email:** `admin@admin.com`
   - **Contraseña:** `admin123`
4. Click en **Ingresar**. Si las credenciales son correctas, se abre el panel
   principal; si no, el sistema muestra "Email o contraseña incorrectos".

## 2. Panel principal

A la izquierda hay un menú con las secciones disponibles: **Clientes, Eventos,
Salones, Reservas, Servicios, Invitados, Pagos, Reportes** y
**Administradores**. Click en cualquiera para abrir esa sección en el panel
central. El botón **Cerrar Sesión**, abajo de todo, vuelve a la pantalla de
login.

## 3. Clientes

Permite gestionar a las personas que organizan eventos.

- **Ver lista:** se carga automáticamente al entrar a la sección.
- **Buscar:** escribí un DNI en el campo de búsqueda.
- **Agregar:** completá el formulario (DNI, nombre y apellido, email,
  teléfono) y guardá. El sistema rechaza el alta si el DNI o el email ya
  existen.
- **Editar / Eliminar:** seleccioná un cliente de la tabla y usá los botones
  correspondientes.

## 4. Salones

Catálogo de los salones disponibles para alquilar.

- Cada salón tiene dirección, nombre, capacidad, cantidad de sillas y mesas, y
  costo de alquiler.
- Alta, búsqueda por nombre, edición y baja funcionan igual que en Clientes.

## 5. Eventos

El corazón del sistema: cada evento vincula un cliente con un salón en una
fecha y horario determinados.

- Al crear un evento se indica: fecha, horario, tipo (ej. Boda, Cumpleaños),
  cantidad de invitados, estado (`pendiente de confirmacion`, `confirmado`,
  `cancelado`), costo final, y a qué cliente y salón corresponde.
- Podés filtrar eventos por tipo, o ver solo los eventos de un cliente puntual.

## 6. Reservas

Muestra el listado de reservas de salón (fecha, horario, monto, cliente y
salón asociados). En esta versión la pantalla es de solo consulta.

## 7. Servicios

Catálogo de servicios contratables (catering, DJ, decoración, etc.), con
proveedor, costo, cantidad disponible y estado (`Disponible` /
`No disponible`). Alta, edición y baja disponibles desde esta pantalla.

## 8. Invitados

Gestión de los invitados de cada evento.

- Al agregar un invitado se indica DNI, nombre y apellido, email, teléfono,
  estado de asistencia y preferencia de menú (Celíaco, Vegetariano, Vegano,
  Clásico, Infantil), y el evento al que pertenece.
- Podés ver todos los invitados, filtrar por evento, o buscar por DNI dentro
  de un evento puntual.

## 9. Pagos

Registro de pagos asociados a una reserva.

- Al registrar un pago se indica el monto, la reserva a la que corresponde,
  quién pagó y el método de pago (transferencia, débito, crédito, efectivo,
  etc.). La fecha se completa automáticamente con la fecha del día.
- Podés editar el monto, la reserva o el método de un pago existente (la
  fecha original no se modifica), o eliminarlo.

## 10. Reportes

Muestra los eventos **confirmados**, con cliente, salón, fecha, cantidad de
invitados y costo final.

- Usá el combo **"Filtrar por salón"** para ver solo los eventos de un salón
  puntual, o "Todos" para ver el listado completo.
- **Refrescar** vuelve a consultar la base de datos por si hubo cambios.
- Esta pantalla es solo de consulta; no incluye exportación a PDF (queda
  pendiente para una próxima versión, ver `docs/DECISIONES_DISENO.md`).

## 11. Administradores

Gestión de las cuentas que pueden iniciar sesión en el panel administrativo.
Alta (con validación de email único), edición y baja disponibles desde esta
pantalla. Tené cuidado de no eliminar todas las cuentas de administrador: si
eso pasa, siempre podés volver a entrar con la cuenta por defecto
(`admin@admin.com` / `admin123`) descripta en el paso 1.

## Problemas frecuentes

| Problema | Qué hacer |
|---|---|
| "Error al conectar con la base de datos" en cualquier pantalla | Verificá que el contenedor de Docker esté corriendo (`docker ps`) |
| Un alta falla con un mensaje de error de SQL | Avisá al equipo de desarrollo — puede ser un caso no contemplado; revisá igualmente `docs/DECISIONES_DISENO.md` por si es una limitación conocida |
| Olvidaste la contraseña de administrador | Pedile a otro administrador que la restablezca desde la sección Administradores, o usá la cuenta por defecto del paso 1 |
