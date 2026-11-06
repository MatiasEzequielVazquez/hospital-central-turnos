# Revisión PR #4 · `refactor/reportes`

- **Autor:** Patricio Sussini Guanziroli · **Revisor:** Matias Ezequiel Vazquez
- **Commits revisados:** `FiltroTurnos`; `CalculadoraArancel` y enums de dominio; separación de `ReporteService` y `ReportesConsola`.

## Checklist aplicado

| Ítem | Resultado | Comentario |
|---|---|---|
| A1 Tests y CI | ✅ | |
| A2 `*.esperado` sin cambios | ✅ | Montos idénticos con `BigDecimal` |
| B1 SRP | ✅ | Cálculo (`ReporteService`) separado de presentación (`ReportesConsola`) |
| B2 OCP | ✅ | Sin `switch` por obra social ni `if` por especialidad |
| C3 Sin duplicación ni valores mágicos | ❌ → ✅ | Ver observaciones 1 y 2 |
| D3 Rama actualizada con `main` | ❌ → ✅ | Ver observación 4 |

## Observaciones

| # | Severidad | Ubicación | Observación | Acción | Estado |
|---|---|---|---|---|---|
| 1 | 🟠 Mayor | `ReportesConsola.imprimirFacturacion()` | `"$" + String.format("%.2f", ...)` se repite tres veces | Extraer `formatearMonto(BigDecimal)` | Corregido en `fix(review): extrae el formato de montos y nombra el recargo de urgencia` |
| 2 | 🟡 Menor | `CalculadoraArancel.calcular()` | `new BigDecimal("1.20")` es un número mágico | Constante `RECARGO_URGENCIA` documentada | Corregido en el mismo commit |
| 3 | 🟡 Menor | `FiltroTurnos` | Estado y fechas siguen siendo `String` (Primitive Obsession residual) | Tiparlos cambiaría el comportamiento ante entradas mal formadas; queda como deuda aceptada en `04-deuda-tecnica-priorizada.md` | Justificado |
| 4 | 🟡 Menor | Rama | Después del merge del PR #5 la rama quedó desactualizada | Merge de `main` en la rama; `Main.java` se combinó sin conflictos y `mvn test` quedó en verde | Corregido |

**Decisión:** ✅ Aprobado después de las correcciones.
