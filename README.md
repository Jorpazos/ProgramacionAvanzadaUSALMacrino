# Parcial 2do Cuatrimestre 2025 – Programación Avanzada (USAL Pilar)

Aplicación web de una empresa de logística: ABM de camiones y choferes, carga de viajes con cálculo de
tiempo y tanques, y pantalla de chofer para iniciar/finalizar sus viajes.

**Stack:** Java 17 · Maven multi-módulo · Servlets/JSP 6 (Jakarta EE 10, Tomcat 10.1) · JSTL · JDBC puro · MySQL 8 · Bootstrap 5 + SweetAlert2.

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

## Usuarios de prueba

| Perfil | Usuario | Contraseña |
|---|---|---|
| Administrador | `admin` | `admin123` |
| Chofer (Juan Pérez, cat. C) | `30111222` | `chofer123` |
| Chofer (María Gómez, cat. B) | `28555666` | `chofer123` |

El usuario de cada chofer es su DNI; la contraseña inicial la define el administrador al crearlo.

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
