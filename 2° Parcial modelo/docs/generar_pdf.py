#!/usr/bin/env python3
"""
Genera docs/Explicacion_y_Defensa_Parcial.pdf.

Toma cada archivo fuente del proyecto, lo numera linea por linea y, debajo de cada
bloque de lineas, agrega la explicacion y la defensa de esas lineas. Verifica que
TODAS las lineas del archivo queden cubiertas por algun bloque explicado.

Uso:  python3 docs/generar_pdf.py
"""
import os
import re
import sys
from xml.sax.saxutils import escape

from reportlab.lib import colors
from reportlab.lib.enums import TA_JUSTIFY
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import cm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (BaseDocTemplate, Frame, KeepTogether, PageBreak, PageTemplate, Paragraph,
                                Preformatted, Spacer, Table, TableStyle, XPreformatted)

RAIZ = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
sys.path.insert(0, os.path.dirname(__file__))

from contenido import CAPITULOS_INICIALES, CAPITULOS_FINALES  # noqa: E402
from exp_dao_modelo import EXPLICACIONES as E1  # noqa: E402
from exp_dao_jdbc import EXPLICACIONES as E2  # noqa: E402
from exp_web import EXPLICACIONES as E3  # noqa: E402
from exp_vistas import EXPLICACIONES as E4  # noqa: E402
from exp_otros import EXPLICACIONES as E5  # noqa: E402

FUENTES = "/usr/share/fonts/truetype/dejavu/"
pdfmetrics.registerFont(TTFont("Sans", FUENTES + "DejaVuSans.ttf"))
pdfmetrics.registerFont(TTFont("Sans-Bold", FUENTES + "DejaVuSans-Bold.ttf"))
pdfmetrics.registerFont(TTFont("Sans-Italic", FUENTES + "DejaVuSans.ttf"))
pdfmetrics.registerFont(TTFont("Mono", FUENTES + "DejaVuSansMono.ttf"))
pdfmetrics.registerFontFamily("Sans", normal="Sans", bold="Sans-Bold", italic="Sans-Italic", boldItalic="Sans-Bold")

AZUL = colors.HexColor("#1f3a5f")
GRIS = colors.HexColor("#f2f4f7")
VERDE = colors.HexColor("#e9f5ec")

ESTILOS = {
    "titulo": ParagraphStyle("titulo", fontName="Sans-Bold", fontSize=22, leading=27, textColor=AZUL, spaceAfter=10),
    "h1": ParagraphStyle("h1", fontName="Sans-Bold", fontSize=16, leading=20, textColor=AZUL, spaceBefore=6, spaceAfter=8),
    "h2": ParagraphStyle("h2", fontName="Sans-Bold", fontSize=12, leading=15, textColor=AZUL, spaceBefore=8, spaceAfter=4),
    "h3": ParagraphStyle("h3", fontName="Sans-Bold", fontSize=10, leading=13, textColor=colors.black, spaceBefore=6, spaceAfter=3),
    "p": ParagraphStyle("p", fontName="Sans", fontSize=9, leading=12.5, alignment=TA_JUSTIFY, spaceAfter=4),
    "li": ParagraphStyle("li", fontName="Sans", fontSize=9, leading=12.5, leftIndent=14, bulletIndent=3, spaceAfter=2),
    "exp": ParagraphStyle("exp", fontName="Sans", fontSize=8.6, leading=11.8, alignment=TA_JUSTIFY),
    "celda": ParagraphStyle("celda", fontName="Sans", fontSize=8.4, leading=11.2),
    "q": ParagraphStyle("q", fontName="Sans-Bold", fontSize=9, leading=12.5, textColor=AZUL, spaceBefore=6, spaceAfter=1),
    "codigo": ParagraphStyle("codigo", fontName="Mono", fontSize=6.6, leading=8.4),
    "rango": ParagraphStyle("rango", fontName="Sans-Bold", fontSize=7.5, leading=9, textColor=colors.HexColor("#555555")),
    "peq": ParagraphStyle("peq", fontName="Sans-Italic", fontSize=8, leading=10.5, textColor=colors.HexColor("#555555")),
}

ANCHO_CODIGO = 118  # caracteres por linea de codigo antes de cortar


def p(texto, estilo="p"):
    return Paragraph(texto, ESTILOS[estilo])


def renderizar_contenido(items):
    """Convierte la lista de (tipo, texto) de contenido.py en flowables."""
    salida = []
    for tipo, texto in items:
        if tipo == "salto":
            salida.append(PageBreak())
        elif tipo == "li":
            salida.append(Paragraph(texto, ESTILOS["li"], bulletText="•"))
        elif tipo == "tabla":
            filas = [[Paragraph(str(c), ESTILOS["celda"]) for c in fila] for fila in texto]
            t = Table(filas, colWidths=None, repeatRows=1)
            t.setStyle(TableStyle([
                ("BACKGROUND", (0, 0), (-1, 0), GRIS), ("GRID", (0, 0), (-1, -1), 0.4, colors.grey),
                ("VALIGN", (0, 0), (-1, -1), "TOP"), ("FONTNAME", (0, 0), (-1, 0), "Sans-Bold")]))
            salida.append(t)
            salida.append(Spacer(1, 6))
        elif tipo == "codigo":
            salida.append(Preformatted(texto, ESTILOS["codigo"], bulletText=None))
            salida.append(Spacer(1, 4))
        else:
            salida.append(p(texto, tipo))
    return salida


def cortar(linea):
    """Corta lineas largas para que entren en la hoja (se marca la continuacion con ↳)."""
    linea = linea.expandtabs(4)
    if len(linea) <= ANCHO_CODIGO:
        return [linea]
    partes, sangria = [], len(linea) - len(linea.lstrip())
    while len(linea) > ANCHO_CODIGO:
        corte = linea.rfind(" ", sangria + 10, ANCHO_CODIGO)
        if corte == -1:
            corte = ANCHO_CODIGO
        partes.append(linea[:corte])
        linea = " " * (sangria + 4) + "↳ " + linea[corte:].lstrip()
    partes.append(linea)
    return partes


def imports_texto(lineas):
    clases = []
    for l in lineas:
        m = re.match(r"\s*import\s+(static\s+)?([\w.]+);", l)
        if m:
            clases.append(m.group(2).split(".")[-1])
    return clases


def dividir_en_bloques(ruta, lineas, bloques):
    """Devuelve [(inicio, fin, texto)] (lineas 1-indexadas) y valida la cobertura total."""
    inicios = []
    posicion = 0
    for idx, (ancla, texto) in enumerate(bloques):
        if idx == 0:
            inicios.append((1, texto))
            continue
        encontrado = None
        for n in range(posicion, len(lineas)):
            if ancla.split("\n")[0] in lineas[n]:
                encontrado = n
                break
        if encontrado is None or encontrado + 1 <= inicios[-1][0]:
            raise SystemExit(f"[{ruta}] ancla no encontrada (o fuera de orden): {ancla!r}")
        inicios.append((encontrado + 1, texto))
        posicion = encontrado + 1
    resultado = []
    for i, (ini, texto) in enumerate(inicios):
        fin = inicios[i + 1][0] - 1 if i + 1 < len(inicios) else len(lineas)
        if texto == "@imports":
            texto = "Importaciones de las clases que usa este archivo: <b>" + ", ".join(
                imports_texto(lineas[ini - 1:fin])) + "</b>. Java necesita declararlas para poder nombrarlas sin su paquete completo."
        resultado.append((ini, fin, texto))
    return resultado


def flowables_archivo(ruta, datos):
    with open(os.path.join(RAIZ, ruta), encoding="utf-8") as f:
        lineas = f.read().split("\n")
    if lineas and lineas[-1] == "":
        lineas.pop()
    bloques = dividir_en_bloques(ruta, lineas, datos["bloques"])
    cubiertas = sum(fin - ini + 1 for ini, fin, _ in bloques)
    assert cubiertas == len(lineas), (ruta, cubiertas, len(lineas))

    sal = [p(ruta.replace("/", " / "), "h2"), p("<b>Para qué existe:</b> " + datos["rol"], "p"),
           p(f"<b>Defensa:</b> {datos['defensa']}", "p"), Spacer(1, 4)]
    for ini, fin, texto in bloques:
        codigo = []
        for n in range(ini, fin + 1):
            partes = cortar(lineas[n - 1])
            codigo.append(f"{n:>4} │ {partes[0]}")
            codigo.extend(f"     │ {x}" for x in partes[1:])
        rango = f"Línea {ini}" if ini == fin else f"Líneas {ini} a {fin}"
        tabla = Table([[Preformatted("\n".join(codigo), ESTILOS["codigo"])],
                       [Paragraph(f"<b>{rango}:</b> {texto}", ESTILOS["exp"])]], colWidths=[17.2 * cm])
        tabla.setStyle(TableStyle([
            ("BACKGROUND", (0, 0), (0, 0), GRIS), ("BACKGROUND", (0, 1), (0, 1), VERDE),
            ("BOX", (0, 0), (-1, -1), 0.4, colors.HexColor("#b8c0cc")),
            ("LEFTPADDING", (0, 0), (-1, -1), 5), ("RIGHTPADDING", (0, 0), (-1, -1), 5),
            ("TOPPADDING", (0, 0), (-1, -1), 3), ("BOTTOMPADDING", (0, 0), (-1, -1), 4)]))
        # Bloques cortos no se parten entre paginas; los largos si pueden partirse
        sal.append(KeepTogether([tabla, Spacer(1, 4)]) if fin - ini < 28 else tabla)
        if fin - ini >= 28:
            sal.append(Spacer(1, 4))
    return sal, len(lineas)


def pie(canvas, doc):
    canvas.saveState()
    canvas.setFont("Sans", 7.5)
    canvas.setFillColor(colors.HexColor("#666666"))
    canvas.drawString(2 * cm, 1.2 * cm, "Programación Avanzada – Parcial 2do Cuatrimestre 2025 – Explicación y defensa del código")
    canvas.drawRightString(A4[0] - 2 * cm, 1.2 * cm, f"Página {doc.page}")
    canvas.restoreState()


def main():
    salida = os.path.join(RAIZ, "docs", "Explicacion_y_Defensa_Parcial.pdf")
    doc = BaseDocTemplate(salida, pagesize=A4, leftMargin=1.9 * cm, rightMargin=1.9 * cm, topMargin=1.7 * cm,
                          bottomMargin=1.8 * cm, title="Explicación y defensa del código - Parcial Programación Avanzada",
                          author="Parcial 2do Cuatrimestre 2025")
    marco = Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height, id="m")
    doc.addPageTemplates([PageTemplate(id="p", frames=[marco], onPage=pie)])

    historia = renderizar_contenido(CAPITULOS_INICIALES)
    historia += [PageBreak(), p("Parte III – Código fuente explicado línea por línea", "h1"),
                 p("A continuación está <b>todo el código del proyecto</b>. Cada recuadro gris muestra líneas reales del "
                   "archivo con su número; el recuadro verde de abajo explica qué hacen esas líneas y por qué se "
                   "escribieron así (la defensa). Los archivos siguen el orden de las capas: base de datos, dominio, "
                   "acceso a datos (DAO), controladores, filtros y vistas.", "p")]
    total_lineas = 0
    secciones = [("Base de datos y configuración Maven", E5), ("Proyecto DAO – dominio (modelo) y excepciones", E1),
                 ("Proyecto DAO – acceso a datos JDBC", E2), ("Proyecto MVC – controladores, filtros y utilidades", E3),
                 ("Proyecto MVC – vistas (JSP + JSTL + JS)", E4)]
    for titulo, dic in secciones:
        historia += [PageBreak(), p(titulo, "h1")]
        for ruta, datos in dic.items():
            flow, n = flowables_archivo(ruta, datos)
            total_lineas += n
            historia += flow
    historia += [PageBreak()] + renderizar_contenido(CAPITULOS_FINALES)
    doc.build(historia)
    print(f"PDF generado: {salida} ({total_lineas} líneas de código explicadas)")


if __name__ == "__main__":
    main()
