package com.renatojobal.kmptemplate

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withNameStartingWith
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Architecture tests for the features package.
 *
 * Scoped to com.renatojobal.kmptemplate.features.
 */
class CleanArchitectureTest {

    private val featuresScope = Konsist.scopeFromPackage("com.renatojobal.kmptemplate.features..")

    // ==================== Layer Dependencies ====================

    @Test
    fun `clean architecture layers have correct dependencies`() {
        featuresScope.assertArchitecture {
            val domain = Layer("Domain", "..domain..")
            val data = Layer("Data", "..data..")
            val di = Layer("DI", "..di..")
            val ui = Layer("Ui", "..ui..")

            domain.dependsOnNothing()
            data.dependsOn(domain)
            di.dependsOn(domain, data)
            ui.dependsOn(domain)
        }
    }

    // ==================== Domain Layer ====================

    @Test
    fun `domain classes do not depend on data layer`() {
        featuresScope
            .classes()
            .withPackage("..domain..")
            .assertTrue { clazz ->
                val hasDataDependency = clazz.containingFile.imports.any { import ->
                    import.name.contains(".data.")
                }
                !hasDataDependency
            }
    }

    @Test
    fun `domain classes do not depend on ui layer`() {
        featuresScope
            .classes()
            .withPackage("..domain..")
            .assertTrue { clazz ->
                val hasUiDependency = clazz.containingFile.imports.any { import ->
                    import.name.contains(".ui.")
                }
                !hasUiDependency
            }
    }

    @Test
    fun `domain classes do not depend on Android framework`() {
        featuresScope
            .classes()
            .withPackage("..domain..")
            .assertTrue { clazz ->
                val hasAndroidDependency = clazz.containingFile.imports.any { import ->
                    import.name.startsWith("android.") ||
                        import.name.startsWith("androidx.")
                }
                !hasAndroidDependency
            }
    }

    @Test
    fun `domain classes do not depend on Firebase`() {
        featuresScope
            .classes()
            .withPackage("..domain..")
            .assertTrue { clazz ->
                val hasFirebaseDependency = clazz.containingFile.imports.any { import ->
                    import.name.contains("firebase")
                }
                !hasFirebaseDependency
            }
    }

    @Test
    fun `domain classes do not depend on Room`() {
        featuresScope
            .classes()
            .withPackage("..domain..")
            .assertTrue { clazz ->
                val hasRoomDependency = clazz.containingFile.imports.any { import ->
                    import.name.contains("androidx.room")
                }
                !hasRoomDependency
            }
    }

    // ==================== Naming Conventions ====================

    @Test
    fun `use case implementations have correct naming`() {
        featuresScope
            .classes()
            .withPackage("..domain.usecase..")
            .withNameEndingWith("Impl")
            .assertTrue { clazz ->
                clazz.name.endsWith("UseCaseImpl")
            }
    }

    @Test
    fun `use case interfaces have correct naming`() {
        featuresScope
            .interfaces()
            .withPackage("..domain.usecase..")
            .assertTrue { iface ->
                iface.name.startsWith("I") && iface.name.contains("UseCase")
            }
    }

    @Test
    fun `repository interfaces are in domain layer`() {
        featuresScope
            .interfaces()
            .withNameStartingWith("I")
            .withNameEndingWith("Repository")
            .assertTrue { iface ->
                iface.resideInPackage("..domain..")
            }
    }

    @Test
    fun `repository implementations are in data layer`() {
        featuresScope
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { clazz ->
                clazz.resideInPackage("..data..")
            }
    }

    // ==================== UI Layer ====================

    @Test
    fun `ui classes do not depend on data layer`() {
        featuresScope
            .classes()
            .withPackage("..ui..")
            .assertTrue { clazz ->
                val hasDataDependency = clazz.containingFile.imports.any { import ->
                    import.name.contains(".data.") &&
                        import.name.contains("com.renatojobal.kmptemplate.features")
                }
                !hasDataDependency
            }
    }

    @Test
    fun `ui classes do not import firebase`() {
        featuresScope
            .classes()
            .withPackage("..ui..")
            .assertTrue { clazz ->
                val hasFirebaseDependency = clazz.containingFile.imports.any { import ->
                    import.name.startsWith("com.google.firebase")
                }
                !hasFirebaseDependency
            }
    }

    @Test
    fun `viewmodels do not depend on data sources or repositories directly`() {
        featuresScope
            .classes()
            .withPackage("..ui.viewmodel..")
            .withNameEndingWith("ViewModel")
            .assertTrue { clazz ->
                val forbidden = clazz.containingFile.imports.any { import ->
                    import.name.contains(".domain.datasource.") ||
                        import.name.contains(".domain.repository.") ||
                        import.name.contains(".data.")
                }
                !forbidden
            }
    }

    @Test
    fun `viewmodels do not depend on core services directly`() {
        // ViewModels must go through use cases for any core service (settings, debug,
        // logging, etc.). Pure model imports from core are still allowed, as are the
        // cross-cutting error/UiText primitives in features.core.error.
        featuresScope
            .classes()
            .withPackage("..ui.viewmodel..")
            .withNameEndingWith("ViewModel")
            .assertTrue { clazz ->
                val forbidden = clazz.containingFile.imports.any { import ->
                    import.name.contains(".features.core.") &&
                        !import.name.contains(".model.") &&
                        !import.name.contains(".model") &&
                        !import.name.contains(".features.core.error.")
                }
                !forbidden
            }
    }

    @Test
    fun `viewmodels have correct naming`() {
        featuresScope
            .classes()
            .withPackage("..ui.viewmodel..")
            .filter { !it.name.endsWith("Test") }
            .assertTrue { clazz ->
                clazz.name.endsWith("ViewModel")
            }
    }

    @Test
    fun `screen state classes are data classes`() {
        featuresScope
            .classes()
            .withPackage("..ui..")
            .withNameEndingWith("ScreenState")
            .assertTrue { clazz ->
                clazz.hasDataModifier
            }
    }

    @Test
    fun `event classes are sealed`() {
        featuresScope
            .classes()
            .withPackage("..ui..")
            .withNameEndingWith("Event")
            .assertTrue { clazz ->
                clazz.hasSealedModifier
            }
    }
}
