# Bloques de explicacion reutilizables para archivos que se repiten entre clases (poms y web.xml del arquetipo webapp)

def pom_war(artefacto, jstl=False):
    bloques = [
     ("", "Cabecera del POM: elemento raíz <b>project</b> con sus espacios de nombres XML (definen el formato válido del archivo)."),
     ("<modelVersion>", "Versión del modelo de POM (siempre 4.0.0)."),
     ("<groupId>edu.usal", f"Coordenadas del proyecto: <b>groupId</b> edu.usal (la organización), <b>artifactId</b> {artefacto} (el nombre), <b>packaging war</b> (se empaqueta como aplicación web, un archivo .war que se despliega en Tomcat), versión 1.0-SNAPSHOT, nombre descriptivo y URL (datos del arquetipo <i>maven-archetype-webapp</i>)."),
     ("<dependencies>", "Abre la lista de dependencias (librerías que Maven descarga solo)."),
     ("<dependency>", "<b>JUnit 3.8.1</b> con <b>scope test</b>: solo se usa para pruebas unitarias, no viaja dentro del WAR."),
     ("<dependency>", "<b>javax.servlet-api 4.0.1</b> con <b>scope provided</b>: se necesita para compilar los servlets, pero en ejecución la provee el servidor (Tomcat 9), por eso no se incluye en el WAR."),
     ("<dependency>", "<b>javax.servlet.jsp-api 2.3.3</b>, también <b>provided</b>: la API de JSP que aporta Tomcat."),
    ]
    if jstl:
        bloques += [
         ("<dependency>", "<b>jstl 1.2</b> (scope compile por defecto): la implementación de JSTL; <b>Tomcat no la trae</b>, por eso debe ir dentro del WAR (carpeta WEB-INF/lib)."),
         ("<dependency>", "<b>jstl-api 1.2</b>: la API (interfaces) de JSTL."),
         ("<dependency>", "<b>taglibs standard 1.1.2</b>: las librerías de etiquetas estándar (los .tld con core, fmt, etc.)."),
        ]
    bloques += [
     ("</dependencies>", "Cierre de la lista de dependencias."),
     ("<build>", f"<b>finalName</b> define el nombre del WAR generado (<i>{artefacto}.war</i>) y por lo tanto la <b>ruta de contexto</b> de la aplicación: <i>http://localhost:8080/{artefacto}/</i>."),
     ("</project>", "Cierre del POM."),
    ]
    return {
     "rol": "Archivo de configuración de Maven (POM) de una aplicación web: coordenadas, dependencias y nombre del WAR.",
     "defensa": "Es el POM que genera el arquetipo de Maven para aplicaciones web. Lo importante: <b>packaging war</b>, las APIs de Servlet/JSP como <b>provided</b> (las aporta el contenedor) y, si la página usa JSTL, esas librerías sí van en el WAR." + (" Esta versión incluye JSTL." if jstl else ""),
     "bloques": bloques}


def webxml(v4=False, extra=None):
    bloques = [("", "Declaración del tipo de documento (<b>DOCTYPE</b>): indica que es un descriptor de despliegue de aplicación web.")]
    if v4:
        bloques.append(("<web-app", "Elemento raíz <b>web-app</b> con los espacios de nombres de Java EE y <b>version=\"4.0\"</b> (Servlet 4.0): habilita funciones modernas como las <b>anotaciones</b> (@WebServlet) y el EL de JSP 2.3."))
    else:
        bloques.append(("<web-app>", "Elemento raíz <b>web-app</b> en su versión mínima (Servlet 2.3): no declara espacios de nombres ni versión."))
    bloques.append(("<display-name>", "Nombre descriptivo de la aplicación (lo muestra el administrador de Tomcat)."))
    if extra:
        bloques += extra
    bloques.append(("</web-app>", "Cierre del descriptor."))
    return {
     "rol": "<b>web.xml</b>: descriptor de despliegue de la aplicación web. Vive en <i>WEB-INF</i> y es el archivo de configuración que lee el contenedor (Tomcat) al iniciar.",
     "defensa": "Aquí solo contiene el nombre de la aplicación porque todavía no hay servlets declarados por XML. Es el lugar donde se declararían servlets, filtros, páginas de error y configuración de sesión (ver Tema 08)." if not extra else
                "Es el descriptor clásico: los servlets se declaran con <b>&lt;servlet&gt;</b> (nombre + clase) y se mapean a una URL con <b>&lt;servlet-mapping&gt;</b> (mismo nombre + url-pattern). Es la alternativa a la anotación @WebServlet; el parcial pide conocer <b>ambas</b>.",
     "bloques": bloques}
