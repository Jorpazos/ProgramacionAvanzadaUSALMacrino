#!/usr/bin/env python3
"""
Genera un PDF por cada tema de 'Temas 2do cuatrimestre'.

Cada PDF tiene: explicacion del tema, resumen de cada clase y TODO el codigo de las clases
numerado linea por linea con su explicacion. El script verifica que ninguna linea del
codigo quede sin explicar.

Uso (desde cualquier carpeta):   python3 "Temas 2do cuatrimestre/_generador/generar_pdfs.py" [numero_de_tema]
Requiere: reportlab
"""
import importlib
import os
import re
import sys

from reportlab.lib import colors
from reportlab.lib.enums import TA_JUSTIFY
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle
from reportlab.lib.units import cm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (BaseDocTemplate, Frame, KeepTogether, PageBreak, PageTemplate, Paragraph,
                                Preformatted, Spacer, Table, TableStyle)

AQUI = os.path.dirname(os.path.abspath(__file__))
RAIZ = os.path.dirname(AQUI)          # carpeta 'Temas 2do cuatrimestre'
sys.path.insert(0, os.path.join(AQUI, "temas"))
sys.path.insert(0, AQUI)

FUENTES = "/usr/share/fonts/truetype/dejavu/"
pdfmetrics.registerFont(TTFont("Sans", FUENTES + "DejaVuSans.ttf"))
pdfmetrics.registerFont(TTFont("Sans-Bold", FUENTES + "DejaVuSans-Bold.ttf"))
pdfmetrics.registerFont(TTFont("Mono", FUENTES + "DejaVuSansMono.ttf"))
pdfmetrics.registerFontFamily("Sans", normal="Sans", bold="Sans-Bold", italic="Sans", boldItalic="Sans-Bold")

AZUL = colors.HexColor("#1f3a5f")
GRIS = colors.HexColor("#f2f4f7")
VERDE = colors.HexColor("#e9f5ec")
AMARILLO = colors.HexColor("#fff7df")

E = {
    "titulo": ParagraphStyle("titulo", fontName="Sans-Bold", fontSize=22, leading=27, textColor=AZUL, spaceAfter=8),
    "sub": ParagraphStyle("sub", fontName="Sans", fontSize=11, leading=15, textColor=colors.HexColor("#444444"), spaceAfter=4),
    "h1": ParagraphStyle("h1", fontName="Sans-Bold", fontSize=15, leading=19, textColor=AZUL, spaceBefore=6, spaceAfter=7),
    "h2": ParagraphStyle("h2", fontName="Sans-Bold", fontSize=11.5, leading=15, textColor=AZUL, spaceBefore=8, spaceAfter=4),
    "p": ParagraphStyle("p", fontName="Sans", fontSize=9, leading=12.8, alignment=TA_JUSTIFY, spaceAfter=4),
    "li": ParagraphStyle("li", fontName="Sans", fontSize=9, leading=12.8, leftIndent=14, bulletIndent=3, spaceAfter=2),
    "exp": ParagraphStyle("exp", fontName="Sans", fontSize=8.6, leading=11.8, alignment=TA_JUSTIFY),
    "celda": ParagraphStyle("celda", fontName="Sans", fontSize=8.4, leading=11.2),
    "codigo": ParagraphStyle("codigo", fontName="Mono", fontSize=6.6, leading=8.4),
    "q": ParagraphStyle("q", fontName="Sans-Bold", fontSize=9, leading=12.5, textColor=AZUL, spaceBefore=6, spaceAfter=1),
    "nota": ParagraphStyle("nota", fontName="Sans", fontSize=8.6, leading=12, alignment=TA_JUSTIFY),
}
ANCHO = 118


def contenido(items):
    sal = []
    for tipo, texto in items:
        if tipo == "salto":
            sal.append(PageBreak())
        elif tipo == "li":
            sal.append(Paragraph(texto, E["li"], bulletText="•"))
        elif tipo == "codigo":
            t = Table([[Preformatted(texto, E["codigo"])]], colWidths=[17.2 * cm])
            t.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, -1), GRIS), ("BOX", (0, 0), (-1, -1), 0.4, colors.grey),
                                   ("LEFTPADDING", (0, 0), (-1, -1), 6), ("TOPPADDING", (0, 0), (-1, -1), 4),
                                   ("BOTTOMPADDING", (0, 0), (-1, -1), 4)]))
            sal += [t, Spacer(1, 5)]
        elif tipo == "nota":
            t = Table([[Paragraph(texto, E["nota"])]], colWidths=[17.2 * cm])
            t.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, -1), AMARILLO), ("BOX", (0, 0), (-1, -1), 0.5, colors.HexColor("#d9b44a")),
                                   ("LEFTPADDING", (0, 0), (-1, -1), 6), ("TOPPADDING", (0, 0), (-1, -1), 4),
                                   ("BOTTOMPADDING", (0, 0), (-1, -1), 5)]))
            sal += [t, Spacer(1, 6)]
        elif tipo == "tabla":
            filas = [[Paragraph(str(c), E["celda"]) for c in fila] for fila in texto]
            t = Table(filas, repeatRows=1)
            t.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, 0), GRIS), ("GRID", (0, 0), (-1, -1), 0.4, colors.grey),
                                   ("VALIGN", (0, 0), (-1, -1), "TOP")]))
            sal += [t, Spacer(1, 6)]
        else:
            sal.append(Paragraph(texto, E[tipo]))
    return sal


def cortar(linea):
    linea = linea.expandtabs(4)
    if len(linea) <= ANCHO:
        return [linea]
    partes, sangria = [], len(linea) - len(linea.lstrip())
    while len(linea) > ANCHO:
        corte = linea.rfind(" ", sangria + 10, ANCHO)
        if corte == -1:
            corte = ANCHO
        partes.append(linea[:corte])
        linea = " " * (sangria + 4) + "↳ " + linea[corte:].lstrip()
    partes.append(linea)
    return partes


def imports_de(lineas):
    return [m.group(2).split(".")[-1] for l in lineas
            for m in [re.match(r"\s*import\s+(static\s+)?([\w.]+);", l)] if m]


def bloques_de(ruta, lineas, bloques):
    inicios, pos = [], 0
    for i, (ancla, texto) in enumerate(bloques):
        if i == 0:
            inicios.append((1, texto))
            continue
        opcional = ancla.startswith("?")
        ancla = ancla.lstrip("?").split("\n")[0]
        hallada = next((n for n in range(pos, len(lineas)) if ancla in lineas[n]), None)
        if hallada is None or hallada + 1 <= inicios[-1][0]:
            if opcional:
                continue
            raise SystemExit(f"[{ruta}] ancla no encontrada o fuera de orden: {ancla!r}")
        inicios.append((hallada + 1, texto))
        pos = hallada + 1
    res = []
    for i, (ini, texto) in enumerate(inicios):
        fin = inicios[i + 1][0] - 1 if i + 1 < len(inicios) else len(lineas)
        if texto == "@imports":
            texto = ("Importaciones: <b>" + ", ".join(imports_de(lineas[ini - 1:fin])) +
                     "</b>. Cada <i>import</i> permite usar esa clase por su nombre corto en lugar de escribir el paquete completo.")
        res.append((ini, fin, texto))
    return res


def archivo(carpeta_tema, ruta, d):
    with open(os.path.join(carpeta_tema, ruta), encoding="utf-8") as f:
        lineas = f.read().split("\n")
    if lineas and lineas[-1] == "":
        lineas.pop()
    bl = bloques_de(ruta, lineas, d["bloques"])
    assert sum(f - i + 1 for i, f, _ in bl) == len(lineas)
    sal = [Paragraph(ruta.split("Codigo/", 1)[-1].replace("/", " / "), E["h2"]),
           Paragraph("<b>Qué es y para qué sirve:</b> " + d["rol"], E["p"]),
           Paragraph("<b>Cómo defenderlo / qué decir:</b> " + d["defensa"], E["p"]), Spacer(1, 3)]
    for ini, fin, texto in bl:
        cod = []
        for n in range(ini, fin + 1):
            partes = cortar(lineas[n - 1])
            cod.append(f"{n:>4} │ {partes[0]}")
            cod += [f"     │ {x}" for x in partes[1:]]
        rango = f"Línea {ini}" if ini == fin else f"Líneas {ini} a {fin}"
        t = Table([[Preformatted("\n".join(cod), E["codigo"])], [Paragraph(f"<b>{rango}:</b> {texto}", E["exp"])]],
                  colWidths=[17.2 * cm])
        t.setStyle(TableStyle([("BACKGROUND", (0, 0), (0, 0), GRIS), ("BACKGROUND", (0, 1), (0, 1), VERDE),
                               ("BOX", (0, 0), (-1, -1), 0.4, colors.HexColor("#b8c0cc")),
                               ("LEFTPADDING", (0, 0), (-1, -1), 5), ("RIGHTPADDING", (0, 0), (-1, -1), 5),
                               ("TOPPADDING", (0, 0), (-1, -1), 3), ("BOTTOMPADDING", (0, 0), (-1, -1), 4)]))
        sal += [KeepTogether([t, Spacer(1, 4)]) if fin - ini < 28 else t]
        if fin - ini >= 28:
            sal.append(Spacer(1, 4))
    return sal, len(lineas)


def construir(modulo):
    T = importlib.import_module(modulo).TEMA
    carpeta = os.path.join(RAIZ, T["carpeta"])
    historia = [Paragraph(T["titulo"], E["titulo"]), Paragraph(T["clases"], E["sub"]),
                Paragraph("Programación Avanzada · Prof. Macrino Matías · Universidad del Salvador – Sede Pilar · 2.º cuatrimestre", E["sub"]),
                Spacer(1, 8)]
    historia += contenido(T["intro"])
    total = 0
    historia += [PageBreak(), Paragraph("Código de las clases, explicado línea por línea", E["h1"]),
                 Paragraph("Cada recuadro gris reproduce líneas reales del archivo (con su número de línea) y el recuadro verde "
                           "de abajo explica qué hacen y por qué están escritas así.", E["p"])]
    for clase in T["clases_detalle"]:
        historia += [Paragraph(clase["titulo"], E["h1"])]
        historia += contenido(clase["resumen"])
        for ruta, d in clase["archivos"].items():
            flow, n = archivo(carpeta, ruta, d)
            total += n
            historia += flow
    historia += [PageBreak()] + contenido(T["cierre"])

    nombre = os.path.join(carpeta, T["carpeta"] + ".pdf")

    def pie(c, doc):
        c.saveState()
        c.setFont("Sans", 7.5)
        c.setFillColor(colors.HexColor("#666666"))
        c.drawString(2 * cm, 1.2 * cm, T["carpeta"])
        c.drawRightString(A4[0] - 2 * cm, 1.2 * cm, f"Página {doc.page}")
        c.restoreState()

    doc = BaseDocTemplate(nombre, pagesize=A4, leftMargin=1.9 * cm, rightMargin=1.9 * cm, topMargin=1.7 * cm,
                          bottomMargin=1.8 * cm, title=T["titulo"], author="Programación Avanzada – USAL Pilar")
    doc.addPageTemplates([PageTemplate(id="p", frames=[Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height)], onPage=pie)])
    doc.build(historia)
    print(f"{T['carpeta']}.pdf  ({total} líneas de código explicadas)")


def main():
    modulos = sorted(f[:-3] for f in os.listdir(os.path.join(AQUI, "temas")) if re.match(r"t\d\d_.*\.py$", f))
    if len(sys.argv) > 1:
        modulos = [m for m in modulos if m.startswith("t" + sys.argv[1].zfill(2))]
    for m in modulos:
        construir(m)


if __name__ == "__main__":
    main()
