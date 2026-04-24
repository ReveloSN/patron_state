# Chemical Reaction Control

Full-stack implementation of a controlled chemical reaction tracking system using:

- `Spring Boot` for the backend REST API
- `React + TypeScript + Vite` for the frontend
- in-memory persistence
- the `State` pattern in the domain model
- a hybrid compound catalog with optional PubChem lookup and local offline fallback

## Project structure

- `backend`: Java backend with layered architecture and state-driven domain behavior
- `frontend`: React client for creating, operating, and reviewing a reaction lifecycle

## Run the backend

```powershell
cd backend
.\gradlew.bat bootRun
```

The API starts on `http://localhost:8080`.

## Run the frontend

```powershell
cd frontend
npm install
npm run dev
```

The client starts on `http://localhost:5173`.

## Verify the project

```powershell
cd backend
.\gradlew.bat build
```

```powershell
cd frontend
npm run build
```

## Online and offline catalog

The backend exposes a hybrid chemical reference catalog:

- When external connectivity is available, the backend can query PubChem for compound names, synonyms, molecular formula, molecular weight, and external identifiers.
- When there is no internet connection or PubChem is unavailable, the backend automatically falls back to the local educational compound catalog.
- Educational recipes are always served from the local catalog.

Available catalog endpoints:

- `GET /api/catalog/compounds?query=...`
- `GET /api/catalog/compounds/{externalId}`
- `GET /api/catalog/recipes`
