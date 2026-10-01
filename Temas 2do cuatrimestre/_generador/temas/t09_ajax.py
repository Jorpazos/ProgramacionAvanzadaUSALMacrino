from comunes import pom_war, webxml

B = "Codigo/023 - Clase 23-09/"
J = B + "src/main/java/edu/usal/"
W = B + "src/main/webapp/"


def js(err):
    url_txt = ("<b>url: '/registro'</b>: <b>ERROR</b> de esta versión: la ruta <b>empieza en la raíz del servidor</b> (<i>http://localhost:8080/registro</i>) y no incluye el <b>contexto</b> de la aplicación (<i>/clase-23-09</i>). El servidor responde 404, jQuery ejecuta la función <i>error</i> y el usuario ve «No se pudo comunicar con el servidor»."
               if err else
               "<b>url: contextPath + '/registro'</b>: la URL del servlet. <b>contextPath</b> es una constante que la JSP define con EL (<i>'${pageContext.request.contextPath}'</i>), por lo que se obtiene <i>/clase-23-09/registro</i> sin importar con qué nombre se despliegue el WAR.")
    return {
     "rol": "Código JavaScript con <b>jQuery</b> que intercepta el envío del formulario, hace una <b>petición AJAX (POST)</b> al servlet y muestra la respuesta con <b>SweetAlert2</b>." + (" Es la versión <b>con error</b> (ver defensa)." if err else ""),
     "defensa": ("Se conserva como <b>ejemplo del error clásico</b>: usar una ruta absoluta que ignora el contexto de la aplicación. La diferencia con <i>cliente.js</i> es una sola línea (la url) y otra en la JSP (que acá no define contextPath). " if err else "") +
                "<b>AJAX</b> permite comunicarse con el servidor <b>sin recargar la página</b>: el JS envía los datos en segundo plano y, cuando llega la respuesta, actualiza solo lo necesario. El protocolo acordado con el servlet es simple: la respuesta es texto con un <b>prefijo</b> («OK:» o «ERROR:») seguido del mensaje; el JS lo interpreta. En proyectos reales se suele usar <b>JSON</b>. La guía de estudio menciona también la <b>Fetch API</b>, la forma moderna sin jQuery.",
     "bloques": [
      ("", "<b>$(document).ready(...)</b>: jQuery ejecuta esta función cuando el HTML terminó de cargar (el DOM está listo), igual que esperar el evento load."),
      ("$('#formCliente').on('submit'", "<b>Listener del evento submit</b>: <i>$('#formCliente')</i> selecciona el formulario por su id (sintaxis CSS: # = id). <b>e.preventDefault()</b> cancela el envío normal del formulario (que recargaría la página)."),
      ("$.ajax({", "<b>$.ajax({…})</b>: abre la configuración de la petición AJAX."),
      ("url:", url_txt),
      ("type: 'POST'", "<b>type: 'POST'</b>: método HTTP (se modifica/crea datos). <b>data: $(this).serialize()</b> convierte todos los campos del formulario en el texto <i>nombre=…&amp;email=…</i> (usa los atributos <b>name</b> de los inputs). <b>dataType: 'text'</b>: se espera una respuesta de texto."),
      ("success: function(respuestaTexto)", "<b>success</b>: se ejecuta si el servidor respondió bien (código 2xx). Recibe el texto de la respuesta."),
      ("if (respuestaTexto.startsWith(\"OK:\"))", "Evalúa la respuesta: si <b>empieza con «OK:»</b> la operación fue exitosa, y <i>replace(\"OK:\", \"\")</i> deja solo el mensaje."),
      ("Swal.fire({", "<b>Swal.fire({…})</b> de <b>SweetAlert2</b>: muestra una ventana modal bonita (en lugar del feo <i>alert</i> del navegador) con título «¡Éxito!», texto, ícono <i>success</i> y botón «Aceptar»."),
      ("}).then((result)", "<b>.then(...)</b>: Swal devuelve una <b>promesa</b>; el código se ejecuta cuando el usuario cierra la ventana. Si <b>result.isConfirmed</b> (presionó Aceptar) limpia el formulario con <b>reset()</b> (el <i>[0]</i> obtiene el elemento DOM puro del objeto jQuery)."),
      ("} else {", "Si la respuesta <b>no</b> empieza con «OK:» (es «ERROR:…»), quita el prefijo y muestra el mensaje con ícono de <b>advertencia</b> (warning)."),
      ("error: function()", "<b>error</b>: se ejecuta si la <b>comunicación falla</b> (servidor caído, 404, 500, red). Muestra un SweetAlert con ícono de error."),
     ]}


def jsp(err):
    b = [
     ("", "Comentario JSP generado por el IDE; no llega al navegador."),
     ("<%@ page contentType", "Directiva page: tipo de contenido y codificación UTF-8."),
     ("<html>", "Apertura de html y head con el título «Clase 23-09 | Demo Ajax»."),
     ("<!-- Bootstrap 5 -->", "Hoja de estilos de <b>Bootstrap 5.3.3</b> desde un CDN."),
     ("<!-- SweetAlert2 CSS -->", "Hoja de estilos de <b>SweetAlert2</b>: da formato a las ventanas modales."),
     ("<!-- 1. Librerías" if not err else "<!-- Librerías", "<b>Librerías JavaScript externas (CDN)</b>: <b>jQuery 3.7.1</b> (para $ y $.ajax) y <b>SweetAlert2</b> (Swal.fire). Se cargan en el head, antes del código propio que las usa."),
     ("<body class=\"bg-light p-5\">", "Body con fondo claro y padding. <i>container</i> de ancho máximo 450px centrado y una <b>tarjeta</b> (card) con encabezado azul «Registrar Cliente»."),
     ("<!-- Formulario -->", "<b>Formulario</b> con <i>id=\"formCliente\"</i> (el id que busca el JS). <b>No tiene action ni method</b>: no se envía de forma tradicional; lo intercepta JavaScript. Dos campos: nombre (text) y email (email); los <b>name</b> son los parámetros que leerá el servlet."),
     ("<button type=\"submit\" class=\"btn btn-success", "Botón <b>submit</b> «Guardar Cliente»: dispara el evento submit que captura el JS."),
    ]
    if err:
        b.append(("<button type=\"button\" class=\"my-3", "Botón <i>type=\"button\"</i> (no envía el formulario) con <b>onclick=\"alert(...)\"</b>: muestra el <b>mensaje clásico</b> del navegador, para comparar con SweetAlert."))
        b.append(("<script src=\"../js/cliente-err.js\">", "Carga el JS de la versión con error. La ruta <i>../js/</i> es relativa a la carpeta <i>formularios</i>. Esta versión <b>no define contextPath</b>."))
    else:
        b.append(("<script>", "<b>Script en línea</b> que define la constante <b>contextPath</b> con <b>EL</b>: <i>'${pageContext.request.contextPath}'</i>. Como la JSP se procesa en el servidor, el navegador recibe el valor real (por ejemplo <i>'/clase-23-09'</i>). Es la técnica para que el JS externo conozca el contexto."))
        b.append(("<script src=\"../js/cliente.js\">", "Carga el JavaScript de la lógica AJAX (<b>cliente.js</b>), después de definir contextPath."))
    return {
     "rol": "Página (JSP) con el <b>formulario de registro de clientes</b> que se envía con AJAX." + (" Es la versión <b>con error</b> de la demostración." if err else ""),
     "defensa": ("Versión pensada para mostrar los <b>errores típicos</b>: el JS usa una URL sin contexto (por eso falla) y se agrega un botón con <i>alert()</i> clásico para comparar con SweetAlert. " if err else "") +
                "Es una JSP porque necesita que el servidor calcule el <b>contextPath</b> (con EL) y se lo pase al JavaScript; con HTML estático eso no sería posible. El formulario no tiene action: el <b>submit</b> lo captura jQuery.",
     "bloques": b}


TEMA = {
 "carpeta": "Tema 09 - AJAX con jQuery y SweetAlert",
 "titulo": "Tema 09 · AJAX con jQuery y SweetAlert",
 "clases": "Clase 023 (23-09) · Teoría: «Servlets» (Clase023_Applet_Servlet.pdf) + guía de estudio (JS, AJAX y SweetAlert)",
 "intro": [
  ("h1", "1. Qué es AJAX"),
  ("p", "<b>AJAX (Asynchronous JavaScript And XML)</b> es la técnica que permite a una página <b>comunicarse con el servidor en segundo plano, sin recargarse</b>. En una aplicación tradicional, cada acción del usuario (enviar un formulario) recarga toda la página. Con AJAX, el JavaScript <b>envía la petición</b>, el usuario sigue usando la página y, cuando llega la respuesta, el JS <b>actualiza solo la parte necesaria</b> del DOM. Resultado: interfaces más rápidas y fluidas (mejor UX)."),
  ("p", "A pesar del nombre, hoy el formato de intercambio suele ser <b>JSON</b> (o texto) en lugar de XML. «Asincrónico» significa que el navegador <b>no se bloquea esperando</b>: registra qué hacer cuando llegue la respuesta (<i>callbacks</i> o <i>promesas</i>)."),
  ("codigo", "Usuario ---(submit)---> JavaScript intercepta, evita la recarga (preventDefault)\n                              |\n                              +---POST /contexto/registro (en segundo plano)---> Servlet\n                              |                                                    |\n                              |<-------- \"OK:El cliente X fue registrado\" ---------+\n                              v\n                      JS lee la respuesta y muestra SweetAlert (la página no se recargó)"),
  ("h1", "2. Formas de hacer AJAX"),
  ("tabla", [["Herramienta", "Ejemplo", "Notas"],
    ["<b>jQuery $.ajax</b> (esta clase)", "<i>$.ajax({url, type:'POST', data: $(form).serialize(), success: fn, error: fn})</i>", "Librería que simplifica el DOM y AJAX; se incluye desde un CDN. <i>serialize()</i> arma <i>campo=valor&amp;…</i> con los atributos name"],
    ["<b>Fetch API</b> (nativa, moderna)", "<i>fetch(url, {method:'POST', body: new URLSearchParams(datos)}).then(r =&gt; r.text()).then(...)</i>", "Sin librerías, basada en promesas. Los datos se envían con <b>URLSearchParams</b> o como <b>JSON</b> (<i>JSON.stringify</i> + cabecera Content-Type: application/json). Es la que menciona la guía de estudio."],
    ["XMLHttpRequest", "<i>new XMLHttpRequest()</i>", "La forma original; hoy casi no se usa directamente"]]),
  ("h1", "3. El servlet que responde a AJAX"),
  ("p", "Para el servidor, una petición AJAX es una petición HTTP común: el servlet lee los parámetros con <b>getParameter</b> y escribe la respuesta. La diferencia es <b>qué devuelve</b>: en vez de HTML completo (que haría hacer forward a una JSP), devuelve solo el dato necesario (un texto o JSON) con el <b>tipo MIME</b> correcto (<i>text/plain</i> o <i>application/json</i>), y no hay forward ni redirect. Detalles correctos: <b>req.setCharacterEncoding(\"UTF-8\")</b> antes de leer parámetros (tildes) y <b>resp.setCharacterEncoding(\"UTF-8\")</b>."),
  ("h1", "4. SweetAlert2"),
  ("p", "<b>SweetAlert2</b> es una librería que reemplaza los diálogos nativos del navegador (<i>alert, confirm, prompt</i>) por <b>ventanas modales con mejor diseño</b>. Se incluye con un CDN (CSS + JS). Se usa con <b>Swal.fire({ title, text, icon, confirmButtonText })</b>: el <i>icon</i> puede ser <i>success, error, warning, info, question</i>. Devuelve una promesa: <i>.then(result =&gt; { if (result.isConfirmed) {…} })</i> para saber si el usuario confirmó. Para confirmar acciones se agrega <i>showCancelButton: true</i>. Es uno de los puntos opcionales del parcial (junto con Bootstrap) y la solución lo usa para avisos y confirmaciones."),
  ("h1", "5. La ruta de contexto (contextPath)"),
  ("p", "Una aplicación web se publica bajo un <b>contexto</b> (el nombre del WAR): <i>http://localhost:8080/clase-23-09/…</i>. Una URL que empieza con <b>/</b> (por ejemplo <i>'/registro'</i>) se interpreta desde la <b>raíz del servidor</b>, <b>ignorando el contexto</b>, y devuelve 404. La forma correcta es anteponer el contexto: en JSP con <b>${pageContext.request.contextPath}</b> (o <i>request.getContextPath()</i> en un servlet). Es el error que ilustran los archivos «-err» de la clase."),
  ("nota", "<b>Seguridad:</b> los datos que llegan por AJAX deben validarse en el servidor igual que los de un formulario; el JS del cliente no es confiable. Y al mostrar texto recibido en el DOM usar <i>textContent</i> (no innerHTML) para evitar XSS."),
 ],
 "clases_detalle": [
  {"titulo": "Clase 023 – 23-09: formulario con AJAX (jQuery), servlet de registro y SweetAlert2",
   "resumen": [("p", "Se implementa el registro de un cliente <b>sin recargar la página</b>: el formulario (<i>formu-registro.jsp</i>) es interceptado por <b>cliente.js</b>, que llama por AJAX al <b>RegistroServlet</b>; este responde con un texto <i>OK:…</i> o <i>ERROR:…</i> y el JS lo muestra con <b>SweetAlert2</b>. Los archivos <b>«-err»</b> son una versión con errores intencionales (ruta sin contexto) para comparar y aprender a depurarlos.")],
   "archivos": {
    B + "pom.xml": pom_war("clase-23-09", jstl=True),
    W + "WEB-INF/web.xml": webxml(v4=True),
    J + "servlet/RegistroServlet.java": {
     "rol": "Servlet que <b>recibe el registro por POST</b>, valida que los campos no estén vacíos y responde con un texto «OK:…» o «ERROR:…».",
     "defensa": "Cumple varios puntos del parcial: <b>@WebServlet</b> (anotación), solo <b>doPost</b> (modifica datos: método HTTP correcto), <b>validación en el servidor</b> (aunque el cliente ya valide) y respuesta de <b>tipo MIME text/plain</b> con UTF-8. No hace forward ni redirect porque responde a una llamada AJAX. En una aplicación real aquí se llamaría a un DAO para guardar; la clase solo simula el alta.",
     "bloques": [
      ("", "Paquete de los servlets."),
      ("import javax.servlet.ServletException", "@imports"),
      ("@WebServlet(\"/registro\")", "<b>Anotación</b>: el servlet responde en la URL <b>/registro</b> (dentro del contexto de la aplicación). Forma abreviada: solo el patrón."),
      ("public class RegistroServlet", "Extiende HttpServlet."),
      ("protected void doPost", "Solo se sobrescribe <b>doPost</b>: el servlet atiende únicamente peticiones POST (un GET devolvería el error por defecto)."),
      ("req.setCharacterEncoding", "Fija UTF-8 para <b>leer</b> los parámetros (necesario <b>antes</b> del primer getParameter) y la respuesta como <b>text/plain</b> con UTF-8."),
      ("String nombre = req.getParameter", "Lee los parámetros <i>nombre</i> y <i>email</i> (los <b>name</b> de los inputs del formulario) y obtiene el <b>PrintWriter</b> de la respuesta."),
      ("if (nombre != null", "<b>Validación del servidor</b>: ambos deben existir y no estar vacíos (<i>trim()</i>). Si es válido, responde <b>«OK:»</b> más el mensaje de éxito; si no, <b>«ERROR:»</b> más el motivo. Ese prefijo es el «protocolo» que interpreta el JavaScript."),
      ("out.flush()", "Vacía el buffer para asegurar que la respuesta se envíe."),
     ]},
    W + "formularios/formu-registro.jsp": jsp(False),
    W + "js/cliente.js": js(False),
    W + "formularios/formu-registro-err.jsp": jsp(True),
    W + "js/cliente-err.js": js(True),
   }},
 ],
 "cierre": [
  ("h1", "Resumen para estudiar"),
  ("li", "<b>AJAX</b> = petición asincrónica al servidor desde JavaScript, sin recargar la página; el JS actualiza solo el DOM."),
  ("li", "jQuery: <i>$(document).ready</i>, <i>$('#id').on('submit', …)</i>, <i>e.preventDefault()</i>, <i>$.ajax({url, type, data, success, error})</i>, <i>serialize()</i>. Alternativa moderna: <b>fetch</b>."),
  ("li", "El servlet de AJAX responde <b>solo el dato</b> (texto/JSON) con el tipo MIME correcto; no usa forward a una JSP."),
  ("li", "<b>SweetAlert2</b>: <i>Swal.fire({title, text, icon})</i> → promesa <i>.then(result.isConfirmed)</i>."),
  ("li", "<b>contextPath</b>: nunca usar rutas que empiecen con / sin el contexto: en JSP <i>${pageContext.request.contextPath}</i>."),
  ("li", "Validar siempre también en el servidor."),
  ("h1", "Cómo se aplica en el parcial de logística"),
  ("li", "<b>app.js</b> de la solución usa SweetAlert para mostrar los avisos del servidor y para confirmar altas, bajas e inicio/fin de viajes. La guía de estudio propone además enviar el origen y el destino del viaje con <b>fetch</b>; la solución entregada resuelve el flujo con formularios y GET/POST tradicionales (más simple y robusto), pudiéndose migrar a fetch con el mismo servidor."),
  ("h1", "Preguntas típicas"),
  ("q", "¿Qué ventaja tiene AJAX?"),
  ("p", "No recarga toda la página: es más rápido y mejora la experiencia del usuario, porque solo se intercambia y actualiza lo necesario."),
  ("q", "¿Por qué falla la versión «-err»?"),
  ("p", "Porque usa la URL '/registro' sin el contexto de la aplicación: apunta a la raíz del servidor, donde no existe el servlet, y devuelve 404. Hay que usar contextPath + '/registro'."),
  ("q", "¿Para qué sirve e.preventDefault()?"),
  ("p", "Cancela el envío normal del formulario (que recargaría la página) para que sea el JavaScript quien haga la petición AJAX."),
 ],
}
