package com.renatojobal.kmptemplate.features.core.accessibility.data

import androidx.compose.runtime.Composable

@Composable
actual fun isReduceMotionEnabled(): Boolean = false

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on desktop — back navigation is handled by app UI (no system back gesture).
}
