# CLAUDE.md

Guidance for Claude Code when working in this repository.

## Project Overview

**KMP App Template** — a Kotlin Multiplatform + Compose Multiplatform starter project. Ships with a small Todos sample feature wired end-to-end (data + domain + ui + di) so the architecture is visible from a single screen.

- **Platforms**: Android, iOS, Desktop (JVM)
- **UI**: Compose Multiplatform with Material 3
- **Persistence**: SQLDelight
- **DI**: Koin (KMP)
- **Logging**: Napier with a swappable platform crash reporter
- **Architecture**: Local-first, no server, no lock-in
- **Package**: `com.renatojobal.kmptemplate`

When using this template for a new project, do a global find/replace on:
- `com.renatojobal.kmptemplate` → your package
- `kmptemplate` (db / resources namespace) → your slug
- `KmpAppTemplate` / `KMP App Template` → your product name

## Build Commands

```bash
# Build all
./gradlew build

# Android debug APK
./gradlew :composeApp:assembleDebug

# Run common tests (Kotest)
./gradlew :composeApp:commonTest

# Run android unit tests (includes Konsist architecture checks)
./gradlew :composeApp:testDebugUnitTest

# Desktop
./gradlew :composeApp:run

# iOS — open Xcode and run the iosApp scheme
open ./iosApp/iosApp.xcodeproj
```

## Architecture

### Clean Architecture — feature grouping

Dependencies flow: `data -> domain <- ui`

- **domain** depends on nothing (pure Kotlin)
- **data** depends on domain + frameworks
- **ui** depends on domain + frameworks

Each feature is organized as:

```
features/<feature_name>/
  domain/
    model/          # Domain entities (e.g., TodoDomain)
    repository/     # Repository interfaces (e.g., ITodoRepository)
    usecase/        # Use case interfaces + implementations
  data/
    datasource/     # Local/remote data source interfaces + implementations
    repository/     # Repository implementations
    mapper/         # Data-to-domain mappers
    model/          # Data layer models (DTOs, DB entities)
  ui/
    screen/         # Composable screens (Root + Screen split)
    viewmodel/      # ViewModels
    state/          # UI state + actions
  di/               # Koin module
```

### Conventions

- Interface prefix `I`, implementation suffix `Impl`:
  - Use cases: `IAddTodoUseCase`, `AddTodoUseCaseImpl`
  - Repositories: `ITodoRepository`, `TodoRepositoryImpl`
  - Data sources: `ITodoLocalDataSource`, `TodoLocalDataSourceImpl`
- Domain models suffixed with `Domain` (e.g., `TodoDomain`)
- Use cases expose `operator fun invoke()` for clean call sites
- ViewModels follow MVI: a single immutable `State` flow + a sealed `Action` consumed via `onAction`
- Compose UI: split `ScreenRoot` (resolves the ViewModel) from `Screen(state, onAction)` for testability
- Tests use **Kotest** (FunSpec style) — not JUnit or kotlin-test
- Architecture rules are enforced by Konsist in `composeApp/src/androidUnitTest/.../CleanArchitectureTest.kt`

### KMP Source Sets

All shared code lives in `composeApp/src/`:

- **commonMain** — Shared business logic, features, and Compose UI
- **androidMain** — Android `Activity`, SQLDelight Android driver, SharedPreferences settings, Logcat crash reporter
- **iosMain** — iOS `UIViewController` bridge, SQLDelight native driver, NSUserDefaults settings, NSLog crash reporter
- **desktopMain** — Desktop `Window`, JDBC SQLite driver, file-backed settings, console crash reporter
- **commonTest** — Shared tests (Kotest)
- **androidUnitTest** — JUnit + Konsist architecture tests

### Platform Bridge Pattern

`expect`/`actual` for platform-specific code:
- `Platform.kt` (commonMain) defines `expect` interfaces
- `Platform.android.kt`, `Platform.ios.kt`, `Platform.desktop.kt` provide `actual` implementations

### Crash reporting

`ICrashReporter` is wired through `loggingPlatformModule` per platform. The template ships log-only impls (`LogcatCrashReporter`, `NSLogCrashReporter`, `ConsoleCrashReporter`). Swap them for Firebase Crashlytics / Sentry / Bugsnag by replacing the binding in `loggingPlatformModule` (and adding the SDK to that platform's source set in `composeApp/build.gradle.kts`).

In release builds, `CrashReporterAntilog` forwards Napier `WARN`/`ERROR` events into the configured reporter — so anywhere code calls `Napier.e(...)`, the reporter sees it.

## Key Versions (gradle/libs.versions.toml)

- Kotlin: 2.3.0, Compose Multiplatform: 1.10.0, Material 3: 1.10.0-alpha05
- Koin: 4.1.1, SQLDelight: 2.0.2, Navigation: 2.9.2
- Kotest: 6.1.4, Konsist: 0.17.3
- Android SDK: min 24, target 36

## Compose UI rules

Follow the guidelines in `20-jetpack-compose-mistakes.md` (project root) when writing Compose UI. Highlights:

- Use `LaunchedEffect`/`DisposableEffect` for side effects, never raw coroutine launches in composables
- Immutable lists only for state (`listOf`, not `mutableListOf`)
- `collectAsStateWithLifecycle()` instead of `collectAsState()`
- `graphicsLayer` for transform/alpha animations
- Separate `ScreenRoot` (has ViewModel) from `Screen(state, onAction)` for testability
- Always use `key` in `LazyColumn` items
- No `return` in composable functions — use `let` or `if`
- Pass lambdas `() -> T` instead of values for frequently changing state
- No expanding sizes (`fillMaxWidth`) in reusable sub-composables — pass from outside
- Always apply Scaffold `paddingValues` to content

## Related skills

The `/android-*` skills ship with the template under `.claude/skills/` — Claude Code picks them up automatically. Use them for layer-specific guidance:

- `/android-arch` — Clean Architecture conventions (naming, layering, MVI)
- `/android-module-structure` — Module layout + Gradle convention plugins
- `/android-data-layer` — Repositories, data sources, mappers, DTOs
- `/android-presentation-mvi` — ViewModels, State/Action/Event, Root/Screen split
- `/android-di-koin` — Koin modules and ViewModel injection
- `/android-error-handling` — Result wrapper, DataError, UiText
- `/android-compose-ui` — Recomposition, side effects, animations, design system
- `/android-navigation` — Type-safe Navigation Compose graphs
- `/android-testing` — ViewModel tests with Kotest + Turbine + fakes
