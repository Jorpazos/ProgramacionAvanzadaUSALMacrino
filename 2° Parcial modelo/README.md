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

## Cómo ejecutarlo

1. **Base de datos** (MySQL 8):
   ```
   mysql -u root -p < db/01_ddl_logistica.sql
   mysql -u root -p < db/02_datos_iniciales.sql
   ```
2. **Credenciales de conexión**: `logistica-dao/src/main/resources/db.properties` (por defecto `root`/`root`, base `logistica`).
   También se pueden pasar con las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. **Compilar**: `mvn clean package` → genera `logistica-web/target/logistica.war`.
4. **Desplegar** el WAR en Tomcat 10.1 (carpeta `webapps/`). La aplicación queda en
   `http://localhost:8080/logistica/`.

## Paso a paso en IntelliJ IDEA

Requisitos previos: **JDK 11**, **MySQL 8** en marcha (puerto 3306) con **MySQL Workbench 8.0 CE** y **Apache Tomcat 10.1** descargado y descomprimido
(no sirve Tomcat 9, ver la sección de compatibilidad más abajo). Maven viene incluido en IntelliJ.

> La integración con Tomcat (paso 5) es de **IntelliJ IDEA Ultimate**. Con la edición **Community** usá el plugin gratuito
> *Smart Tomcat* (ver "Alternativa para Community").

1. **Abrir el proyecto.** `File > Open…` y elegí la carpeta `2° Parcial modelo` (la que contiene el `pom.xml` padre, no una
   subcarpeta). Confirmá *Trust Project*. IntelliJ detecta los módulos `logistica-dao` y `logistica-web` y descarga las
   dependencias; esperá a que termine la barra de progreso de abajo a la derecha. Si aparece un cartel *Maven projects
   need to be imported*, hacé clic en **Load Maven Changes** (o en la vista *Maven* el botón de recargar).
2. **Configurar el JDK 11.** `File > Project Structure > Project`, en *SDK* elegí un JDK 11 (si no está: *Add SDK >
   Download JDK*). En *Language level* poné 11.
3. **Crear la base de datos con MySQL Workbench 8.0 CE.**
   1. Abrí **MySQL Workbench**. En la pantalla de inicio, junto a *MySQL Connections*, tocá el **+** para crear una conexión
      (si ya tenés una `Local instance MySQL80`, podés usarla).
   2. Completá: *Connection Name* = `logistica` (o el que quieras) · *Hostname* = `localhost` · *Port* = `3306` ·
      *Username* = `root`. Con **Store in Vault…** guardá la contraseña. Estos datos deben coincidir con los de
      `db.properties` (`localhost:3306`, usuario `root`, contraseña `root` por defecto); si tu `root` tiene otra
      contraseña, usá la tuya y cambiala en el paso 4.
   3. Tocá **Test Connection**; debe decir *Successfully made the MySQL connection*. Después **OK** y doble clic sobre la
      conexión para abrirla. Si falla, el servicio MySQL80 no está corriendo (Windows: `services.msc` > *MySQL80* > Iniciar).
   4. `File > Open SQL Script…` y elegí **`db/01_ddl_logistica.sql`**. Ejecutalo completo con el rayo **⚡ Execute**
      (o `Ctrl+Shift+Enter`). Esto **borra y recrea** la base `logistica`, con sus tablas y stored procedures.
   5. Repetí con **`db/02_datos_iniciales.sql`** (carga los usuarios y datos de prueba).
   6. Verificá: en el panel *Navigator > SCHEMAS* tocá el botón de refrescar; debe aparecer el esquema **`logistica`** con
      las tablas `usuario`, `chofer`, `camion`, `viaje`, `distancia`, etc. Una consulta rápida:
      `SELECT username, rol FROM logistica.usuario;` debe devolver `admin` y los dos choferes.
   (Alternativa por consola: `mysql -u root -p < db/01_ddl_logistica.sql` y luego `db/02_datos_iniciales.sql`.)
4. **Ajustar las credenciales.** Si tu MySQL no usa `root` / `root`, editá `logistica-dao/src/main/resources/db.properties`
   (`db.user`, `db.password`, y `db.url` si el puerto es otro).
5. **Configurar Tomcat en IntelliJ (Ultimate).**
   1. `Run > Edit Configurations… > + > Tomcat Server > Local`.
   2. En *Application server* tocá **Configure…** y seleccioná la carpeta donde descomprimiste Tomcat 10.1.
   3. Pestaña **Deployment** > `+` > **Artifact…** > elegí **`logistica-web:war exploded`**.
   4. En *Application context* dejá **`/logistica`** (así la URL coincide con la de este README).
   5. Pestaña **Server**: *URL* = `http://localhost:8080/logistica/`; si querés que abra el navegador solo, dejá tildado
      *After launch*. Aplicá con **OK**.
6. **Compilar.** En la vista *Maven* (panel derecho) > `logistica-parent > Lifecycle > clean` y luego `install` (o desde
   la terminal de IntelliJ: `mvn clean install`). Es importante hacerlo al menos una vez para que `logistica-web`
   encuentre el módulo `logistica-dao`.
7. **Ejecutar.** Elegí la configuración de Tomcat creada arriba y presioná el botón verde **Run** (▶) o **Debug** (🐞).
   En la consola *Services/Run* debe aparecer algo como `Server startup in [...] milliseconds`.
8. **Ver la página funcionando.** Abrí **http://localhost:8080/logistica/**. Te redirige al login: entrá con
   `admin` / `admin123` (administrador: ABM de choferes, camiones y carga de viajes) o con `30111222` / `chofer123`
   (chofer: ve sus viajes y puede iniciarlos/finalizarlos). Ver tabla de *Usuarios de prueba* más abajo.

### Alternativa para IntelliJ Community (plugin Smart Tomcat)

1. `File > Settings > Plugins > Marketplace`, buscá **Smart Tomcat**, instalalo y reiniciá el IDE.
2. `Run > Edit Configurations… > + > Smart Tomcat`.
3. *Tomcat Server*: la carpeta de Tomcat 10.1 · *Deployment directory*: `logistica-web/src/main/webapp` ·
   *Context path*: `/logistica` · *Server port*: `8080`.
4. Antes de correr, compilá con `mvn clean install` (paso 6) y ejecutá la configuración con ▶. La página queda en
   `http://localhost:8080/logistica/`.

### Problemas frecuentes

| Síntoma | Causa / solución |
|---|---|
| `Communications link failure` o `Access denied for user` al entrar | MySQL apagado o credenciales distintas: revisá el paso 4. También funcionan las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD` en la configuración de Run. |
| `Unknown database 'logistica'` / tablas inexistentes | No se corrieron los scripts de `db/` en Workbench (paso 3), o se ejecutó solo una parte: usá *Execute* sin seleccionar texto para correr todo el script. |
| `ClassNotFoundException: javax.servlet...` o 404 en todas las páginas | Se está usando Tomcat 9; hace falta **Tomcat 10.1+**. |
| Aparece `logistica-web:war exploded` pero no `logistica-dao` | Recargá Maven y corré `mvn clean install` (paso 6). |
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

`mvn test` ejecuta las pruebas unitarias del cálculo de viaje y de las reglas del dominio.
