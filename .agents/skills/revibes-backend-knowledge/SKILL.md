---
name: revibes-backend-knowledge
description: "Guidelines, specifications, and full knowledge graph mapping for the Revibes Node.js/TypeScript Express & Firebase Cloud Functions backend service (API endpoints, DTO contracts, Firestore data models, authentication mechanisms, controllers, and business logic). Make sure to use this skill whenever the user mentions backend API endpoints, Firestore schemas, server data models, API payloads/DTOs, Ktorfit client implementations matching backend routes, or integration between Revibes Android and Revibes Backend."
---

# Revibes Backend Knowledge Skill

Source of truth for the Revibes Node.js / Express / Firebase Cloud Functions backend architecture, HTTP API endpoints, Firestore data models, authentication contracts, DTO schemas, and client-server integration mappings for Revibes-Android.

<instructions>
Refer to the reference documents below when creating or modifying Android networking services (Ktorfit), DTO models, authentication flows, or repository sync logic that interacts with the Revibes Backend. Ensure strict adherence to endpoint routes, parameter names, DTO JSON contracts, and Firestore entity structures.
</instructions>

<references>
- **API Endpoints**: [api-endpoints.md](.agents/skills/revibes-backend-knowledge/references/api-endpoints.md) — HTTP routes, methods, path params, request/response DTOs for Auth, User/Me, Banners, Countries, Exchange, Inventory, Logistic Orders, Missions, Stores, Vouchers, and Settings.
- **Firestore Data Models**: [firestore-models.md](.agents/skills/revibes-backend-knowledge/references/firestore-models.md) — Database collections, entity fields, relationships, enums (Role, OrderStatus, MissionType, VoucherStatus, etc.).
- **Backend Architecture & Integration**: [architecture.md](.agents/skills/revibes-backend-knowledge/references/architecture.md) — Express handler pipeline, Cloud Function `v1` layout, JWT authentication, error handling contracts, and Android Ktorfit mapping guidelines.
</references>

<related>
For related architecture and design skills in Revibes-Android:
- `/revibes-architecture` — Android modularity system, Ktorfit networking configuration, DI, Orbit MVI, and storage.
- `/ktor-expert` — Ktor client engine, authentication tokens, and serialization setup.
- `/offline-first-expert` — Room caching and offline repository synchronization.
</related>

<constraints>
- Developers **must** prefix all shell command operations with `rtk` **only**.
- Always **require** using codebase-memory-mcp graph tools (`Users-jsanjaya-Projects-learning-Revibes-Backend` project) for deep backend code discovery.
- Match Kotlin data class property names and `@SerialName` annotations exactly with backend DTOs.
</constraints>
