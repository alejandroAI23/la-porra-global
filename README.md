# La Porra Global ⚽

Plataforma **no oficial** de predicciones de fútbol para crear ligas privadas entre amigos, empresas y bares durante el gran torneo internacional de selecciones de 2026.

> ⚠️ Proyecto independiente sin afiliación a FIFA ni a ningún organismo oficial. No usa marcas registradas ni assets oficiales. **No permite apuestas con dinero real**: solo predicciones lúdicas y rankings privados.

## Stack

| Capa | Tecnología |
|---|---|
| Backend | Java 21, Spring Boot 3, Maven |
| Base de datos | MySQL 8 |
| Seguridad | Spring Security + JWT |
| Documentación | OpenAPI / Swagger |
| Frontend | React + Vite + Tailwind CSS (PWA) |
| Testing | JUnit 5, Mockito, Testcontainers |
| Infraestructura | Docker Compose |

## Arquitectura

Monolito modular preparado para evolucionar a microservicios. Cada módulo (`auth`, `users`, `tournament`, `leagues`, `predictions`, `ranking`, `bars`, `notifications`, `payments`, `ai`) tiene su propio paquete con `controller`, `service`, `repository`, `dto` y `mapper`.

```
backend/src/main/java/com/porraglobal/
├── PorraGlobalApplication.java
├── common/          # config, seguridad, excepciones, utilidades
│   ├── config/      # Swagger, CORS, seguridad
│   ├── exception/   # excepciones globales + handler
│   └── security/    # JWT filter, provider, UserDetails
├── auth/
├── users/
├── tournament/      # Team, Match
├── leagues/         # League, LeagueMember
├── predictions/
├── ranking/
├── bars/            # Bar, BarEvent
├── notifications/
├── payments/        # Subscription (planes premium, sin apuestas)
└── ai/              # asistente de predicciones (futuro)
```

## Ejecutar en local

### Opción A: Docker Compose (todo)

```bash
docker compose up --build
```

- Backend: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Frontend: http://localhost:5173
- MySQL: localhost:3307 (`porra` / `porra_secret`)

### Opción B: Desarrollo local

1. **Base de datos** (solo MySQL en Docker):

```bash
docker compose up -d mysql
```

2. **Backend**:

```bash
cd backend
./mvnw spring-boot:run
```

3. **Frontend**:

```bash
cd frontend
npm install
npm run dev
```

## Tests

```bash
cd backend
./mvnw test                 # unitarios
./mvnw verify -Pit          # integración con Testcontainers (requiere Docker)
```

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registro de usuario |
| POST | `/api/auth/login` | Login (devuelve JWT) |
| GET | `/api/matches` | Listado de partidos |
| POST | `/api/leagues` | Crear liga privada |
| POST | `/api/leagues/{id}/join` | Unirse a liga con código |
| GET | `/api/leagues/{id}/ranking` | Ranking de la liga |
| POST | `/api/predictions` | Enviar predicción |
| GET | `/api/bars` | Listar bares |
| POST | `/api/bars` | Registrar bar |
| GET | `/api/bars/{id}/events` | Eventos de un bar |

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SPRING_DATASOURCE_URL` | jdbc:mysql://localhost:3307/porra_global | URL de MySQL |
| `SPRING_DATASOURCE_USERNAME` | porra | Usuario BD |
| `SPRING_DATASOURCE_PASSWORD` | porra_secret | Password BD |
| `JWT_SECRET` | (dev default) | Clave HMAC ≥ 256 bits |
| `JWT_EXPIRATION_MS` | 86400000 | Expiración del token (24h) |
