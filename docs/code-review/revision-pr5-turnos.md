# Revisión PR #5 · `refactor/turnos`

- **Autor:** Matias Ezequiel Vazquez · **Revisor:** Patricio Sussini Guanziroli
- **Commits revisados:** `SolicitudTurno`; separación de consola y excepciones; `ValidadorTurno` con reloj inyectado.

## Checklist aplicado

| Ítem | Resultado | Comentario |
|---|---|---|
| A1 Tests y CI | ✅ | |
| A2 `*.esperado` sin cambios | ✅ | |
| B1 SRP | ✅ | `TurnoService` sin `System.out`; consola aparte |
| B5 DIP | ✅ | `Clock` y `ValidadorTurno` inyectados |
| C2 Funciones cortas | ✅ | `registrar()` pasó de 72 a 8 líneas |
| C3 Sin duplicación ni valores mágicos | ❌ → ✅ | Ver observaciones 1 y 2 |
| C4 Manejo de errores | ✅ | `DateTimeParseException` específica |

## Observaciones

| # | Severidad | Ubicación | Observación | Acción | Estado |
|---|---|---|---|---|---|
| 1 | 🟠 Mayor | `TurnosConsola.cancelar()` / `atender()` | Las dos operaciones repiten la lectura del id y los dos `catch` (10 líneas casi idénticas) | Extraer `operarSobreTurno(IntFunction<String>)` e `informarError(String)` | Corregido en `fix(review): elimina duplicacion en TurnosConsola y nombra el horario de atencion` |
| 2 | 🟡 Menor | `ValidadorTurno.validarHorarioDeAtencion()` | `8` y `20` son números mágicos | Constantes `APERTURA` y `CIERRE` de tipo `LocalTime` | Corregido en el mismo commit |
| 3 | 💡 Sugerencia | `TurnoRegistrado` | Podría evitarse si `Turno` guardara referencias a `Paciente` y `Profesional` | Se mantiene: acoplaría las entidades entre sí | Registrado |

**Verificación de la corrección:** `mvn test` en verde después del commit de corrección; los `*.esperado` siguen sin cambios.

**Decisión:** ✅ Aprobado después de la corrección.
