# Revisión PR #3 · `refactor/modelo`

- **Autor:** Matias Ezequiel Vazquez · **Revisor:** Patricio Sussini Guanziroli
- **Commits revisados:** enum `EstadoTurno`; encapsulamiento de entidades y Move Method a `Turno`; repositorios inyectados.

## Checklist aplicado

| Ítem | Resultado | Comentario |
|---|---|---|
| A1 Tests y CI | ✅ | 4/4 caracterización en verde en los 3 commits |
| A2 `*.esperado` sin cambios | ✅ | |
| B1 SRP | ✅ | `Datos` eliminado; acceso a datos en repositorios |
| B4 ISP | ✅ | Tres interfaces chicas |
| B5 DIP | ✅ | Managers reciben repositorios por constructor |
| C1 Nombres | ✅ | `dni`/`mat`/`urg` → `dniPaciente`/`matriculaProfesional`/`urgente` |
| C3 Valores mágicos | ✅ | 0 estados como texto |
| D1 Un refactoring por commit | ✅ | |

## Observaciones

| # | Severidad | Ubicación | Observación | Respuesta del autor | Estado |
|---|---|---|---|---|---|
| 1 | 🟡 Menor | `Turno` (constructor) | El constructor recibe 7 parámetros: es una Long Parameter List | `Turno` es una entidad inmutable y todos los datos son obligatorios; agregar un Builder ahora no reduce riesgo. Se registra como mejora futura | Justificado |
| 2 | 💡 Sugerencia | `TurnoRepositorio.siguienteId()` | Con una base de datos real el id lo generaría la base | De acuerdo; queda documentado en el contrato del repositorio | Registrado |

**Decisión:** ✅ Aprobado.
