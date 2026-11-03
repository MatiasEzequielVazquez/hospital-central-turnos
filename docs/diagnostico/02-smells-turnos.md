# 02 · Code smells — módulo de turnos

> Alcance: `Turno`, `Datos`, `TurnoManager`, `Main` (casos de uso 1–3) · Responsable: Matias Ezequiel Vazquez
> Ubicaciones referidas al tag `estado-inicial`.

| ID | Smell / debilidad | Ubicación | Evidencia |
|---|---|---|---|
| S01 | Long Method | `TurnoManager.agregarTurno()` L9–80 | 72 líneas que buscan paciente, buscan profesional, parsean fecha/hora, validan fecha, horario, superposición y turno pendiente, crean el turno, lo guardan e imprimen |
| S02 | Long Parameter List | `TurnoManager.agregarTurno(String, String, String, String, boolean, String)` L9 | Seis parámetros, cuatro `String` consecutivos: invertir `fecha` y `hora` compila sin error |
| S03 | Primitive Obsession | `Turno.estado`, `Turno.fecha`, `Turno.hora` L7–9; comparaciones en `TurnoManager` L52, L60, L93–101, L116–124 | Estados como texto literal (17 apariciones en el sistema) y fechas como `String` |
| S04 | Duplicated Code | Búsqueda por id en `cancelar()` L83–88 y `atender()` L106–111; búsqueda de paciente/profesional L11–27 (repetida en `ReporteManager`) | El mismo bucle aparece 6 veces en el sistema |
| S05 | Estado global mutable | `Datos` L8–11 (`public static` listas y contador) | 24 accesos directos desde 3 clases; cualquier clase puede modificar cualquier dato |
| S06 | Responsabilidades mezcladas (viola SRP) | 16 `System.out.println` dentro de `TurnoManager` | La regla de negocio no puede reutilizarse ni probarse sin capturar la consola |
| S07 | Manejo de errores deficiente | `catch (Exception e)` L37; errores informados por `println` + `return false` | Se captura cualquier excepción y quien llama no sabe por qué falló |
| S08 | Dependencia oculta del reloj | `LocalDate.now()` L41 | El resultado depende del día en que se ejecuta: prueba no determinista |
| S09 | Clase de datos anémica y nombres crípticos | `Turno` (campos públicos `dni`, `mat`, `urg`); variables `p`, `pr`, `f`, `h`, `x` | Las reglas de transición de estado viven en `TurnoManager`, no en `Turno` |

## Impacto, riesgo y prioridad

| ID | Impacto en mantenimiento | Riesgo si no se corrige | Prioridad |
|---|---|---|---|
| S01 | Cada nueva validación agranda el método y se mezcla con las existentes | Alto: regresiones al tocar una validación | **Alta** |
| S02 | Llamadas difíciles de leer, errores de orden de argumentos | Medio | **Alta** (habilita S01) |
| S03 | Errores de tipeo silenciosos; reglas de fecha/hora sobre texto | Alto | **Alta** |
| S04 | Un cambio en la búsqueda debe replicarse en 6 lugares | Medio | Media |
| S05 | Acoplamiento total a la "base" en memoria; imposible cambiar de almacenamiento | Alto | **Alta** |
| S06 | Imposible reutilizar la lógica en otra interfaz (web, API) | Alto | **Alta** |
| S07 | Fallos ocultos; diagnóstico difícil | Medio | Media |
| S08 | Pruebas que fallan según la fecha | Medio | Media |
| S09 | Reglas de dominio dispersas; cualquier clase puede dejar un turno en estado inválido | Medio | Media |
