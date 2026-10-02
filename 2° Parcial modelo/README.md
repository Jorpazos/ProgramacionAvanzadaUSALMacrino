# Parcial 2do Cuatrimestre 2025 – Programación Avanzada (USAL Pilar)

Aplicación web de una empresa de logística: ABM de camiones y choferes, carga de viajes con cálculo de
tiempo y tanques, y pantalla de chofer para iniciar/finalizar sus viajes.

**Stack:** Java 11 · Maven multi-módulo · Servlets 4.0/JSP 2.3 (`javax.servlet`, Tomcat 9.0) · JSTL · JDBC puro · MySQL 8 · Bootstrap 5 + SweetAlert2.

## Estructura (entregables del enunciado)

| Elemento | Ubicación |
|---|---|
| 01 – Código fuente Maven, proyectos DAO y MVC separados | `logistica-dao/` (dominio + DAO) y `logistica-web/` (MVC) |
| 02 – Script DDL de la base de datos | `db/01_ddl_logistica.sql` (+ `db/02_datos_iniciales.sql` con datos de prueba) |
| Explicación línea por línea y defensa del código | `docs/Explicacion_y_Defensa_Parcial.pdf` |

## Cómo ejecutarlo, paso a paso

Requisitos previos: **JDK 11** (obligatorio, no usar otra versión), **MySQL 8** en marcha (puerto 3306) con **MySQL Workbench 8.0 CE**
y **Apache Tomcat 9.0** instalado (el instalador de Windows crea el servicio `Tomcat9`; no sirve Tomcat 10 o superior, ver la sección de compatibilidad más abajo).
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

### 4. Detener el servicio Tomcat9 de Windows

El instalador de Tomcat 9.0 crea un servicio de Windows que puede quedar usando el puerto 8080. Si sigue corriendo, el Tomcat
de IntelliJ no puede arrancar o, peor, vos entrás a la página del servicio (que no tiene tu aplicación) creyendo que es la tuya.

1. Abrí **Monitor Tomcat** (Menú Inicio > *Apache Tomcat 9.0 Tomcat9* > *Monitor Tomcat*; queda el icono del gatito en la
   bandeja del sistema junto al reloj, clic derecho > *Stop service*).
2. En la ventana *Apache Tomcat 9.0 Tomcat9 Properties*, pestaña **General**, tocá **Stop** y comprobá que diga
   *Service Status: **Stopped***. Si querés que no arranque solo con Windows, dejá *Startup type* en **Manual**.
   Después tocá **Cancelar** y cerrá la ventana: no hace falta cambiar nada más.
3. Abrí `http://localhost:8080/` en el navegador: debe dar «no se puede acceder al sitio». Eso significa que el puerto quedó libre.

### 5. Configurar Tomcat 9.0 en IntelliJ (Ultimate)

1. `Run > Edit Configurations… > + > Tomcat Server > Local`.
2. Junto a *Application server* tocá **Configure…** y en *Tomcat Home* poné la carpeta de instalación de Tomcat 9.0 (la
   encontrás en el Menú Inicio > *Apache Tomcat 9.0 Tomcat9* > **Tomcat 9.0 Program Directory**), normalmente
   `C:\Program Files\Apache Software Foundation\Tomcat 9.0`. Aceptá.
3. Pestaña **Deployment**: `+` > **Artifact…** > **`logistica-web:war exploded`**. Abajo, en *Application context*,
   escribí **`/logistica`**.
4. Pestaña **Server**: en *JRE* elegí tu **JDK 11**. En *URL* poné `http://localhost:8080/logistica/`.
   Si el puerto 8080 está ocupado por otro programa y no lo podés liberar, cambiá **HTTP port** a `8081` (y si da error el
   *JMX port* `1099`, poné `1100`); en ese caso la dirección de la aplicación es `http://localhost:8081/logistica/`.
5. Tocá **OK**.

**IntelliJ Community** (sin Tomcat integrado): instalá el plugin gratuito **Smart Tomcat** (`File > Settings > Plugins >
Marketplace`, reiniciá el IDE) y usá `Run > Edit Configurations… > + > Smart Tomcat` con *Tomcat Server* = carpeta de
Tomcat 9.0, *Deployment directory* = `logistica-web/src/main/webapp`, *Context path* = `/logistica` y *Server port* = `8080`.
Compilá antes con el paso 3 y detené el servicio Tomcat9 (paso 4).

**Alternativa sin IntelliJ:** con el servicio Tomcat9 andando, copiá `logistica-web/target/logistica.war` a la carpeta
`webapps` de Tomcat 9.0 (puede pedir permisos de administrador) y entrá a `http://localhost:8080/logistica/`.

### 6. Ejecutar y entrar

1. Elegí la configuración de Tomcat y presioná ▶ **Run**. Esperá en la consola (panel *Services* o *Run*) el mensaje
   **`Artifact logistica-web:war exploded: Deploy took … milliseconds`**: recién ahí la aplicación está cargada.
   Si antes estaba corriendo, tocá ⏹ **Stop** en el panel *Services* y volvé a ejecutar.
2. Se abre el navegador; si no, entrá a **http://localhost:8080/logistica/** (o con `8081` si cambiaste el puerto).
3. Iniciá sesión con `admin` / `admin123` (administrador: ABM de choferes, camiones y carga de viajes) o con
   `30111222` / `chofer123` (chofer: ve sus viajes y puede iniciarlos o finalizarlos). Ver *Usuarios de prueba* más abajo.

**¿Cómo sé que responde mi Tomcat y no otro?** Si en `http://localhost:8080/` ves la página de bienvenida de Tomcat («If you're
seeing this, you've successfully installed Tomcat»), o si `/logistica/` da **404**, está respondiendo otro Tomcat (casi siempre
el servicio de Windows) o la aplicación no se desplegó. Detené el servicio (paso 4) y revisá que en *Deployment* esté
`logistica-web:war exploded` con contexto `/logistica`.

### Cómo ver el error real (IntelliJ)

La página «Ocurrió un error inesperado» no muestra el detalle a propósito. Para ver la excepción:

1. Abrí el panel **Services** (`View > Tool Windows > Services`) y elegí tu configuración de Tomcat.
2. Pestaña **Server** (o la consola *Run*): buscá las líneas en rojo `SEVERE` / `Exception`. La causa suele estar en el último
   `Caused by:` (por ejemplo `Access denied for user`, `Unknown database 'logistica'` o `Communications link failure`).
3. También está el archivo **Tomcat Localhost Log** (pestaña al lado de *Tomcat Catalina Log*) con el stack trace completo.
4. Con el mensaje en mano, usá la tabla de abajo.

### Si algo falla

Cuando Maven o Tomcat muestran un error, lo que se ve abajo suele ser solo el final: el error real está más arriba.
En la consola, hacé clic en la línea roja de la izquierda (por ejemplo `ar.edu.usal.logistica:logistica-dao:jar`) y buscá
la **primera línea que diga `[ERROR]`**.

| Síntoma | Causa / solución |
|---|---|
| `invalid target release`, `release version 11 not supported` o error de compilación en `logistica-dao` | Maven usa otro JDK: repetí el paso 2 (SDK, *Language level* y **JRE del Runner de Maven**, todos en 11) y volvé a correr **clean** y **package**. |
| `Communications link failure` o `Access denied for user` al entrar | MySQL apagado o credenciales distintas: revisá el paso 1.6. |
| `Unknown database 'logistica'` / tablas inexistentes | No se corrieron los scripts de `db/` o se ejecutó solo una parte: repetí el paso 1 sin seleccionar texto. |
| `ClassNotFoundException: javax.servlet...` o 404 en todas las páginas | Se está usando Tomcat 10 o superior; hace falta **Tomcat 9.0**. |
| Aparece `logistica-web:war exploded` pero falta `logistica-dao`, o el panel Maven está vacío | Recargá Maven (🔄) y repetí **clean** y **package** (paso 3). |
| `Address already in use` / `Port 8080 is already in use` al arrancar | El servicio **Tomcat9** de Windows sigue corriendo: pararlo (paso 4) y volver a ejecutar, o usar el puerto 8081 (paso 5.4). |
| «Ocurrió un error inesperado» al iniciar sesión o al navegar | Es una excepción del servidor; el mensaje real está en la consola. Ver *Cómo ver el error real*, arriba. Causas típicas: base de datos sin cargar, usuario o contraseña de MySQL distintos (paso 1.6), o MySQL apagado. |
| La URL da 404 o aparece la página de bienvenida de Tomcat | Responde otro Tomcat o la aplicación no se desplegó: ver «¿Cómo sé que responde mi Tomcat?» (paso 6) y el *Application context* `/logistica`. |
| Caracteres raros (`Ã³`) | Confirmá que los archivos se abren en UTF-8 (`File > Settings > Editor > File Encodings`). |

## Usuarios de prueba

| Perfil | Usuario | Contraseña |
|---|---|---|
| Administrador | `admin` | `admin123` |
| Chofer (Juan Pérez, cat. C) | `30111222` | `chofer123` |
| Chofer (María Gómez, cat. B) | `28555666` | `chofer123` |

El usuario de cada chofer es su DNI; la contraseña inicial la define el administrador al crearlo.

## Compatibilidad con Tomcat (javax vs jakarta)

Esta solución usa `javax.servlet.*` (Servlet 4.0, JSP 2.3, JSTL 1.2 con las URI `http://java.sun.com/jsp/jstl/...`) y **requiere
Tomcat 9.0**, el mismo que usan los ejemplos de clase. No funciona en Tomcat 10 o superior, que cambió los paquetes a
`jakarta.servlet.*`. Probado desplegando el WAR en Apache Tomcat 9.0.98 con JDK 11.

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
