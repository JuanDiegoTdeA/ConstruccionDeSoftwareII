# EVALUACIÓN - ConstruccionDeSoftwareII

## Información General
- Estudiante(s): Juan Diego Millan Cano, Maria Jose Lopera Velasquez
- Rama evaluada: main
- Fecha de evaluación: 2026-03-23

## Tabla de Calificación

| # | Criterio | Peso | Puntaje (1-5) | Nota ponderada |
|---|---|---|---|---|
| 1 | Modelado de dominio | 25% | 3 | 0.75 |
| 2 | Relaciones entre entidades | 15% | 2 | 0.30 |
| 3 | Uso de Enums | 15% | 1 | 0.15 |
| 4 | Manejo de estados | 5% | 1 | 0.05 |
| 5 | Tipos de datos | 5% | 4 | 0.20 |
| 6 | Separación Usuario vs Cliente | 10% | 1 | 0.10 |
| 7 | Bitácora | 5% | 4 | 0.20 |
| 8 | Reglas básicas de negocio | 5% | 1 | 0.05 |
| 9 | Estructura del proyecto | 10% | 1 | 0.10 |
| 10 | Repositorio | 10% | 2 | 0.20 |
| **TOTAL** | | **100%** | | **2.10** |

## Penalizaciones
- Ninguna

## Bonus
- Ninguno

## Nota Final: 2.1 / 5.0

---

## Análisis por Criterio

### 1. Modelado de dominio (Puntaje: 3)
Las entidades del dominio están identificadas: `User` (abstracta), `IndividualClient`, `BusinessClient`, `BankAccount`, `Loan`, `Transfer`, `BankingProduct`, `AuditLogEntry` + clases de detalle. Sin embargo, la falla conceptual grave es que **`IndividualClient` y `BusinessClient` extienden directamente `User`** — esto mezcla el concepto de usuario del sistema con el concepto de cliente. No existe separación entre acceso al sistema y titular de productos bancarios.

### 2. Relaciones entre entidades (Puntaje: 2)
Las relaciones se expresan mediante FK como Strings (ej. `holderID`, `applicantClientID`, `disbursementTargetAccount`, `createdByUserID`). No hay referencias directas entre objetos Java. La intención es correcta (se documentan los FK en comentarios) pero el modelo OO no es navegable.

### 3. Uso de Enums (Puntaje: 1)
**⚠️ CRÍTICO:** No existe ningún enum en todo el proyecto. Todos los catálogos y estados son `String`:
- `systemRole` y `userStatus` en `User` → deben ser enums.
- `accountType`, `currency`, `accountStatus` en `BankAccount` → deben ser enums.
- `loanType`, `loanStatus` en `Loan` → deben ser enums.
- `transferStatus` en `Transfer` → debe ser enum.
- `category` en `BankingProduct` → debe ser enum.

### 4. Manejo de estados (Puntaje: 1)
Sin enums y sin métodos de transición de estado. No hay lógica de estado implementada.

### 5. Tipos de datos (Puntaje: 4)
`BigDecimal` correctamente usado en `BankAccount` (`currentBalance`), `Loan` (`requestedAmount`, `approvedAmount`, `interestRate`) y `Transfer` (`amount`). `LocalDate`/`LocalDateTime` usados apropiadamente para fechas. Excelente uso de tipos matemáticos precisos.

### 6. Separación Usuario vs Cliente (Puntaje: 1)
`IndividualClient extends User` y `BusinessClient extends User`. El `User` abstracto representa tanto usuarios del sistema como clientes del banco — conceptos fundamentalmente distintos. Esta confusión es el error conceptual más grave del proyecto.

### 7. Bitácora (Puntaje: 4)
`AuditLogEntry` tiene `Object detailData` que admite polimorfismo. Las clases `TransferDetail`, `LoanDetail`, `ExpirationDetail` especializan los datos de detalle — diseño correcto para una bitácora flexible. La documentación Javadoc explica el propósito de cada clase.

### 8. Reglas básicas de negocio (Puntaje: 1)
No hay validaciones en constructores ni métodos de negocio. Los constructores simplemente asignan parámetros sin verificar precondiciones.

### 9. Estructura del proyecto (Puntaje: 1)
No es un proyecto Maven/Spring Boot. Los archivos están en `src/domian/` (typo: "domian" en lugar de "domain"). El nombre de paquete incluye la ruta completa: `ConstruccionDeSoftwareII.src.domian.client` — esto no sigue la convención Java de paquetes. No hay `pom.xml`.

### 10. Repositorio (Puntaje: 2)
- **Nombre:** `ConstruccionDeSoftwareII` — descriptivo.
- **README:** Solo tiene los nombres de los integrantes. Sin materia, tecnología ni instrucciones.
- **Commits:** 4 commits con mensajes razonablemente descriptivos.
- **Ramas:** Solo `main`, sin `develop`.
- **Tag:** Ninguno.

---

## Fortalezas
- Uso correcto de `BigDecimal` para campos monetarios.
- Bitácora con estructura polimórfica (`AuditLogEntry` + clases de detalle) — bien diseñada.
- Todas las entidades del dominio identificadas.
- `LocalDateTime` para fechas de operaciones — preciso.

## Oportunidades de mejora
- **Crítico:** Crear enums para todos los estados y catálogos. Ningún String debe usarse para representar estados o tipos.
- **Crítico:** Separar `User` (acceso al sistema) de `Cliente` (titular de productos). `IndividualClient` y `BusinessClient` no deben extender `User`.
- Crear proyecto Maven con estructura `src/main/java/` y corregir typo en paquete ("domian" → "domain").
- Agregar referencias de objeto entre entidades en lugar de FK Strings.
- Agregar validaciones de negocio en constructores.
- Completar el README con materia, tecnología y descripción del proyecto.
- Crear rama `develop` y tag de entrega.
