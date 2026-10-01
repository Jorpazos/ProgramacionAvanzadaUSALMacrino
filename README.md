# Programación Avanzada – USAL Sede Pilar

Material ordenado del 2.º cuatrimestre: el **parcial modelo** resuelto y los **temas de cursada** con un PDF por tema.

```
.
├── 2° Parcial modelo/              ← enunciado del parcial (15/10/2025) + solución completa + PDF de defensa
│   ├── Parcial 2do cuatrimestre - 151025.pdf
│   ├── logistica-dao/  logistica-web/  db/  pom.xml
│   ├── docs/Explicacion_y_Defensa_Parcial.pdf
│   └── README.md                   ← cómo instalar y ejecutar
└── Temas 2do cuatrimestre/         ← un tema por carpeta, cada una con su PDF explicativo
    ├── Tema 01 - JDBC (Statement, PreparedStatement y CallableStatement)/   clases 014 y 015
    ├── Tema 02 - Singleton, DAO con transacciones y Maven multimodulo/        clase 016
    ├── Tema 03 - HTML5 (estructura, formularios y tablas)/                    clase 017
    ├── Tema 04 - JavaScript y manejo del DOM/                                 clase 018
    ├── Tema 05 - Servidor web y JSP basico (scriptlets y formularios)/        clase 019
    ├── Tema 06 - JSP (include y Session)/                                     clase 020
    ├── Tema 07 - JSTL y Cookies/                                              clase 021
    ├── Tema 08 - Servlets (web.xml, anotaciones, forward)/                    clase 022
    ├── Tema 09 - AJAX con jQuery y SweetAlert/                                clase 023
    ├── Guia de estudio integral (2do parcial).txt
    └── _generador/                 ← scripts que generan los PDF (reportlab)
```

Cada carpeta de tema contiene:

| Elemento | Contenido |
|---|---|
| `Tema NN - ….pdf` | Explicación del tema (cómo es, cómo funciona) + **todo el código de las clases explicado línea por línea**, con la forma de defenderlo y preguntas típicas |
| `Codigo/` | Código fuente original de las clases del tema (copiado del repositorio compartido de la cátedra) |
| `Teoria/` | Los PDF de teoría del profesor correspondientes al tema |

## Criterio de división

* **Parcial modelo**: la práctica del parcial del 15/10/2025 (aplicación de logística) con su solución y su defensa.
* **Temas del 2.º cuatrimestre**: clases 016 a 023 (05-08 al 23-09), una carpeta por tema. Se agregó como **Tema 01** el repaso de JDBC (clases 014 y 015, que se dictaron al cierre del 1.º cuatrimestre) porque el parcial exige los tres tipos de sentencia JDBC y las clases posteriores se apoyan en eso.
* Las clases 017 a 023 dan teoría en PDFs que abarcan varias clases (por ejemplo `Clase020_jsp.pdf` cubre los temas 05, 06 y 07); por eso algunos PDF de teoría aparecen copiados en más de un tema.

## Regenerar los PDF de los temas

```
pip install reportlab
python3 "Temas 2do cuatrimestre/_generador/generar_pdfs.py"        # todos
python3 "Temas 2do cuatrimestre/_generador/generar_pdfs.py" 7      # solo el tema 7
```

El generador verifica que **ninguna línea de código quede sin explicar**.

## Importante: `javax` (Tomcat 9) vs `jakarta` (Tomcat 10)

El código de las clases usa `javax.servlet` (Servlet 4.0, **Tomcat 9**). La solución de `2° Parcial modelo` está escrita con `jakarta.servlet` (Servlet 6.0, **Tomcat 10.1**). Un WAR `jakarta` **no corre en Tomcat 9** (y viceversa). Ver el README del parcial.
