# Código de Brian: articulos-web

Proyecto web (Maven, Servlets/JSP, JDBC, MySQL) subido por Brian en
https://github.com/BrianSz1228/ProgAvanzBase (carpeta `codigos`, archivo `articulos-web.zip`).
Se copia acá tal cual lo subió: `articulos-web/` (descomprimido) y `articulos-web.zip` (original).

Estructura: patrón DAO + Factory (`ArticuloDAOFactory`), capa de servicio (`ArticuloService`),
excepción de negocio, `ConexionBD` y `PropertiesUtil` para la conexión, controlador `ArticuloServlet`
y vista `articulos.jsp`.

## Cómo hacerlo andar (indicaciones de Brian)

1. En MySQL, ejecutar los queries de `articulos-web/sql/articulos.sql` (crea la base `tienda`).
2. Abrir `articulos-web/src/main/resources/application.properties` y completar `db.password`
   con la contraseña propia de MySQL (usuario `root`, base `tienda`).
3. Compilar con `mvn clean package` y desplegar el WAR en Tomcat.
