# 01 · Calidad externa vs. calidad interna (versión inicial)

> Tag analizado: `estado-inicial` · Autor del análisis: Matias Ezequiel Vazquez

## 1. Calidad externa — lo que percibe el usuario

Desde la consola, el sistema **cumple su función**: los cinco casos de uso responden correctamente para entradas válidas y rechazan las inválidas con un mensaje.

| Caso de uso | Resultado observado | Evidencia |
|---|---|---|
| CU1 Registrar turno | Alta correcta; rechaza paciente/profesional inexistente, fecha u hora inválida, fecha pasada, fuera de horario (08–20), superposición del profesional y segundo turno pendiente no urgente | `escenarios/turnos-registro` |
| CU2 Cancelar turno | Cancela pendientes; rechaza atendidos, ya cancelados, id inexistente o no numérico | `escenarios/turnos-estados` |
| CU3 Atender turno | Marca atendidos; rechaza cancelados y ya atendidos | `escenarios/turnos-estados` |
| CU4 Buscar turnos | Filtra por matrícula, estado, rango de fechas y urgencia (combinables) | `escenarios/reportes-busqueda` |
| CU5 Reporte general | Conteo por estado, turnos activos por profesional y facturación estimada por obra social | `escenarios/reportes-general` |

Defectos externos menores detectados (no bloquean el uso):

- Los datos se pierden al cerrar el programa (persistencia sólo en memoria; aceptado para el alcance del TPI).
- La búsqueda acepta fechas mal formadas sin avisar, porque compara las fechas como texto.
- Mensaje gramaticalmente incorrecto: `Se encontraron 1 turno(s)`.

## 2. Calidad interna — lo que ve el equipo técnico

La estructura interna muestra el patrón típico de un prototipo que creció rápido: **funciona, pero cuesta cambiarlo**.

| Indicador (v1) | Valor | Lectura |
|---|---|---|
| Método más largo | `TurnoManager.agregarTurno()` · 72 líneas · 8 responsabilidades | Difícil de leer, probar y modificar |
| `System.out` en clases de negocio | 16 en `TurnoManager`, 14 en `ReporteManager` | Lógica acoplada a la consola |
| Estados como texto literal | 17 apariciones de `"PENDIENTE"`, `"ATENDIDO"`, `"CANCELADO"` | Un error de tipeo compila y falla en ejecución |
| Accesos a estado global `Datos.` | 24 | Todas las clases dependen de listas estáticas públicas |
| Bucles de búsqueda lineal repetidos | 16 bucles `for` en 2 clases, 6 de ellos para buscar por clave | Duplicación de lógica |
| Pruebas automatizadas | 0 | Ningún cambio es verificable de forma segura |

## 3. Relación entre ambas miradas

| Atributo | Situación actual | Consecuencia para la evolución |
|---|---|---|
| Funcionalidad visible | Correcta | El usuario no percibe problemas **hoy** |
| Mantenibilidad | Baja | Agregar una obra social o especialidad obliga a editar condicionales en métodos largos |
| Legibilidad | Baja | Nombres como `mat`, `urg`, `x`, `lista2`, `pac`, `os` ocultan la intención |
| Testeabilidad | Muy baja | Estado global + `println` + `LocalDate.now()` impiden pruebas unitarias aisladas |
| Evolución futura | Riesgosa | Pasar a base de datos o a interfaz web exigiría reescribir los managers, porque mezclan negocio, datos y presentación |

**Conclusión:** la calidad externa es aceptable, pero la interna genera **deuda técnica** que encarece cada cambio. Antes de tocar el diseño se construye una red de seguridad (pruebas de caracterización) que fija el comportamiento externo actual.
