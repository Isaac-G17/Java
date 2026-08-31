# VetCare

Sistema de gestión para una clínica veterinaria, desarrollado en Java SE con persistencia en PostgreSQL. Permite administrar propietarios, mascotas, veterinarios, especialidades, citas, atenciones médicas y el inventario de medicamentos, con control de acceso basado en roles.

## Descripción

VetCare es una aplicación de escritorio (interfaz gráfica con `JOptionPane`) que digitaliza el flujo operativo de una clínica veterinaria: desde el registro de propietarios y sus mascotas, la programación de citas con un veterinario, hasta la atención médica misma —incluyendo el consumo de medicamentos del inventario y el descuento automático de existencias—.

La aplicación distingue tres roles de usuario (`ADMIN`, `RECEPCIONISTA`, `VETERINARIO`), cada uno con acceso a un subconjunto distinto de funcionalidades según sus responsabilidades.

## Caso de uso

Flujo principal ("camino feliz") que cubre la aplicación:

1. **Login**: el usuario inicia sesión con correo y contraseña; el menú disponible se filtra según su rol.
2. **Registrar propietario**: se crea un `Owner` con sus datos de contacto.
3. **Registrar mascota**: se asocia una `Pet` a un propietario activo.
4. **Registrar especialidad** (ADMIN): se define una especialidad médica (ej. "Cirugía", "Dermatología").
5. **Registrar veterinario** (ADMIN): se vincula un `User` con rol `VETERINARIO` a una especialidad existente y una tarjeta profesional única.
6. **Agendar cita**: se programa una `Appointment` entre una mascota y un veterinario, validando que ambos estén activos, que no haya cruces de horario, y que la fecha no sea pasada.
7. **Confirmar cita**: la cita pasa de `PROGRAMADA` a `CONFIRMADA`.
8. **Iniciar atención**: solo permitido desde una cita `CONFIRMADA`; crea una `Attention` y mueve la cita a `EN_ATENCION`.
9. **Finalizar atención**: se registra diagnóstico, tratamiento/observación y los medicamentos utilizados. En una única transacción se actualiza la atención, se insertan los `AttentionDetails`, se descuenta el inventario de cada medicamento (validando existencias) y la cita pasa a `FINALIZADA`. Si algo falla, todo se revierte (rollback).
10. **Consultar historial médico**: se lista el historial de atenciones de una mascota.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 (SE) | Lenguaje y plataforma |
| Maven | Gestión de dependencias y build |
| Swing (`JOptionPane`) | Interfaz gráfica |
| JDBC | Acceso a datos |
| PostgreSQL | Motor de base de datos |

Dependencia declarada en `pom.xml`: `org.postgresql:postgresql:42.7.13`.

## Estructura de paquetes

Arquitectura por capas: `model → dao (interfaces + impl) → service (interfaces + impl) → controller → view`.

```
com.vetcare
├── app          # Punto de entrada (App.java: main)
├── config       # ConnectionDB: configuración de la conexión JDBC
├── model        # Entidades del dominio (POJOs)
├── enums        # IdentificationType, Sex, AppointmentStatus, AttentionStatus
├── dao          # Interfaces de acceso a datos
│   └── impl     # Implementaciones JDBC de cada DAO
├── service      # Interfaces de lógica de negocio
│   └── impl     # Implementaciones de servicio (reglas de negocio, transacciones)
├── controller   # Orquestan vista <-> servicio, manejan excepciones
├── view         # Diálogos Swing/JOptionPane
└── exception    # Jerarquía de excepciones de la aplicación
```

**Jerarquía de excepciones:**

- `BusinessException` (extiende `RuntimeException`) — reglas de negocio violadas (ej. `InsufficientStockException`, `InvalidAppointmentStateException`, `DuplicateOwnerDocumentException`, `AppointmentConflictException`, etc.)
- `ValidationException` (extiende `RuntimeException`) — datos de entrada inválidos.
- `PersistenceException` (extiende `RuntimeException`) — errores de acceso a datos (envuelve `SQLException`).

Cada `Controller` diferencia estas tres categorías: `ValidationException`/`BusinessException` muestran el mensaje real al usuario, `PersistenceException` muestra un mensaje genérico (sin detalles técnicos de SQL), y cualquier otra `Exception` muestra un mensaje inesperado sin cerrar la aplicación.

## Diagrama de clases

Modelo de dominio (paquete `model`) y sus relaciones:

```mermaid
classDiagram
    class Role {
        -int id
        -String name
    }
    class User {
        -int id
        -IdentificationType identificationType
        -String identificationNumber
        -String firstName
        -String lastName
        -String phoneNumber
        -String email
        -String password
        -boolean status
        -LocalDateTime registrationDate
    }
    class Owner {
        -int id
        -IdentificationType identificationType
        -String identificationNumber
        -String firstName
        -String lastName
        -String phoneNumber
        -String email
        -String address
        -boolean status
        -LocalDateTime registrationDate
    }
    class Pet {
        -int id
        -String name
        -String species
        -String breed
        -Sex sex
        -LocalDate dateOfBirth
        -BigDecimal weight
        -boolean status
        -LocalDateTime registrationDate
    }
    class Specialty {
        -int id
        -String name
    }
    class Veterinarian {
        -int id
        -String professionalLicense
    }
    class Appointment {
        -int id
        -LocalDate date
        -LocalTime hour
        -String reason
        -AppointmentStatus status
        -LocalDateTime registrationDate
    }
    class Attention {
        -int id
        -String symptoms
        -String diagnosis
        -String treatment
        -String observation
        -LocalDateTime dateAttention
        -AttentionStatus status
    }
    class AttentionDetails {
        -int id
        -int quantity
    }
    class Medication {
        -int id
        -String code
        -String name
        -String presentation
        -String laboratory
        -int availableQuantity
        -int minimumQuantity
        -BigDecimal price
        -boolean status
        -LocalDateTime registrationDate
    }

    Role "1" --> "*" User : id_rol
    User "1" --> "1" Veterinarian : id_user
    Specialty "1" --> "*" Veterinarian : id_specialty
    Owner "1" --> "*" Pet : id_owner
    Pet "1" --> "*" Appointment : id_pet
    Veterinarian "1" --> "*" Appointment : id_veterinarian
    Appointment "1" --> "0..1" Attention : id_appointment
    Veterinarian "1" --> "*" Attention : id_veterinarian
    Pet "1" --> "*" Attention : id_pet
    Attention "1" --> "*" AttentionDetails : id_attention
    Medication "1" --> "*" AttentionDetails : id_medication
```

Cada entidad principal (`Owner`, `Pet`, `Veterinarian`, `Appointment`, `Attention`, `Medication`, `User`, `Specialty`, `Role`) tiene su propia interfaz `XxxDAO` + `XxxDAOImpl`, `XxxService` + `XxxServiceImpl`, `XxxController` y `XxxView`, siguiendo el mismo patrón capa por capa.

## Diagrama entidad-relación

Corresponde al esquema físico en PostgreSQL (`db.sql` / `SCRIPT VETCARE.sql`):

```mermaid
erDiagram
    ROLE ||--o{ USERS : id_rol
    USERS ||--|| VETERINARIAN : id_user
    SPECIALTY ||--o{ VETERINARIAN : id_specialty
    OWNER ||--o{ PET : id_owner
    PET ||--o{ APPOINTMENT : id_pet
    VETERINARIAN ||--o{ APPOINTMENT : id_veterinarian
    APPOINTMENT ||--o| ATTENTION : id_appointment
    VETERINARIAN ||--o{ ATTENTION : id_veterinarian
    PET ||--o{ ATTENTION : id_pet
    ATTENTION ||--o{ ATTENTION_DETAILS : id_attention
    MEDICATION ||--o{ ATTENTION_DETAILS : id_medication

    ROLE {
        int id PK
        varchar name
    }
    USERS {
        int id PK
        varchar identification_type
        varchar identification_number
        varchar first_name
        varchar last_name
        varchar phone_number
        varchar email
        varchar password
        boolean status
        timestamp registration_date
        int id_rol FK
    }
    OWNER {
        int id PK
        varchar identification_type
        varchar identification_number
        varchar first_name
        varchar last_name
        varchar phone_number
        varchar email
        varchar address
        boolean status
        timestamp registration_date
    }
    PET {
        int id PK
        varchar name
        varchar species
        varchar breed
        varchar sex
        date date_of_birth
        decimal weight
        boolean status
        timestamp registration_date
        int id_owner FK
    }
    SPECIALTY {
        int id PK
        varchar name
    }
    VETERINARIAN {
        int id PK
        varchar professional_license
        int id_user FK
        int id_specialty FK
    }
    APPOINTMENT {
        int id PK
        date date
        time hour
        text reason
        varchar status
        timestamp registration_date
        int id_pet FK
        int id_veterinarian FK
    }
    MEDICATION {
        int id PK
        varchar code
        varchar name
        varchar presentation
        varchar laboratory
        int available_quantity
        int minimum_quantity
        decimal price
        boolean status
        timestamp registration_date
    }
    ATTENTION {
        int id PK
        text symptoms
        text diagnosis
        text treatment
        text observation
        timestamp date_attention
        varchar status
        int id_veterinarian FK
        int id_appointment FK
        int id_pet FK
    }
    ATTENTION_DETAILS {
        int id PK
        int quantity
        int id_attention FK
        int id_medication FK
    }
```

## Configuración de la base de datos

1. Crea la base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE vetcare;
   ```
2. Ejecuta el script de creación de tablas ubicado en la raíz del proyecto: [`SCRIPT VETCARE.sql`](SCRIPT%20VETCARE.sql) (o su equivalente [`db.sql`](db.sql)).
3. Ajusta las credenciales de conexión en [`app/src/main/java/com/vetcare/config/ConnectionDB.java`](app/src/main/java/com/vetcare/config/ConnectionDB.java) si son distintas a las siguientes por defecto:

   ```java
   URL  = "jdbc:postgresql://localhost:5432/vetcare"
   USER = "postgres"
   PASS = "123456"
   ```
4. Siembra al menos los tres roles base (si el script no los incluye ya):
   ```sql
   INSERT INTO role (name) VALUES ('ADMIN'), ('RECEPCIONISTA'), ('VETERINARIO');
   ```
5. Crea un usuario inicial con rol `ADMIN` para poder iniciar sesión la primera vez (las contraseñas se almacenan en texto plano, tal como están definidas en el esquema actual):
   ```sql
   INSERT INTO users (identification_type, identification_number, first_name, last_name,
       phone_number, email, password, id_rol)
   VALUES ('CC', '0000000000', 'Admin', 'VetCare', '3000000000',
       'admin@vetcare.com', '1234', (SELECT id FROM role WHERE name = 'ADMIN'));
   ```

## Instrucciones de ejecución

Requisitos: JDK 21+, Maven 3.9+, PostgreSQL con la base de datos configurada como arriba.

**1. Compilar** (desde la carpeta `app/`):
```bash
mvn compile
```

**2. Ejecutar** (usando el driver de PostgreSQL ya descargado por Maven):

En bash / Git Bash:
```bash
java -cp "target/classes;$HOME/.m2/repository/org/postgresql/postgresql/42.7.13/postgresql-42.7.13.jar" com.vetcare.app.App
```

En PowerShell:
```powershell
& java -cp "target\classes;$env:USERPROFILE\.m2\repository\org\postgresql\postgresql\42.7.13\postgresql-42.7.13.jar" com.vetcare.app.App
```

> Si tu `java` del PATH no es JDK 21+, reemplaza `java` por la ruta completa a tu instalación (ej. `"C:\Program Files\Java\jdk-21.0.12.1\bin\java.exe"`).

La aplicación abre una ventana de login (Swing). Ingresa el correo y contraseña de un usuario existente para acceder al menú principal, filtrado según tu rol.

## Funcionalidades implementadas

- **Autenticación y control de acceso por rol**: login contra la base de datos; el menú principal muestra únicamente las opciones permitidas para `ADMIN`, `RECEPCIONISTA` o `VETERINARIO`.
- **Propietarios**: registrar, listar, buscar por documento, activar/desactivar.
- **Mascotas**: registrar (validando propietario activo, peso > 0, fecha de nacimiento no futura, sin duplicados), listar, buscar por nombre/propietario, activar/desactivar.
- **Especialidades** (ADMIN): registrar (nombre único) y listar.
- **Veterinarios** (ADMIN): registrar (usuario con rol `VETERINARIO` + especialidad + tarjeta profesional única), listar, filtrar por especialidad, activar/desactivar.
- **Usuarios** (ADMIN): registrar (correo único, contraseña obligatoria), listar, activar/desactivar.
- **Citas**: agendar (validando mascota/propietario/veterinario activos, fecha no pasada, sin cruces de horario), listar, buscar por mascota/veterinario/fecha, confirmar y cancelar (con validación de la máquina de estados: no se puede confirmar o cancelar una cita ya `FINALIZADA` o `CANCELADA`).
- **Atenciones médicas**:
  - Iniciar atención únicamente desde una cita `CONFIRMADA` (transacción: crea la atención y mueve la cita a `EN_ATENCION`).
  - Finalizar atención con diagnóstico, tratamiento/observación y medicamentos utilizados, en una transacción única que actualiza la atención, registra el detalle de medicamentos, descuenta el inventario (con validación atómica de existencias) y mueve la cita a `FINALIZADA`; si algo falla, se revierte todo el conjunto de cambios.
  - Consultar historial médico por mascota.
- **Medicamentos**: registrar, listar, actualizar existencias, activar/desactivar, ver medicamentos con inventario bajo el mínimo.
- **Manejo de errores diferenciado**: cada controlador distingue errores de validación/negocio (mensaje real al usuario), errores de persistencia (mensaje genérico, sin exponer detalles de SQL) y errores inesperados (sin cerrar la aplicación).

## Datos del coder

- **Nombre**: Isaac Guzmán
