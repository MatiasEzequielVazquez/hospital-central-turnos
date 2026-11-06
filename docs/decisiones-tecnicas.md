# Decisiones técnicas

Registro de las decisiones de diseño tomadas durante el refactoring, con su justificación y sus consecuencias (formato ADR simplificado).

## ADR-01 · Red de seguridad antes de refactorizar
- **Contexto:** la v1 no tenía pruebas y su lógica estaba mezclada con la consola, por lo que no admitía pruebas unitarias.
- **Decisión:** escribir primero pruebas de **caracterización** (Golden Master) que ejecutan el programa completo con entradas guionadas y comparan la salida contra la registrada en `estado-inicial`.
- **Consecuencia:** cada refactoring se validó con `mvn test` sin depender del diseño interno. Las pruebas unitarias se agregaron después, cuando el diseño ya las permitía.

## ADR-02 · Paquetes por funcionalidad
- **Decisión:** `dominio` (entidades y reglas), `repositorio` (acceso a datos), `turnos` y `reportes` (casos de uso + consola de cada módulo).
- **Motivo:** cada integrante trabaja en su propio paquete; los cambios en paralelo no se pisan y cada módulo puede evolucionar por separado.

## ADR-03 · Repositorios con interfaces chicas e inyección por constructor (DIP, ISP)
- **Decisión:** reemplazar la clase `Datos` (listas `public static`) por `PacienteRepositorio`, `ProfesionalRepositorio` y `TurnoRepositorio`, con implementaciones en memoria. `Main` es la única clase que conoce las implementaciones concretas (*composition root*).
- **Consecuencia:** se puede agregar persistencia en archivo o base de datos implementando las interfaces, sin tocar servicios ni pruebas. Cada servicio depende sólo de los métodos que usa.

## ADR-04 · Modelo de dominio con comportamiento (Move Method)
- **Decisión:** las reglas "no se cancela un turno atendido" y "no se atiende uno cancelado" pasan de `TurnoManager` a `Turno.cancelar()` y `Turno.atender()`. Los campos pasan a ser privados.
- **Consecuencia:** ninguna clase puede dejar un turno en un estado inválido.

## ADR-05 · Excepción de dominio en lugar de códigos de error
- **Decisión:** `TurnoInvalidoException` (no chequeada) con el mensaje de negocio. La consola es la única que la convierte en `ERROR: <mensaje>`.
- **Alternativa descartada:** devolver un objeto `Resultado` con éxito/error; agregaba complejidad sin beneficio para este alcance.

## ADR-06 · Reloj inyectado
- **Decisión:** `ValidadorTurno` recibe un `java.time.Clock`. En producción se usa `Clock.systemDefaultZone()`; en pruebas, `Clock.fixed(...)`.
- **Consecuencia:** las pruebas de fechas son deterministas (smell S08).

## ADR-07 · Enums con datos en lugar de condicionales (OCP)
- **Decisión:** `ObraSocial` conoce su porcentaje de coseguro y `Especialidad` su arancel base. `CalculadoraArancel` ya no tiene `if`/`switch` por tipo.
- **Consecuencia:** agregar una obra social es agregar una constante al enum; no se modifica ningún algoritmo.
- **Alternativa descartada:** una jerarquía de clases *Strategy* por obra social. Hoy todas las coberturas son un porcentaje, así que una clase por obra social sería sobrediseño; se migraría a *Strategy* si aparece una regla distinta (por ejemplo, topes o copagos fijos).

## ADR-08 · Montos con `BigDecimal`
- **Decisión:** reemplazar `double` por `BigDecimal` con escala 2 y redondeo `HALF_UP`.
- **Consecuencia:** desaparece el error binario (`15000 * 1.2 = 18000.000000000004`), comprobado por la prueba UT-21.

## ADR-09 · Comportamiento externo idéntico
- **Decisión:** conservar exactamente los textos de la v1, incluso imperfectos (`turno(s)`), y la semántica de los filtros como texto.
- **Motivo:** un refactoring no debe cambiar el comportamiento visible; las mejoras funcionales se harían en cambios separados y con su propia prueba.

## Aplicación de SOLID

| Principio | Dónde se aplicó | Evidencia |
|---|---|---|
| **SRP** | Consola (`TurnosConsola`, `ReportesConsola`), casos de uso (`TurnoService`, `BuscadorTurnos`, `ReporteService`), reglas (`ValidadorTurno`, `CalculadoraArancel`), datos (repositorios) | 0 `System.out` fuera de las clases de consola (antes: 30) |
| **OCP** | `ObraSocial` y `Especialidad` con datos; `FiltroTurnos` encapsula los criterios | Agregar una obra social no modifica `CalculadoraArancel` |
| **LSP** | Cualquier implementación de `TurnoRepositorio` debe respetar el contrato: ids correlativos y orden de inserción en `listarTodos()` | Servicios y pruebas usan las interfaces; la implementación en memoria es intercambiable |
| **ISP** | Tres interfaces de repositorio pequeñas en lugar de una "base de datos" general | `ValidadorTurno` depende sólo de `TurnoRepositorio` |
| **DIP** | Servicios reciben repositorios, validador, calculadora y reloj por constructor | `Main` arma el grafo de objetos; ninguna clase de negocio hace `new` de una dependencia de infraestructura |

## Aplicación de Clean Code

| Aspecto | Antes | Después |
|---|---|---|
| Nombres | `mat`, `urg`, `x`, `p`, `pr`, `lista2`, `ok`, `pac`, `os`, `cos` | `matriculaProfesional`, `urgente`, `turno`, `paciente`, `encontrados`, `aCargoPaciente`, `aCargoObraSocial` |
| Funciones | Método más largo de negocio: 72 líneas (`agregarTurno`) | Máximo 12 líneas por método de negocio; `registrar()` lee como una lista de pasos |
| Errores | `catch (Exception)` + `println` + `return false` | `DateTimeParseException` específica y `TurnoInvalidoException` con mensaje |
| Valores mágicos | 17 estados como texto, 5 números sueltos | 0: enums y constantes con nombre (`APERTURA`, `CIERRE`, `RECARGO_URGENCIA`) |
| Duplicación | 6 bucles de búsqueda idénticos | Búsquedas centralizadas en los repositorios |

## Métricas antes/después

| Métrica | v1 (`estado-inicial`) | v2 (`estado-refactorizado`) |
|---|---|---|
| Clases | 7 | 29 |
| Líneas de código de producción | 397 | 964 |
| Método de negocio más largo | 72 líneas | 12 líneas |
| `System.out` en clases de negocio | 30 | 0 |
| Estados como texto literal | 17 | 0 |
| Accesos a estado global | 24 | 0 |
| Pruebas automatizadas | 0 | 29 (4 de caracterización + 25 unitarias) |

El código creció en líneas porque ahora hay más clases, cada una pequeña y con una sola responsabilidad. Lo que importa es que bajó el **costo del cambio**: cada regla vive en un único lugar y tiene su prueba.

## Trazabilidad smell → refactoring → verificación

| Smell | Refactoring | Commit | Verificación |
|---|---|---|---|
| S03 | Replace Type Code with Enum (`EstadoTurno`) | `refactor(modelo): reemplaza estados de texto por enum EstadoTurno` | Caracterización |
| S03, S09 | Encapsulate Field, **Move Method** (reglas de estado a `Turno`) | `refactor(modelo): encapsula entidades, tipa fechas y mueve reglas de estado a Turno` | Caracterización + UT-01..05 |
| S04, S05 | Extract Interface + inyección de dependencias | `refactor(arquitectura): reemplaza estado global por repositorios inyectados (DIP)` | Caracterización |
| S02 | **Introduce Parameter Object** (`SolicitudTurno`) | `refactor(turnos): introduce parameter object SolicitudTurno` | Caracterización |
| S06, S07 | Extract Class, Rename Class, Replace Error Code with Exception | `refactor(turnos): separa consola de la logica y reemplaza codigos de error por excepciones` | Caracterización + UT-11..15 |
| S01, S08 | **Extract Method**, Extract Class (`ValidadorTurno`), reloj inyectado | `refactor(turnos): extrae validaciones de registro a ValidadorTurno con reloj inyectado` | Caracterización + UT-06..10 |
| S10 | **Introduce Parameter Object** (`FiltroTurnos`) + Move Method | `refactor(reportes): introduce parameter object FiltroTurnos y mueve el criterio de filtrado` | Caracterización + UT-16..19 |
| S12–S15, S17 | **Move Method** (`CalculadoraArancel`), Replace Conditional with Polymorphism, Replace Magic Number | `refactor(reportes): mueve el calculo de aranceles a CalculadoraArancel y enums de dominio` | Caracterización + UT-20..22 |
| S11, S16 | **Extract Method**, Extract Class (`ReporteService`, `ReportesConsola`) | `refactor(reportes): separa calculo y presentacion del reporte (Extract Method, SRP)` | Caracterización + UT-23..25 |

## Deuda remanente (mejoras futuras)

| Ítem | Propuesta |
|---|---|
| `Main.main()` sigue teniendo 58 líneas (menú + armado de objetos) | Extraer el menú a una clase propia y modelar cada opción con el patrón **Command** |
| Constructor de `Turno` con 7 parámetros | Patrón **Builder** o crear el turno a partir de `SolicitudTurno` ya validada |
| Filtros como texto en `FiltroTurnos` | Tipar estado y fechas, informando entradas inválidas (cambio funcional, con su prueba) |
| Persistencia en memoria | Implementar `TurnoRepositorio` sobre archivo o base de datos |
| Sin medición de cobertura | Agregar JaCoCo al pipeline de CI |
| `TurnoRegistrado` guarda `Turno`, `Paciente` y `Profesional` (observación del PR #5) | Evaluar que `Turno` mantenga las referencias y eliminar el record |
| `TurnoRepositorio.siguienteId()` (observación del PR #3) | Con una base de datos real, el id lo generaría la base; queda documentado en el contrato del repositorio |
