# 04 · Deuda técnica priorizada y plan de refactoring

Criterio de prioridad: **impacto en el costo del cambio × probabilidad de que esa zona cambie**. Se atacan primero los smells que bloquean a otros (por ejemplo, S05 y S03 antes que los refactorings de cada módulo).

| Orden | Smells | Acción planificada | Técnica | Responsable | Rama |
|---|---|---|---|---|---|
| 1 | S03 | Reemplazar estados de texto por `enum EstadoTurno` | Replace Type Code with Enum | Matias | `refactor/modelo` |
| 2 | S03, S09 | Encapsular entidades, tipar fechas y mover reglas de estado a `Turno` | Encapsulate Field · **Move Method** | Matias | `refactor/modelo` |
| 3 | S04, S05 | Reemplazar `Datos` por repositorios inyectados por constructor | Extract Interface · inyección de dependencias (DIP) | Matias | `refactor/modelo` |
| 4 | S02 | Agrupar los datos de alta en `SolicitudTurno` | **Introduce Parameter Object** | Matias | `refactor/turnos` |
| 5 | S01, S08 | Extraer validaciones a `ValidadorTurno` con reloj inyectado | **Extract Method** · Extract Class | Matias | `refactor/turnos` |
| 6 | S06, S07 | Separar consola y lógica; errores con excepción de dominio | Extract Class · Rename · Replace Error Code with Exception | Matias | `refactor/turnos` |
| 7 | S10 | Agrupar filtros en `FiltroTurnos` y mover el criterio de filtrado al objeto | **Introduce Parameter Object** · Move Method | Patricio | `refactor/reportes` |
| 8 | S12, S13, S14, S15, S17 | Mover cálculo de aranceles a `CalculadoraArancel` y enums `ObraSocial`/`Especialidad`; montos en `BigDecimal` | **Move Method** · Replace Conditional with Polymorphism · Replace Magic Number | Patricio | `refactor/reportes` |
| 9 | S11, S16 | Separar cálculo (`ReporteService`) de presentación (`ReportesConsola`) | **Extract Method** · Extract Class | Patricio | `refactor/reportes` |

## Deuda que se acepta conscientemente (fuera de alcance)

| Ítem | Motivo |
|---|---|
| Persistencia sólo en memoria | El TPI evalúa diseño interno; el repositorio queda preparado (interfaces) para agregar archivo o base de datos |
| Filtros de búsqueda como texto | Cambiarlos a tipos fuertes modificaría el comportamiento visible ante entradas mal formadas; se registra como mejora futura |
| Mensaje `turno(s)` | Es comportamiento externo fijado por las pruebas de caracterización; se corregiría en una funcionalidad aparte |
