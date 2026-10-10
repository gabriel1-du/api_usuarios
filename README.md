# Proyecto de microservicios : microservicio de usuarios para una tienda de historietas.

> En este repositorio se guardan los archivos de la api de usuarios (`api_usuarios`) desarrollada con Springboot y Java. Esta api gestiona usuarios, regiones y comunas, y pertenece a un sistema de microservicios del siguiente repositorio : https://github.com/gabriel1-du/Optativo-Desarollo-Java-Springboot-Cloud

## Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Frameworks / Librerías:** Springboot 4.1.1, Spring Data JPA, Spring RestClient, Spring Security, Lombok
* **Base de datos:** MySQL (`usuarios_db`)
* **Puerto:** 8080
* **Otras herramientas:** Git, Maven

---

## Diagrama de la base de datos 

![MER](<Base de datos/MERUsuarios.png>)

El script de creación de las tablas (`USUARIOS`, `REGIONES` y `COMUNAS`) está en `Base de datos/usuarios_bda.txt`.

Relaciones principales:

* `COMUNAS` N ──► 1 `REGIONES` (relación física por `id_region`).
* `USUARIOS` N ──► 1 `REGIONES` (relación física por `id_region`).
* `USUARIOS` N ──► 1 `COMUNAS` (relación física por `id_comuna`).

---
## Estructura de carpetas

```text
api_usuarios/
└── api_usuarios/
    ├── pom.xml
    └── src/main/
        ├── resources/
        │   └── application.properties  # Puerto, conexión MySQL y configuración JPA
        └── java/com/example/api_usuarios/
            ├── ApiUsuariosApplication.java
            ├── Controller/             # Endpoints REST
            ├── Service/                # Interfaces con el contrato de la lógica de negocio
            ├── ServiceImpl/            # Implementación de la lógica de negocio
            ├── Repository/             # Interfaces Spring Data JPA
            ├── Model/                  # Entidades JPA (tablas)
            ├── DTO/                    # DTOs y Mappers por entidad
            │   └── usuarioDTO/         # DTOs de usuario (GET, POST, PUT, Admin)
            ├── RestClient/             # Comunicación con otros microservicios
            │   ├── RestClientConfig.java
            │   ├── CrearCarritoDTO.java
            │   └── CrearListaDeDeseosDTO.java
            └── Seguridad/              # Configuración de Spring Security
                └── SecurityConfig.java
```

---
## Arquitectura y Flujo de Datos

El proyecto implementa una arquitectura en capas basada en separación de responsabilidades:

```text
[ Cliente / Frontend ]
         │ ▲
(HTTP)   ▼ │ (JSON / DTO)
   ┌─────────────┐
   │ Controller  │ ──► Expone los endpoints REST y gestiona la entrada/salida HTTP
   └─────────────┘
         │ ▲
(DTOs)   ▼ │
   ┌─────────────┐
   │   Service   │ ──► Interfaz: define el contrato de la lógica de negocio
   └─────────────┘
         │ ▲
         ▼ │
   ┌─────────────┐        ┌─────────────┐
   │ ServiceImpl │ ◄────► │ RestClient  │ ──► Consulta a otros microservicios (compras)
   └─────────────┘        └─────────────┘
         │ ▲              ServiceImpl: ejecuta reglas de negocio, validaciones y mapeo DTO ◄─► Modelo
(Model)  ▼ │
   ┌─────────────┐
   │ Repository  │ ──► Interfaz Spring Data JPA: acceso y operaciones sobre la base de datos
   └─────────────┘
         │ ▲
(SQL)    ▼ │
   ┌─────────────┐
   │   Database  │
   └─────────────┘

* DTO: Objeto transversal utilizado para transferir datos limpios entre capas sin exponer el Modelo o facilitar el cuerpo en las peticiones.
```

---
## Uso de RestClient en este microservicio

Este microservicio se comunica con el microservicio de compras mediante `RestClient` de Spring. Se utiliza para crear carritos y listas de deseos asociados a un usuario. Se aplica en dos pasos:

1. **Configuración (`RestClientConfig`)**: define dos beans `RestClient`, cada uno con su URL base:
   * `comprasRestClient` → `http://localhost:8081/api/carritosApi`
   * `listaDeseosRestClient` → `http://localhost:8081/api/ListaDeseosApi`
2. **Uso en los `ServiceImpl`**: el servicio llama al cliente para crear un carrito o lista de deseos cuando se registra un nuevo usuario, enviando un DTO con los datos necesarios (`CrearCarritoDTO` o `CrearListaDeDeseosDTO`).

| Cliente | Dónde se usa | Para qué |
|---|---|---|
| `comprasRestClient` | `UsuarioServiceImpl` | Crear un carrito automáticamente al registrar un nuevo usuario |
| `listaDeseosRestClient` | `UsuarioServiceImpl` | Crear una lista de deseos automáticamente al registrar un nuevo usuario |

> Para que estas consultas funcionen, el microservicio de compras (puerto 8081) debe estar levantado.

---
## Seguridad

Este microservicio utiliza **Spring Security** con las siguientes configuraciones:

* **BCryptPasswordEncoder**: las contraseñas se almacenan en la base de datos de forma segura usando hashing BCrypt.
* **CSRF deshabilitado**: permite peticiones POST/PUT sin token CSRF.
* **Acceso permitido**: todos los endpoints están abiertos (`permitAll`), sin autenticación obligatoria.

La configuración se encuentra en `Seguridad/SecurityConfig.java`.

---
## Endpoints

| Recurso | Ruta base | Métodos |
|---|---|---|
| Usuarios | `/api/usuariosApi` | GET, POST, PUT, DELETE |
| Regiones | `/api/regionesApi` | GET, POST, PUT, DELETE |
| Comunas | `/api/comunasApi` | GET, POST, PUT, DELETE |

Notas:

* Al crear un **usuario** (`POST /api/usuariosApi/`) se debe enviar `p_nombre`, `s_nombre`, `p_apellido`, `s_apellido`, `correo_elec`, `num_telefono`, `rut_dv`, `rut_cuerpo`, `contrasena`, `permiso_admin`, `id_region` e `id_comuna`. La contraseña se encripta automáticamente con BCrypt.
* Los endpoints `/admin` permiten ver y editar usuarios con información sensible (como la contraseña encriptada).
* Al registrar un nuevo usuario, se crea automáticamente un carrito y una lista de deseos asociados mediante RestClient.

---
# Dependencias (pom)

* Spring Boot Starter Data JPA
* Spring Boot Starter RestClient
* Spring Boot Starter WebMVC
* Spring Boot Starter Security
* MySQL Connector/J
* Project Lombok
* Spring Boot Starter Data JPA Test
* Spring Boot Starter RestClient Test
* Spring Boot Starter WebMVC Test
