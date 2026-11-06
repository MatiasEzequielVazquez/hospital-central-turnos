# Hospital Central · Gestión de Turnos

Trabajo Práctico Integrador de **Metodología de Sistemas II** (Tecnicatura Universitaria en Programación a Distancia).

Sistema de consola para gestionar los turnos médicos del Hospital Central. El repositorio contiene la **versión inicial** (con deuda técnica real) y la **versión refactorizada**, junto con el diagnóstico, las pruebas que demuestran que el comportamiento externo no cambió y el registro del code review.

## Funcionalidades

| # | Caso de uso | Reglas principales |
|---|---|---|
| 1 | Registrar turno | Paciente y profesional existentes, fecha no pasada, horario 08:00–20:00, sin superposición del profesional, un solo turno pendiente por paciente salvo urgencia |
| 2 | Cancelar turno | No se cancela un turno atendido ni uno ya cancelado |
| 3 | Marcar turno como atendido | No se atiende un turno cancelado ni uno ya atendido |
| 4 | Buscar turnos | Filtros combinables por matrícula, estado, rango de fechas y urgencia |
| 5 | Reporte general | Turnos por estado, carga por profesional y facturación estimada (arancel por especialidad, recargo por urgencia, coseguro por obra social) |

Los datos son **sintéticos** y se cargan en memoria al iniciar.

## Tecnologías

- Java 17 · Maven · JUnit 5
- GitHub Actions (compilación y pruebas en cada push y Pull Request)
- Git con flujo de ramas por funcionalidad y Pull Requests con revisión cruzada

## Instalación y uso

Requisitos: JDK 17 o superior y Maven 3.8 o superior.

```bash
git clone https://github.com/MatiasEzequielVazquez/hospital-central-turnos.git
cd hospital-central-turnos
mvn package
java -cp target/classes hospital.Main
```

Ejemplo de sesión:

```text
=== HOSPITAL CENTRAL - TURNOS ===
1) Registrar turno
...
Opcion: 1
DNI paciente: 35123456
Matricula profesional: MN1001
Fecha (AAAA-MM-DD): 2028-03-06
Hora (HH:MM): 09:30
Urgente (s/n): n
Motivo: Dolor de cabeza
Turno #4 registrado para Diego Sosa con Dra. Laura Perez el 2028-03-06 09:30
```

## Pruebas

```bash
mvn test                          # 4 de caracterización + 25 unitarias
./scripts/comparar-versiones.sh   # compara la salida de estado-inicial con la versión actual
```

## Estructura

```text
src/main/java/hospital/
├── Main.java              # menú y armado de dependencias (composition root)
├── dominio/               # Turno, Paciente, Profesional, EstadoTurno, ObraSocial, Especialidad
├── repositorio/           # interfaces de repositorio, implementaciones en memoria, datos iniciales
├── turnos/                # TurnoService, ValidadorTurno, SolicitudTurno, TurnosConsola
└── reportes/              # BuscadorTurnos, ReporteService, CalculadoraArancel, FiltroTurnos, ReportesConsola
src/test/
├── java/hospital/         # pruebas de caracterización y unitarias
└── resources/escenarios/  # entradas (.in) y salidas registradas de la v1 (.esperado)
docs/
├── diagnostico/           # calidad externa/interna, code smells, deuda técnica priorizada
├── code-review/           # checklist y registro de cada revisión
├── pruebas/               # plan de pruebas y evidencias antes/después
├── uml/                   # diagramas de clases antes y después (.drawio + .png)
└── decisiones-tecnicas.md
```

## Hitos (tags)

| Tag | Contenido |
|---|---|
| `estado-inicial` | Sistema base v1.0.0, sin pruebas |
| `etapa-diagnostico` | Red de seguridad (caracterización), CI, checklist y diagnóstico |
| `etapa-refactoring` | Refactorings del modelo, del módulo de turnos y del de reportes |
| `etapa-pruebas` | Pruebas unitarias y evidencias antes/después |
| `etapa-docs` | README, decisiones técnicas, registros de code review y UML |
| `estado-refactorizado` | Versión final v2.0.0 |

Para ver una versión: `git checkout estado-inicial` (volver con `git checkout main`).

## Flujo de trabajo

Cada cambio se hizo en una rama (`diagnostico/*`, `refactor/*`, `test/*`, `docs/*`), se integró con un Pull Request revisado por el otro integrante con el [checklist](docs/code-review/checklist.md) y se mergeó sólo con CI en verde. Mensajes de commit en formato *Conventional Commits*.

## Integrantes

| Integrante | Módulo | Responsabilidades |
|---|---|---|
| Matias Ezequiel Vazquez | Turnos (CU 1–3) | Modelo de dominio, repositorios, `TurnoService`, `ValidadorTurno`, CI, README y decisiones técnicas; revisor del módulo de reportes |
| Patricio Sussini Guanziroli | Búsqueda y reportes (CU 4–5) | `FiltroTurnos`, `CalculadoraArancel`, `ReporteService`, plan de pruebas, UML; revisor del módulo de turnos |

## Uso de IA

La declaración de uso de herramientas de IA (herramienta, finalidad y grado de intervención) se encuentra en el informe del trabajo.
