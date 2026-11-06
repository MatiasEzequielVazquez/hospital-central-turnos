# Plan de pruebas y evidencias antes/después

> Responsable: Patricio Sussini Guanziroli · Revisión: Matias Ezequiel Vazquez

## 1. Estrategia

El refactoring se protegió con **dos niveles de prueba**:

| Nivel | Qué verifica | Cuándo se creó | Herramienta |
|---|---|---|---|
| **Caracterización (Golden Master)** | Que la salida completa de consola sea idéntica a la de la versión inicial | Etapa de diagnóstico, **antes** de cualquier refactoring | JUnit 5 + escenarios `.in` / `.esperado` |
| **Unitarias** | Que cada componente nuevo cumpla su regla de forma aislada | Etapa de pruebas, sobre el diseño ya refactorizado | JUnit 5, patrón AAA, reloj fijo (`Clock.fixed`) |

Las pruebas de caracterización son la red de seguridad: se ejecutaron en **cada commit** de refactoring (localmente y en GitHub Actions). Los archivos `*.esperado` se generaron ejecutando el tag `estado-inicial` y **nunca se modificaron**.

## 2. Pruebas de caracterización

| Escenario | Casos cubiertos | Operaciones |
|---|---|---|
| `turnos-registro` | Alta válida, paciente inexistente, profesional inexistente, fecha inválida, hora inválida, fecha pasada, fuera de horario, superposición, segundo pendiente no urgente, segundo pendiente urgente | 10 |
| `turnos-estados` | Cancelar pendiente / cancelado / atendido / inexistente / id no numérico; atender atendido / cancelado / inexistente / id no numérico; alta + atender + re-atender + cancelar atendido | 13 |
| `reportes-busqueda` | Sin filtros, por matrícula (minúsculas), por estado (minúsculas), desde, hasta, solo urgentes, estado + rango, matrícula + urgentes sin resultados, matrícula inexistente | 9 |
| `reportes-general` | Reporte inicial; reporte tras atender SWISS urgente (cardiología) y PARTICULAR (pediatría) y cancelar un turno | 7 |
| **Total** | | **39** |

## 3. Pruebas unitarias

| ID | Clase bajo prueba | Caso | Técnica |
|---|---|---|---|
| UT-01 | `Turno` | Un turno nuevo nace PENDIENTE | Estado inicial |
| UT-02 | `Turno` | Cancelar un pendiente lo deja CANCELADO e inactivo | Transición válida |
| UT-03 | `Turno` | No se puede cancelar un atendido | Transición inválida |
| UT-04 | `Turno` | No se puede atender un cancelado | Transición inválida |
| UT-05 | `Turno` | Un cancelado no ocupa el horario | Regla derivada |
| UT-06 | `ValidadorTurno` | Acepta hoy a las 19:59 | Valor límite superior válido |
| UT-07 | `ValidadorTurno` | Rechaza ayer | Valor límite de fecha |
| UT-08 | `ValidadorTurno` | Rechaza 07:59 y 20:00 | Valores límite inválidos |
| UT-09 | `ValidadorTurno` | Rechaza superposición del profesional | Partición de equivalencia |
| UT-10 | `ValidadorTurno` | Segundo pendiente sólo si es urgente | Tabla de decisión |
| UT-11 | `TurnoService` | Registrar guarda pendiente con id correlativo | Camino feliz |
| UT-12 | `TurnoService` | Paciente inexistente | Error de búsqueda |
| UT-13 | `TurnoService` | Fecha mal formada | Entrada inválida |
| UT-14 | `TurnoService` | Cancelar/atender turno inexistente | Error de búsqueda |
| UT-15 | `TurnoService` | Atender cambia el estado persistido | Integración con repositorio |
| UT-16 | `FiltroTurnos` | Filtro vacío acepta todo | Partición de equivalencia |
| UT-17 | `FiltroTurnos` | Estado sin distinguir mayúsculas | Partición de equivalencia |
| UT-18 | `FiltroTurnos` | Rango incluye ambos extremos | Valores límite |
| UT-19 | `FiltroTurnos` | Combina matrícula y urgencia | Combinación de criterios |
| UT-20 | `CalculadoraArancel` | OSDE paga 90 % de clínica | Partición por obra social |
| UT-21 | `CalculadoraArancel` | Recargo de urgencia sin error de redondeo (18000.00) | Regresión del smell S17 |
| UT-22 | `CalculadoraArancel` | PAMI cubre todo, PARTICULAR paga todo | Valores extremos de cobertura |
| UT-23 | `ReporteService` | Sin turnos, reporte en cero | Caso vacío |
| UT-24 | `ReporteService` | Conteo por estado y carga sin cancelados | Partición por estado |
| UT-25 | `ReporteService` | Sólo se facturan atendidos | Regla de negocio |

## 4. Evidencias antes/después

### 4.1 Pruebas por hito

| Tag | Código | Pruebas ejecutadas | Resultado |
|---|---|---|---|
| `estado-inicial` | v1 | — (no había pruebas) | — |
| `etapa-diagnostico` | v1 + red de seguridad | 4 de caracterización | ✅ 4/4 |
| `etapa-refactoring` | v2 | 4 de caracterización (las mismas, sin cambios) | ✅ 4/4 |
| `etapa-pruebas` | v2 | 4 de caracterización + 25 unitarias | ✅ 29/29 |

### 4.2 Comparación directa de ejecuciones

El script `scripts/comparar-versiones.sh` compila ambas versiones en carpetas temporales (`git worktree`), las ejecuta con las mismas entradas y compara las salidas:

```text
$ ./scripts/comparar-versiones.sh estado-inicial HEAD
Comparando estado-inicial  vs  HEAD
  IGUAL     reportes-busqueda (101 lineas de salida)
  IGUAL     reportes-general (96 lineas de salida)
  IGUAL     turnos-estados (126 lineas de salida)
  IGUAL     turnos-registro (99 lineas de salida)
Resultado: el comportamiento externo es identico en todos los escenarios.
```

### 4.3 Cómo reproducir

```bash
mvn test                                   # todas las pruebas
git checkout etapa-diagnostico && mvn test # la red de seguridad pasa sobre la v1
git checkout main
./scripts/comparar-versiones.sh            # comparación v1 vs versión actual
```

## 5. Limitaciones

- No se mide cobertura con herramienta (JaCoCo); se priorizó cubrir todas las ramas de las reglas de negocio.
- La interfaz de consola se prueba sólo de forma indirecta, a través de los escenarios de caracterización.
- Las fechas de los escenarios son futuras (2028); el validador de la v1 usa `LocalDate.now()`, por lo que la red de seguridad debe actualizarse si se ejecuta después de esa fecha.
