---
name: android-arch
description: Apply Ren's clean architecture conventions to any project — feature-based layering (domain/data/ui/di), dependency rule, naming conventions (I prefix, Impl suffix, Domain/Data suffix), MVI pattern for ViewModels, and atomic commit + TDD workflow. Use whenever creating a new feature, adding a repository / use case / ViewModel / screen, structuring a new project, or reviewing whether code follows the conventions. Trigger on phrases like "create a feature", "add a use case", "new repository", "set up clean architecture", "MVI", "structure this project", "where does this go".
user-invocable: true
---

# Clean Architecture Conventions (Ren)

These are the conventions Ren uses across projects. They are framework-agnostic in spirit — they originated in an Android/Kotlin codebase but the structure, dependency rule, and naming apply to any language. Adapt folder casing to the language (e.g. `domain/` in Kotlin, `Domain/` in Swift, etc.).

## 1. Feature-based layout

Group by **feature**, not by technical layer at the top level. Each feature contains its own three layers:

```
features/
├── core/                       # Shared domain models across features
│   └── domain/model/
│
└── {feature}/
    ├── domain/                 # Pure business logic — no framework deps
    │   ├── model/              # Domain entities ({Name}Domain)
    │   ├── repository/         # Repository interfaces (I{Name}Repository)
    │   └── usecase/            # Use cases (I{Name}UseCase + {Name}UseCaseImpl)
    │
    ├── data/                   # Data access
    │   ├── datasource/         # Local/Remote sources (I* + *Impl)
    │   ├── repository/         # Repository implementations
    │   └── mapper/             # Data ↔ Domain mappers
    │
    ├── ui/                     # Presentation
    │   ├── viewmodel/          # ViewModels (MVI)
    │   ├── screen/             # Screens / views
    │   ├── {Feature}State.kt   # Immutable state
    │   └── {Feature}Event.kt   # Events (On* prefix)
    │
    └── di/                     # DI module for the feature
        └── {Feature}Module.kt
```

Shared domain models that cross features go in `features/core/domain/model/`. Don't put feature-specific models there.

## 2. The dependency rule

```
data ──▶ domain ◀── ui
```

- **domain** depends on **nothing**. No Android, no Firebase, no Room, no HTTP client, no framework — pure language code only. If you're tempted to import something framework-y here, it belongs in `data` or `ui`.
- **data** depends on **domain** (it implements the repository interfaces defined there).
- **ui** depends on **domain** (it consumes use cases — never repositories directly).
- **data** and **ui** never depend on each other.

When in doubt: can this file be compiled on a different platform with zero changes? If yes, it's domain. If no, it's data or ui.

## 3. Naming conventions

| Kind | Convention | Example |
|------|------------|---------|
| Interface | `I` prefix | `IFavouritesRepository`, `IGetFavouritesUseCase` |
| Implementation | `Impl` suffix | `FavouritesRepositoryImpl`, `GetFavouritesUseCaseImpl` |
| Domain model | `Domain` suffix | `FavouriteDomain`, `PlaceDomain` |
| Data model (DTO / entity) | `Data` suffix | `PlaceData`, `FavouriteData` |
| MVI event | `On` prefix | `OnSearchQueryChanged`, `OnPlaceClicked` |
| DI module | `{Feature}Module` | `FavouritesModule` |

Use cases are **one class per action**, not a god-object repository wrapper: `ObserveFavouritesUseCase`, `AddFavouriteUseCase`, `RemoveFavouriteUseCase`.

## 4. MVI pattern for ViewModels

- State is an **immutable** data class. Mutate via `copy()`.
- Expose state as a single observable (`StateFlow<State>` in Kotlin, `@Published` in Swift, a store in TS, etc.).
- Expose a single entry point for user intents: `sendEvent(Event)` / `dispatch(Action)`.
- Events use the `On*` prefix — they describe what the user did, not what should happen.
- One-shot side effects (navigation, toasts) go through a separate channel/flow, not state.

```
View ──Event──▶ ViewModel ──State──▶ View
                    │
                    └── UseCase ──▶ Repository ──▶ DataSource
```

## 5. Mappers belong in data

Domain models never know about data models. Mapping `*Data → *Domain` (and back, if needed) happens in `data/mapper/`. Repositories return domain types; data sources return data types.

## 6. Architecture tests

Enforce the rules with a tool appropriate to the stack (Konsist for Kotlin, dependency-cruiser for TS, etc.). At minimum, assert:

- domain imports nothing from data, ui, or any framework package
- interface/impl naming matches the conventions
- ui never imports from data directly

If the project doesn't have an architecture-test tool yet, suggest adding one when introducing this structure.

## 7. Workflow rules that pair with this

These apply to any project Ren works on:

- **Atomic commits** — one logical change per commit. Don't bundle unrelated work.
- **TDD where there's logic** — failing test first, then implementation. Pure data-class field additions or wiring-only commits don't need a test.
- **Gitmoji** commit messages, **always lowercase**, no "Co-authored by Claude" trailer.
- When unsure about an approach, **ask before writing code**.

## How to apply this skill

When the user asks to add a feature, a use case, a repository, or a new screen:

1. Identify which feature folder it belongs to (create one if it's genuinely new).
2. Place each file in the correct layer per section 1.
3. Apply the naming conventions from section 3 — don't invent variants.
4. If you're adding logic, write the test first.
5. Commit atomically with a lowercase gitmoji message.

When reviewing existing code against these conventions, flag violations concretely (file path + which rule) rather than rewriting silently.
