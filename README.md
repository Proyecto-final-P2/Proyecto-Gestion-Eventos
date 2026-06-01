# 🗓️ Gestor de Eventos
**Sistema de gestión de salón de eventos**
> Materia: Sistemas de Información II

---

## 👥 Integrantes
| Nombre | Módulo asignado |
|--------|----------------|
| (completar) | Clientes |
| (completar) | Eventos |
| (completar) | Salones + Reservas |
| (completar) | Servicios |
| (completar) | Invitados + Pagos |
| (completar) | Reportes + BD |

---

## 🏗️ Arquitectura
El proyecto sigue el patrón **MVC**:
```
src/main/java/
├── app/          → Lanzador principal (GestorDeEventos.java)
├── model/        → Entidades (Cliente, Evento, Salon, etc.)
├── repository/   → Acceso a BD con JDBC (ClienteDAO, EventoDAO, etc.)
├── controller/   → Lógica de negocio (ClienteController, etc.)
└── GUI/          → Interfaces Swing (ClientesPanel, EventosPanel, etc.)
```

---

## ⚙️ Requisitos previos
Instalar **una sola vez** antes de clonar el proyecto:

| Herramienta | Versión | Link |
|-------------|---------|------|
| Java JDK | 17+ | https://adoptium.net |
| Maven | 3.8+ | https://maven.apache.org/download.cgi |
| Docker Desktop | Último | https://www.docker.com/products/docker-desktop |
| IntelliJ IDEA Community | Último | https://www.jetbrains.com/idea/download |
| Git | Último | https://git-scm.com |

---

## 🚀 Instalación paso a paso (hacer UNA sola vez)

### 1. Clonar el repositorio
```bash
git clone https://github.com/TU_ORG/GestorDeEventos.git
cd GestorDeEventos
```

### 2. Levantar la base de datos con Docker
```bash
docker compose up -d
```
Esto descarga MySQL 8, crea la BD `salonDeEventos` y carga todos los datos automáticamente.

Verificar que está corriendo:
```bash
docker ps
# Debe mostrar: gestor_mysql   Up
```

### 3. Abrir el proyecto en IntelliJ
1. Abrí IntelliJ IDEA
2. `File → Open` → seleccioná la carpeta `GestorDeEventos`
3. IntelliJ detecta el `pom.xml` → click en **"Load Maven Project"**
4. Esperá que descargue las dependencias (barra de progreso abajo)

### 4. Correr la aplicación
- Abrí `src/main/java/app/GestorDeEventos.java`
- Click derecho → **Run 'GestorDeEventos.main()'**
- Debe aparecer la ventana de Login

---

## 🔄 Flujo de trabajo con Git (leer obligatorio)

### Convención de commits
```
feat: agrega panel de eventos
fix: corrige validación de DNI duplicado
refactor: extrae método mapear() en EventoDAO
docs: actualiza README con instrucciones de Maven
test: agrega prueba de conexión a BD
```

### Cómo trabajar en una tarea
```bash
# 1. Asegurate de estar en develop y tener lo último
git checkout develop
git pull origin develop

# 2. Crear tu rama para la tarea
git checkout -b feature/panel-eventos

# 3. Hacé tus cambios y commiteá seguido
git add .
git commit -m "feat: agrega tabla de eventos al panel"

# 4. Subir tu rama
git push origin feature/panel-eventos

# 5. Abrir un Pull Request en GitHub hacia develop
#    Pedir revisión a al menos 1 compañero antes de mergear
```

### ⚠️ Reglas importantes
- **Nunca pushear directo a `main` ni a `develop`**
- **Siempre** crear una rama `feature/` para cada tarea
- Resolver conflictos antes de pedir el PR
- Si rompés algo, usá una rama `hotfix/nombre`

---

## 🗄️ Base de datos

**Tablas principales:**
| Tabla | Descripción |
|-------|-------------|
| `Cliente` | Clientes que organizan eventos |
| `Salon` | Salones disponibles para alquilar |
| `Evento` | Eventos creados por los clientes |
| `Reserva` | Reservas de fecha/horario |
| `Pago` | Pagos de reservas |
| `Servicios` | Servicios disponibles (catering, DJ, etc.) |
| `Invitado` | Invitados a los eventos |
| `Asiste` | Relación invitado ↔ evento |
| `Contratados` | Relación evento ↔ servicios contratados |

**Credenciales locales (Docker):**
```
Host:     localhost
Puerto:   3306
BD:       salonDeEventos
Usuario:  root
Password: gestor123
```

**Comandos útiles de Docker:**
```bash
docker compose up -d      # Iniciar la BD
docker compose down       # Detener la BD
docker compose down -v    # Detener Y borrar datos (reset completo)
```

---

## 🧩 Cómo implementar un nuevo panel

Seguir el patrón de `ClientesPanel.java` — está completo y documentado como referencia.

1. Implementar `MiEntidadPanel.java` en `GUI/`
2. Implementar `MiEntidadController.java` en `controller/`
3. El panel ya está registrado en `MenuPrincipal.java` — solo hay que reemplazar el placeholder

---

## ❓ Problemas comunes

**"Cannot connect to database"**
→ Verificar que Docker esté corriendo: `docker ps`
→ Si no aparece `gestor_mysql`: `docker compose up -d`

**"Port 3306 already in use"**
→ Tenés MySQL instalado localmente. Cambiar el puerto en `docker-compose.yml` a `3307:3306`
→ Actualizar `Util.java`: `jdbc:mysql://localhost:3307/...`

**Maven no descarga dependencias**
→ `View → Tool Windows → Maven → Reload All Maven Projects` en IntelliJ

**El proyecto no compila**
→ Verificar JDK 17+: `File → Project Structure → SDK`
