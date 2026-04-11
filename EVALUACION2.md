# EVALUACION 2 - ConstruccionDeSoftwareII

## Informacion general
- Estudiante(s): Maria Jose Lopez Velasquez, Juan Diego Millan Cano
- Rama evaluada: develop
- Commit evaluado: 95a3c03672c4399ccd2f5549adffdb644f67091f
- Fecha: 2026-04-11
- Nota: Proyecto Spring Boot con arquitectura hexagonal. Dominio en `Bank/src/main/java/app/domain/`. Puertos de salida en capa `application/ports/out/`. Implementacion de alto nivel con dominio rico.

---

## Tabla de calificacion

| # | Criterio | Peso | Puntaje (1-5) | Contribucion |
|---|----------|------|---------------|--------------|
| 1 | Modelado de dominio | 20% | 5 | 1.00 |
| 2 | Modelado de puertos | 20% | 4 | 0.80 |
| 3 | Modelado de servicios de dominio | 20% | 3 | 0.60 |
| 4 | Enums y estados | 10% | 5 | 0.50 |
| 5 | Reglas de negocio criticas | 10% | 5 | 0.50 |
| 6 | Bitacora y trazabilidad | 5% | 4 | 0.20 |
| 7 | Estructura interna de dominio | 10% | 4 | 0.40 |
| 8 | Calidad tecnica base en domain | 5% | 5 | 0.25 |
| | **Total base** | **100%** | | **4.25** |

**Formula:** Nota = sum(puntaje_i × peso_i) / 100

---

## Penalizaciones
Ninguna.

---

## Bonus

| Bonus | Valor | Justificacion |
|---|---|---|
| Excelente trazabilidad en bitacora | +0.1 | `RegisterAuditLogUseCase` y `AuditLogPort` correctamente definidos. `ExpirePendingTransfersUseCase` aborda la regla de 60 minutos. |
| **Total bonus** | **+0.1** | |

**Nota base:** 4.25
**Bonus:** +0.10
**Nota final:** 4.35

---

## Nota final
**4.35 / 5.0**

---

## Hallazgos

### Fortalezas
- **9 enums completos:** `AccountStatus`, `AccountType`, `CurrencyType`, `LoanStatus`, `LoanType`, `ProductCategory`, `SystemRole`, `TransferStatus`, `UserStatus`. Cobertura total del dominio.
- **Entidades de dominio ricas:** `BankAccount` con metodo factory `open()` y metodos `deposit()`, `withdraw()` con invariantes. `Loan` con factory method `request()` y maquina de estados `approve()`, `reject()`, `disburse()`. `Transfer` con ciclo completo de estados.
- **10 puertos de entrada (inbound):** `ApproveLoanUseCase`, `ApproveTransferUseCase`, `CreateTransferUseCase`, `DisburseLoanUseCase`, `ExpirePendingTransfersUseCase`, `OpenBankAccountUseCase`, `RegisterAuditLogUseCase`, `RejectLoanUseCase`, `RejectTransferUseCase`, `RequestLoanUseCase` — contratos atomicos de casos de uso.
- **7 puertos de salida semanticos:** `AuditLogPort`, `BankAccountRepositoryPort` (con Javadoc de invariantes), `ClientRepositoryPort`, `LoanRepositoryPort` (con `findByClientId`, `findByStatus`), `TransferRepositoryPort`, `UserRepositoryPort`, `BankingProductRepositoryPort`.
- **`ExpirePendingTransfersUseCase`** aborda explicitamente la regla de vencimiento a 60 minutos de las transferencias de alto monto.
- **`SharedExceptions`:** `BusinessException`, `InsufficientFundsException`, `InvalidStateTransitionException`, `ResourceNotFoundException`, `UnauthorizedOperationException` — jerarquia de excepciones de dominio robusta.
- **Codigo en ingles y BigDecimal** en todos los montos.

### Debilidades
- **Puertos en capa application, no domain:** Los puertos de salida estan en `application/ports/out`. En arquitectura hexagonal estricta deberian estar en la capa domain. Es un deslizamiento de capas menor pero perceptible.
- **Sin servicios de dominio en la capa domain:** La logica de orquestacion esta en `application/usecase`. El dominio contiene solo entidades y enums (modelos anémicos en orquestacion). Los use cases de aplicacion asumen el rol de servicios de dominio.
- **Infraestructura skeleton:** Solo existen `package-info.java` en la capa de infraestructura. No hay adaptadores implementados, lo que impide verificar correctitud de puertos en contexto real.
- **`AuditLogPort`** existe pero no hay una entidad `AuditLog` en el dominio — el modelo de auditoria no esta completamente definido en domain.

### Observacion de similitud de codigo
La estructura del codigo en domain (entidades `BankAccount`, `Loan`, `Transfer`, enums) es practicamente identica a la del repositorio `ContruccionDeSoftware2` (Juan Hinestroza / Miguel Gonzales). Se recomienda al docente verificar el origen de ambas implementaciones.

---

## Recomendaciones
1. Mover los puertos de salida a la capa `domain/ports/` para una arquitectura hexagonal estricta.
2. Considerar agregar servicios de dominio en `domain/` para encapsular reglas de negocio que hoy viven en los use cases de application.
3. Implementar los adaptadores de infraestructura para completar el ciclo de la arquitectura.
4. Agregar la entidad `AuditLog` al dominio con su `AuditLogPort` bien definido.
