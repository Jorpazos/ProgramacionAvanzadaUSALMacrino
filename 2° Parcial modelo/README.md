# Parcial 2do Cuatrimestre 2025 – Programación Avanzada (USAL Pilar)

Aplicación web de una empresa de logística: ABM de camiones y choferes, carga de viajes con cálculo de
tiempo y tanques, y pantalla de chofer para iniciar/finalizar sus viajes.

**Stack:** Java 11 · Maven multi-módulo · Servlets/JSP 6 (Jakarta EE 10, Tomcat 10.1) · JSTL · JDBC puro · MySQL 8 · Bootstrap 5 + SweetAlert2.

## Estructura (entregables del enunciado)

| Elemento | Ubicación |
|---|---|
| 01 – Código fuente Maven, proyectos DAO y MVC separados | `logistica-dao/` (dominio + DAO) y `logistica-web/` (MVC) |
| 02 – Script DDL de la base de datos | `db/01_ddl_logistica.sql` (+ `db/02_datos_iniciales.sql` con datos de prueba) |
| Explicación línea por línea y defensa del código | `docs/Explicacion_y_Defensa_Parcial.pdf` |

## Cómo ejecutarlo, paso a paso

Requisitos previos: **JDK 11** (obligatorio, no usar otra versión), **MySQL 8** en marcha (puerto 3306) con **MySQL Workbench 8.0 CE**
y **Apache Tomcat 10.1** descargado y descomprimido (no sirve Tomcat 9, ver la sección de compatibilidad más abajo).
Todo se hace desde las interfaces de Workbench e IntelliJ, sin escribir comandos en la terminal.

### 1. Cargar la base de datos en MySQL Workbench

1. Abrí **MySQL Workbench** y entrá a tu conexión local (`localhost:3306`, usuario `root`). Si no tenés conexión, en la
   pantalla de inicio tocá el **+** junto a *MySQL Connections*, completá *Hostname* = `localhost`, *Port* = `3306`,
   *Username* = `root`, guardá la contraseña con **Store in Vault…** y probá con **Test Connection**.
   Si falla, el servicio MySQL80 no está corriendo (Windows: `services.msc` > *MySQL80* > Iniciar).
2. Menú **File > Open SQL Script…** y elegí `db/01_ddl_logistica.sql` de tu copia del repositorio.
3. Ejecutá **todo** el script con el rayo ⚡ (o `Ctrl+Shift+Enter`), sin seleccionar texto. Esto **borra y recrea** la base
   `logistica` con sus tablas y stored procedures.
4. Repetí los pasos 2 y 3 con `db/02_datos_iniciales.sql` (carga los usuarios y datos de prueba).
5. En el panel **Schemas** tocá el botón de refrescar 🔄: debe aparecer `logistica` con sus tablas. Para comprobarlo:
   `SELECT username, rol FROM logistica.usuario;` debe devolver `admin` y los dos choferes.
6. Si tu contraseña de `root` **no** es `root`, cambiala en `logistica-dao/src/main/resources/db.properties`
   (`db.user`, `db.password` y `db.url` si el puerto es otro). También se pueden usar las variables de entorno
   `DB_URL`, `DB_USER` y `DB_PASSWORD` en la configuración de Run.

### 2. Abrir el proyecto en IntelliJ y poner el JDK 11

1. `File > Open…` y elegí la carpeta `2° Parcial modelo` (la que tiene el `pom.xml` padre, no una subcarpeta).
   Confirmá *Trust Project* y aceptá **Load Maven project**. Esperá a que termine la barra de progreso de abajo a la derecha.
2. `File > Project Structure > Project`: en *SDK* elegí un **JDK 11** (si no está: *Add SDK > Download JDK*, versión 11)
   y en *Language level* poné **11**.
3. `File > Settings > Build, Execution, Deployment > Build Tools > Maven > Runner`: en **JRE** elegí el mismo JDK 11.
   Si queda otro JDK, Maven falla al compilar.

### 3. Compilar desde el panel Maven

1. Abrí el panel **Maven** en la barra derecha (icono de la "m"; si no está: `View > Tool Windows > Maven`).
2. Desplegá el proyecto raíz (**`logistica-parent`**, no `logistica-dao` ni `logistica-web` por separado) y luego **Lifecycle**.
3. Doble clic en **clean**, esperá a que termine, y después doble clic en **package**.
4. En la consola *Run* debe salir **BUILD SUCCESS** y quedar creado `logistica-web/target/logistica.war`.

Si el panel Maven aparece vacío, tocá 🔄 *Reload All Maven Projects* arriba del panel.

### 4. Configurar Tomcat 10.1 (IntelliJ Ultimate)

1. `Run > Edit Configurations… > + > Tomcat Server > Local`.
2. En *Application server* tocá **Configure…** y elegí la carpeta donde descomprimiste Tomcat 10.1.
3. Pestaña **Deployment** > `+` > **Artifact…** > **`logistica-web:war exploded`**.
4. En *Application context* poné **`/logistica`**.
5. Pestaña **Server**: *URL* = `http://localhost:8080/logistica/`. Aplicá con **OK**.

**IntelliJ Community** (sin Tomcat integrado): instalá el plugin gratuito **Smart Tomcat** (`File > Settings > Plugins >
Marketplace`, reiniciá el IDE) y usá `Run > Edit Configurations… > + > Smart Tomcat` con *Tomcat Server* = carpeta de
Tomcat 10.1, *Deployment directory* = `logistica-web/src/main/webapp`, *Context path* = `/logistica` y *Server port* = `8080`.
Compilá antes con el paso 3.

### 5. Ejecutar y entrar

1. Elegí la configuración de Tomcat y presioná ▶ **Run**. En la consola debe aparecer `Server startup in [...] milliseconds`.
2. Se abre el navegador; si no, entrá a **http://localhost:8080/logistica/**.
3. Iniciá sesión con `admin` / `admin123` (administrador: ABM de choferes, camiones y carga de viajes) o con
   `30111222` / `chofer123` (chofer: ve sus viajes y puede iniciarlos o finalizarlos). Ver *Usuarios de prueba* más abajo.

### Si algo falla

Cuando Maven o Tomcat muestran un error, lo que se ve abajo suele ser solo el final: el error real está más arriba.
En la consola, hacé clic en la línea roja de la izquierda (por ejemplo `ar.edu.usal.logistica:logistica-dao:jar`) y buscá
la **primera línea que diga `[ERROR]`**.

| Síntoma | Causa / solución |
|---|---|
| `invalid target release`, `release version 11 not supported` o error de compilación en `logistica-dao` | Maven usa otro JDK: repetí el paso 2 (SDK, *Language level* y **JRE del Runner de Maven**, todos en 11) y volvé a correr **clean** y **package**. |
| `Communications link failure` o `Access denied for user` al entrar | MySQL apagado o credenciales distintas: revisá el paso 1.6. |
| `Unknown database 'logistica'` / tablas inexistentes | No se corrieron los scripts de `db/` o se ejecutó solo una parte: repetí el paso 1 sin seleccionar texto. |
| `ClassNotFoundException: javax.servlet...` o 404 en todas las páginas | Se está usando Tomcat 9; hace falta **Tomcat 10.1+**. |
| Aparece `logistica-web:war exploded` pero falta `logistica-dao`, o el panel Maven está vacío | Recargá Maven (🔄) y repetí **clean** y **package** (paso 3). |
| La URL da 404 | Verificá que el *Application context* sea `/logistica` y que el puerto 8080 no lo use otro programa. |
| Caracteres raros (`Ã³`) | Confirmá que los archivos se abren en UTF-8 (`File > Settings > Editor > File Encodings`). |

## Usuarios de prueba

| Perfil | Usuario | Contraseña |
|---|---|---|
| Administrador | `admin` | `admin123` |
| Chofer (Juan Pérez, cat. C) | `30111222` | `chofer123` |
| Chofer (María Gómez, cat. B) | `28555666` | `chofer123` |

El usuario de cada chofer es su DNI; la contraseña inicial la define el administrador al crearlo.

## ⚠ Compatibilidad con Tomcat (javax vs jakarta)

Esta solución usa **Jakarta EE 10** (`jakarta.servlet.*`, JSTL `jakarta.tags.*`) y requiere **Tomcat 10.1 o superior**.
Los ejemplos de clase del profesor usan `javax.servlet` (Servlet 4.0, **Tomcat 9**). Si el examen se rinde con Tomcat 9,
hay que migrar la solución: reemplazar `jakarta.servlet` por `javax.servlet` en los imports, las dependencias del
`pom.xml` (`javax.servlet-api 4.0.1`, `javax.servlet.jsp-api 2.3.3`, `jstl 1.2`) y los URI de JSTL
(`http://java.sun.com/jsp/jstl/core`, etc.), y el `web.xml` a la versión 4.0.

## Reglas de negocio implementadas

- Destinos válidos: CABA, Córdoba, Corrientes, Formosa, La Plata, La Rioja, Mendoza y Neuquén. Distancias en la tabla `distancia`.
- **Tiempo de viaje** = `ceil(km / 200)` días. **Tanques** = `ceil(km × consumo_l_por_km / capacidad_tanque)`.
- Un chofer solo puede manejar camiones que tiene autorizados **y** cuyas toneladas entren en su categoría (A=10 t, B=20 t, C=30 t, D=40 t).
- Al cargar un viaje se ofrecen solo los camiones disponibles (sin viaje `ASIGNADO` ni `EN_CURSO`).
- Estados del viaje: `ASIGNADO → EN_CURSO` (lo inicia el chofer) `→ FINALIZADO` (lo marca el chofer).
- Sesión recordada con cookie (`RECORDARME`, 30 días). Al cerrar sesión se invalidan la Session y las cookies.

## Base de datos

La base es **MySQL 8** (driver `mysql-connector-j`, URL `jdbc:mysql://…`). Nota de transparencia: en el entorno donde se
desarrolló no había un servidor MySQL, por lo que las pruebas manuales se hicieron contra MariaDB 10.11 (compatible con
el mismo driver y el mismo script). Conviene correr el script en MySQL 8 antes de entregar.

## Documentación

`docs/Explicacion_y_Defensa_Parcial.pdf` explica el código línea por línea y lo defiende (se regenera con
`python3 docs/generar_pdf.py`, requiere `reportlab`).

## Tests

Desde el panel Maven (`Lifecycle > test`) o con `mvn test` se ejecutan las pruebas unitarias del cálculo de viaje y de las reglas del dominio.
