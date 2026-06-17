# KMP App Template

Kotlin Multiplatform starter — Android / iOS / Desktop — wired with Compose Multiplatform, Koin, SQLDelight, Material 3, and a clean-architecture-by-feature layout. Ships with a small Todos feature so the architecture is visible end-to-end from a single screen.

## What's inside

- `composeApp/` — the KMP module (commonMain + androidMain + iosMain + desktopMain)
- `iosApp/` — Xcode project that wraps the Compose UI in SwiftUI

Key pieces wired by the template:

| Concern | Library | Where to look |
|---|---|---|
| UI | Compose Multiplatform + Material 3 | `composeApp/src/commonMain/.../ui/theme`, `features/todo/ui/screen` |
| DI | Koin (KMP) | `di/AppModule.kt` + per-feature `*Module.kt` |
| Persistence | SQLDelight | `commonMain/sqldelight/.../Todo.sq`, `features/core/database` |
| Navigation | Navigation Compose (KMP) | `navigation/AppNavHost.kt`, `navigation/Routes.kt` |
| Logging | Napier | `features/core/logging` |
| Error model | `Result<T, DataError>` + UiText | `features/core/error` |
| Settings | `ISettingsStore` (SharedPrefs / NSUserDefaults / Properties) | `features/core/settings` |
| Architecture rules | Konsist | `androidUnitTest/.../CleanArchitectureTest.kt` |

## Use this template

1. Clone (no fork) into a new directory.
2. Rename — find/replace these globally:
   - `com.renatojobal.kmptemplate` → your package
   - `kmptemplate` → your namespace slug (used by SQLDelight generated package and Compose resources)
   - `KmpAppTemplate` / `KMP App Template` → your product name
3. Move directories under `composeApp/src/*/kotlin/com/renatojobal/kmptemplate/` to your new package.
4. Update `applicationId` in `composeApp/build.gradle.kts`, `rootProject.name` in `settings.gradle.kts`, and `PRODUCT_BUNDLE_IDENTIFIER` / `TEAM_ID` in `iosApp/Configuration/Config.xcconfig`.
5. Replace the Android launcher icons under `composeApp/src/androidMain/res/mipmap-*/`.

## Run it

```bash
# Android debug APK on a connected device or emulator
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run

# iOS — open Xcode and pick a simulator
open iosApp/iosApp.xcodeproj
```

## Tests

```bash
./gradlew :composeApp:commonTest         # Kotest, shared
./gradlew :composeApp:testDebugUnitTest  # Konsist architecture rules + Android-only tests
```

## Architecture cheatsheet

```
features/<feature_name>/
  domain/   { model, repository (interface), usecase (interface + Impl) }
  data/     { datasource, repository (Impl), mapper }
  ui/       { screen (Root + Screen), viewmodel, state (State + Action) }
  di/       { Koin module }
```

`data -> domain <- ui`. Domain is pure Kotlin. ViewModels go through use cases — never directly to a repository or data source.

The `androidUnitTest` source set ships a Konsist test (`CleanArchitectureTest.kt`) that enforces the layer rules + naming conventions on every build, so the architecture doesn't rot.

See `CLAUDE.md` for the full conventions list and `20-jetpack-compose-mistakes.md` for the Compose-specific guidelines.

## Ships with Claude Code skills

`.claude/skills/` contains the `/android-*` skills — Claude Code picks them up automatically when you work in this repo:

- `/android-arch` — Clean Architecture conventions
- `/android-module-structure` — Module layout + Gradle convention plugins
- `/android-data-layer` — Repositories, data sources, mappers
- `/android-presentation-mvi` — ViewModels, State/Action, Root/Screen split
- `/android-di-koin` — Koin modules and ViewModel injection
- `/android-error-handling` — Result wrapper, DataError, UiText
- `/android-compose-ui` — Recomposition, side effects, animations
- `/android-navigation` — Type-safe Navigation Compose graphs
- `/android-testing` — ViewModel tests with Kotest + Turbine + fakes

## License

MIT — see [LICENSE](LICENSE).
