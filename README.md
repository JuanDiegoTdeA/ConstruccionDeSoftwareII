# Bank Management
Members:
Maria Jose Lopez Velasquez - Juan Diego Millan Cano 

# Bank Core — Sistema de Gestión Bancaria

Backend académico para la gestión de información de un banco, desarrollado con **Spring Boot** y organizado bajo **DDD + Arquitectura Hexagonal (Ports & Adapters)**.

---

## Base de Datos

| Motor | Propósito | Host | Puerto | Base de datos |
|-------|-----------|------|--------|---------------|
| **MySQL** | Datos relacionales: clientes, cuentas bancarias, préstamos, transferencias, productos bancarios | `localhost` | `3306` | `bankdb` |
| **MongoDB** | Bitácora de operaciones (auditoría y trazabilidad) | `localhost` | `27017` | `Bank_audit` |

### Crear la base de datos MySQL

```sql
CREATE DATABASE IF NOT EXISTS bankdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> MongoDB crea la base `bank_audit` automáticamente al recibir el primer documento.

### Variables de entorno opcionales

| Variable | Default | Descripción |
|----------|---------|-------------|
| `DB_USER` | `root` | Usuario de MySQL |
| `DB_PASS` | `root` | Contraseña de MySQL |

---

El proyecto modela clientes, usuarios del sistema, cuentas bancarias, préstamos, transferencias y bitácora de operaciones, separando el **dominio**, la **capa de aplicación** y la futura **infraestructura**.

---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Objetivo de la Entrega](#objetivo-de-la-entrega)
- [Estado Actual del Proyecto](#estado-actual-del-proyecto)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Modelo de Dominio Implementado](#modelo-de-dominio-implementado)
- [Puertos y Servicios Implementados](#puertos-y-servicios-implementados)
- [Reglas de Negocio Implementadas](#reglas-de-negocio-implementadas)
- [Roles Modelados](#roles-modelados)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Configuración e Instalación](#configuración-e-instalación)
- [Ejecución](#ejecución)
- [Trabajo Pendiente](#trabajo-pendiente)
- [Contribuciones](#contribuciones)

---

## Descripción General

Este proyecto implementa el núcleo lógico de una aplicación bancaria orientada a la gestión de:

1. **Clientes** persona natural y empresa.
2. **Usuarios del sistema** con roles y estados propios.
3. **Cuentas bancarias** con validaciones operativas.
4. **Préstamos** con flujo de solicitud, aprobación, rechazo y desembolso.
5. **Transferencias** con ejecución directa, aprobación, rechazo y vencimiento.
6. **Bitácora de operaciones** para trazabilidad y auditoría.

La solución se está construyendo con una separación clara entre:

- **Dominio**: reglas del negocio y modelos principales.
- **Aplicación**: puertos y casos de uso.
- **Infraestructura**: adaptadores de persistencia y configuración (en construcción).

---

## Objetivo de la Entrega

Esta segunda entrega se enfoca en la **generación de los puertos y servicios del banco usando arquitectura DDD**, partiendo de una corrección del modelo inicial.

En esta versión se corrigieron especialmente:

- separación entre **`User`** y **`Client`**
- uso de **enums** para estados y catálogos
- transición de estados en código
- validaciones dentro del dominio
- estructura estándar con **Spring Boot + Maven**
- definición de **puertos de entrada**, **puertos de salida** y **servicios de aplicación**

---

## Estado Actual del Proyecto

### Implementado

- Modelo de dominio base del banco
- Enums de roles, estados y catálogos
- Casos de uso de apertura de cuenta
- Casos de uso de solicitud, aprobación, rechazo y desembolso de préstamos
- Casos de uso de creación, rechazo y expiración de transferencias
- Capa de puertos `in` y `out`
- Excepciones de negocio
- Bitácora a través de puerto de auditoría

### En construcción

- Adaptadores de persistencia SQL y NoSQL
- Exposición REST
- Seguridad/autenticación
- DTOs y controladores
- documentación de API
- pruebas automáticas

> **Nota:** esta versión representa el núcleo del dominio y la capa de aplicación. Todavía no expone una API REST completa ni persistencia real.

---

## Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| Framework | Spring Boot |
| Lenguaje | Java 17+ |
| Build | Maven (Maven Wrapper incluido) |
| Arquitectura | DDD + Hexagonal (Ports & Adapters) |
| Validación | Reglas implementadas en dominio y servicios |
| Auditoría | Puerto de auditoría listo para adaptador NoSQL |
| Utilidades | Lombok |

---

## Arquitectura

El proyecto implementa una estructura inspirada en **Arquitectura Hexagonal (Ports & Adapters)**:

```text
┌────────────────────────────────────────────────────────────┐
│  Capa de entrada                                          │
│  Use Cases / futuros Controllers / futuros DTOs           │
└───────────────────────┬────────────────────────────────────┘
                        │
┌───────────────────────▼────────────────────────────────────┐
│  Capa de Aplicación                                        │
│  OpenBankAccountService                                    │
│  RequestLoanService                                        │
│  ApproveLoanService                                        │
│  RejectLoanService                                         │
│  DisburseLoanService                                       │
│  CreateTransferService                                     │
│  RejectTransferService                                     │
│  ExpirePendingTransfersService                             │
└───────────────────────┬────────────────────────────────────┘
                        │ (Ports / Interfaces)
┌───────────────────────▼────────────────────────────────────┐
│  Capa de Dominio                                           │
│  User, Client, IndividualClient, BusinessClient            │
│  BankAccount, Loan, Transfer, BankingProduct               │
│  Enums, validaciones, transiciones de estado               │
└───────────────────────┬────────────────────────────────────┘
                        │ (Adaptadores de salida)
┌───────────────────────▼────────────────────────────────────┐
│  Infraestructura                                           │
│  Persistencia SQL / NoSQL / configuración (pendiente)      │
└────────────────────────────────────────────────────────────┘
```

---

## Modelo de Dominio Implementado

### Entidades principales

- `User`
- `Client`
- `IndividualClient`
- `BusinessClient`
- `BankingProduct`
- `BankAccount`
- `Loan`
- `Transfer`

### Decisiones clave de modelado

- `User` y `Client` son jerarquías **independientes**.
- `relatedClientId` permite vincular un usuario con un cliente.
- Los estados y catálogos ya no están modelados como `String`, sino como **enums**.
- Las reglas principales viven en el dominio y no solo en documentación.
- El dominio está desacoplado de Spring y de JPA.

### Enums implementados

- `SystemRole`
- `UserStatus`
- `AccountType`
- `AccountStatus`
- `ProductCategory`
- `LoanStatus`
- `LoanType`
- `TransferStatus`
- `CurrencyType`

---

## Puertos y Servicios Implementados

### Puertos de entrada (`application.ports.in`)

- `OpenBankAccountUseCase`
- `RequestLoanUseCase`
- `ApproveLoanUseCase`
- `RejectLoanUseCase`
- `DisburseLoanUseCase`
- `CreateTransferUseCase`
- `ApproveTransferUseCase`
- `RejectTransferUseCase`
- `ExpirePendingTransfersUseCase`
- `RegisterAuditLogUseCase`

### Puertos de salida (`application.ports.out`)

- `UserRepositoryPort`
- `ClientRepositoryPort`
- `BankAccountRepositoryPort`
- `LoanRepositoryPort`
- `TransferRepositoryPort`
- `BankingProductRepositoryPort`
- `AuditLogPort`

### Servicios de aplicación (`application.usecase`)

- `OpenBankAccountService`
- `RequestLoanService`
- `ApproveLoanService`
- `RejectLoanService`
- `DisburseLoanService`
- `CreateTransferService`
- `RejectTransferService`
- `ExpirePendingTransfersService`

> La implementación actual prioriza los flujos de negocio principales de la entrega. Otros servicios podrán agregarse en fases posteriores.

---

## Reglas de Negocio Implementadas

Entre las reglas ya modeladas en dominio y aplicación se encuentran:

- Un usuario bloqueado o inactivo no puede habilitar apertura de cuenta.
- Una cuenta no puede operar si está bloqueada o cancelada.
- No se permiten depósitos o retiros con montos menores o iguales a cero.
- No se permiten retiros con saldo insuficiente.
- Un préstamo inicia en `UNDER_REVIEW`.
- Un préstamo solo puede pasar de `UNDER_REVIEW` a `APPROVED` o `REJECTED`.
- Un préstamo solo puede pasar a `DISBURSED` desde `APPROVED`.
- Una transferencia ejecutada actualiza los saldos de cuenta origen y destino.
- Una transferencia pendiente puede expirar automáticamente si supera la ventana configurada.
- Las operaciones críticas generan trazabilidad por medio del puerto de auditoría.

---

## Roles Modelados

El sistema contempla los siguientes roles de negocio:

- `INDIVIDUAL_CLIENT`
- `BUSINESS_ADMIN`
- `TELLER_EMPLOYEE`
- `COMMERCIAL_EMPLOYEE`
- `COMPANY_OPERATOR`
- `COMPANY_SUPERVISOR`
- `INTERNAL_ANALYST`

Estos roles se usan para restringir operaciones críticas como:

- aprobación y rechazo de préstamos
- aprobación y rechazo de transferencias
- apertura de cuentas
- operación sobre productos propios o asociados

---

## Estructura del Proyecto

```text
bank
├── src
│   ├── main
│   │   ├── java/co/edu/tdea/bank
│   │   │   ├── application
│   │   │   │   ├── ports/in
│   │   │   │   ├── ports/out
│   │   │   │   └── usecase
│   │   │   ├── domain
│   │   │   │   ├── enums
│   │   │   │   ├── models
│   │   │   │   └── services
│   │   │   ├── infrastructure
│   │   │   │   ├── adapter
│   │   │   │   └── config
│   │   │   └── shared
│   │   │       └── Exceptions
│   │   └── resources
│   └── test
├── mvnw
├── mvnw.cmd
└── pom.xml
```

---

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.8+** o uso del wrapper `./mvnw`
- **Git**
- **VS Code** o IDE Java compatible

> Aunque el proyecto compila con versiones recientes de Java, la referencia recomendada para el desarrollo es **Java 17**.

---

## Configuración e Instalación

1. Clonar el repositorio:
   ```bash
   git clone <url-del-repositorio>
   cd ContruccionDeSoftware2/bank
   ```

2. Compilar el proyecto:
   ```bash
   ./mvnw clean compile
   ```

3. En Windows:
   ```powershell
   .\\mvnw.cmd clean compile
   ```

---

## Ejecución

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\\mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en el puerto configurado por Spring Boot.

> En esta etapa, el proyecto puede arrancar como base Spring Boot, aunque la exposición REST completa y la persistencia definitiva siguen en construcción.

---

## Trabajo Pendiente

Las siguientes etapas del proyecto contemplan:

- adaptadores de persistencia para SQL y NoSQL
- implementación de repositorios concretos
- controladores REST
- DTOs de request/response
- seguridad con autenticación y autorización
- documentación de API
- pruebas unitarias e integración
- documentación C4 y diagramas

---

## Contribuciones

Proyecto académico desarrollado para la asignatura **Construcción de Software 2**.

Las mejoras futuras deberán mantenerse alineadas con:

- principios de DDD
- arquitectura hexagonal
- separación de responsabilidades
- reglas de negocio del enunciado del banco
 documentan reglas importantes: unicidad de identificaciones, restricciones de estados, rol del Analista Interno en préstamos, flujos de aprobación de transferencias empresariales y vencimiento automático a los 60 minutos, tal como se describe en las secciones de reglas de negocio y flujos de aprobación.
