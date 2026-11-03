# 03 · Code smells — módulo de búsqueda y reportes

> Alcance: `ReporteManager`, `Paciente`, `Profesional`, `Main` (casos de uso 4–5) · Responsable: Patricio Sussini Guanziroli
> Ubicaciones referidas al tag `estado-inicial`.

| ID | Smell / debilidad | Ubicación | Evidencia |
|---|---|---|---|
| S10 | Long Parameter List con valores "comodín" | `ReporteManager.buscar(String, String, String, String, boolean)` L9 | Cinco parámetros donde `null` o `""` significan "sin filtro" |
| S11 | Long Method / baja cohesión | `ReporteManager.reporte()` L39–98 | 60 líneas que cuentan estados, calculan carga por profesional, facturan e imprimen |
| S12 | Condicionales sobre código de tipo (viola OCP) | `if/else` de especialidad L72–74; `switch` de obra social L77–88 | Agregar una obra social o especialidad obliga a modificar el método |
| S13 | Magic numbers | `10000`, `15000`, `12000` L72–74; `1.2` L75; `0.1` L79 y L82 | Aranceles y porcentajes sin nombre ni lugar único de definición |
| S14 | Feature Envy | Cálculo de arancel y coseguro L63–92 | El reporte calcula con datos de `Profesional` y `Paciente`: es conocimiento del dominio |
| S15 | Duplicated Code | Ramas `OSDE` y `SWISS` idénticas L78–83; búsqueda de nombres L27–34 y L64–71 | Lógica repetida dentro del mismo módulo y con `TurnoManager` |
| S16 | Nombres poco expresivos | `lista2`, `ok`, `nom`, `nomPr`, `pac`, `os`, `cos`, `x`, `soloUrg` | Hay que leer la implementación para entender cada variable |
| S17 | Dinero como `double` | `total`, `pac`, `os`, `precio` L62–90 | Errores de redondeo binario (`15000 * 1.2 = 18000.000000000004`) |

## Impacto, riesgo y prioridad

| ID | Impacto en mantenimiento | Riesgo si no se corrige | Prioridad |
|---|---|---|---|
| S10 | Llamadas ilegibles; agregar un filtro cambia la firma y todos los llamadores | Medio | **Alta** |
| S11 | Cualquier cambio en el reporte toca un método de 60 líneas | Alto | **Alta** |
| S12 | Cada alta de obra social o especialidad modifica código existente | Alto | **Alta** |
| S13 | Un cambio de arancel exige buscar números sueltos | Medio | Media |
| S14 | La regla de facturación no se puede reutilizar fuera del reporte | Alto | **Alta** |
| S15 | Correcciones que se aplican en un lugar y se olvidan en otro | Medio | Media |
| S16 | Mayor tiempo de lectura y revisión | Bajo | Baja |
| S17 | Diferencias de centavos en montos acumulados | Medio | Media |
