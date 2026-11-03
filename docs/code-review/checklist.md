# Checklist de code review

Se aplica en **cada Pull Request** (está copiado en `.github/pull_request_template.md`). El revisor es siempre el integrante que **no** escribió el código.

## Severidad de las observaciones

| Nivel | Significado | ¿Bloquea el merge? |
|---|---|---|
| 🔴 Bloqueante | Rompe comportamiento, tests en rojo, pérdida de datos | Sí |
| 🟠 Mayor | Viola un principio de diseño o deja duplicación relevante | Sí, hasta corregir o justificar |
| 🟡 Menor | Nombre mejorable, comentario innecesario, formato | No |
| 💡 Sugerencia | Idea para el futuro | No |

## Checklist

### A. Comportamiento y pruebas
- [ ] A1. `mvn test` pasa localmente y el pipeline de CI está en verde.
- [ ] A2. Los archivos `*.esperado` de caracterización **no** se modificaron (el comportamiento externo se conserva).
- [ ] A3. La lógica nueva o movida tiene pruebas unitarias (a partir de la etapa de pruebas).

### B. Diseño (SOLID)
- [ ] B1. **SRP:** cada clase tiene una única razón para cambiar (negocio, datos y consola separados).
- [ ] B2. **OCP:** agregar una variante (obra social, especialidad, estado) no obliga a modificar condicionales existentes.
- [ ] B3. **LSP:** las implementaciones de una interfaz pueden reemplazarse sin romper a quien la usa.
- [ ] B4. **ISP:** las interfaces son chicas y específicas; nadie depende de métodos que no usa.
- [ ] B5. **DIP:** las clases de negocio reciben sus dependencias por constructor y dependen de interfaces.

### C. Clean Code
- [ ] C1. Nombres que revelan intención (sin abreviaturas crípticas como `mat`, `urg`, `x`).
- [ ] C2. Funciones cortas que hacen una sola cosa, con un único nivel de abstracción.
- [ ] C3. Sin duplicación (DRY) ni números o textos mágicos.
- [ ] C4. Errores informados con excepciones con mensaje claro; sin `catch (Exception)` genérico ni errores silenciados.
- [ ] C5. Sin código muerto ni comentarios que expliquen lo que el código debería decir por sí mismo.

### D. Proceso y trazabilidad
- [ ] D1. Un refactoring por commit, con mensaje en formato *Conventional Commits* (`refactor(alcance): ...`).
- [ ] D2. La descripción del PR indica smells atacados (IDs `Sxx`), técnica aplicada y cómo se verificó.
- [ ] D3. La rama está actualizada con `main` y no tiene conflictos.

## Criterios de aceptación técnica (Definition of Done del PR)

Un PR se aprueba y se mergea sólo si:

1. El CI está en verde (compilación + todas las pruebas).
2. No quedan observaciones 🔴 ni 🟠 abiertas (corregidas con un commit `fix(review): ...` o justificadas por escrito en el PR).
3. Las salidas `*.esperado` siguen idénticas a las del tag `estado-inicial`.
4. El revisor dejó su aprobación (*Approve*) en GitHub.
