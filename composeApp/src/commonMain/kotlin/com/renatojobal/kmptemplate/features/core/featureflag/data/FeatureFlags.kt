package com.renatojobal.kmptemplate.features.core.featureflag.data

/**
 * Compile-time feature flags for in-progress spikes.
 *
 * Plain `const val` instead of a runtime store because the audience is "one developer
 * flipping a flag and rebuilding." If you need remote or per-user gating, replace this
 * with a runtime FeatureFlagStore.
 *
 * Each flag should reference its spike issue and have a clear sunset path (either
 * promotion to production code paths or deletion when the spike closes).
 */
object FeatureFlags {
    /** Example flag — replace with your own and update or delete as you go. */
    const val EXAMPLE_FLAG: Boolean = false
}
